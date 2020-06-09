package org.jeecg.modules.vcapi.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.vcapi.enums.ApiStatusEnum;
import org.jeecg.modules.vcapi.enums.ApiTypeEnum;
import org.jeecg.modules.vcapi.service.IUpstreamSevice;
import org.jeecg.modules.vcapi.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/*
*   根据产品类别查询佳诺产品
* */
@Service("JiaNuoUpstreamSevice")
@Slf4j
public class UpstreamSeviceImpl implements IUpstreamSevice {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.apiKey}")
    private String apiKey;
    @Value("${api.userId}")
    private String userId;
    @Value("${api.http.address}")
    private String apiHttpAddress;
    @Override
    public ResponseBean getProductByBizType(String bizType) {
        JSONObject jsonObject= queryApi(bizType, ApiTypeEnum.QueryProducts.getCode(),null);
        if (jsonObject==null)
        {
            return new ResponseBean(500,"网络访问失败！！！","网络访问失败！！！");
        }
        if (ApiStatusEnum.ERROR.getCode().equals(jsonObject.getString("status"))){
            return new ResponseBean(400,jsonObject.getString("code"),jsonObject.getString("msg"));
        }
        return new ResponseBean(200,jsonObject.getString("msg"),jsonObject.get("Products"));
    }

    @Override
    public ResponseBean getProductByBizType() {
        return null;
    }

    /**
     * @Author: Mr.Luke
     * @Description: 查询服务实现
     * @Date: 11:46 2019/10/15
     * @Param: [bizType, service]
     * @return: org.jeecg.modules.shiro.vo.ResponseBean
     */
    private JSONObject queryApi(String bizType, String service, SortedMap<Object,Object> param){
        SortedMap<Object,Object> signMap=new TreeMap<Object,Object>();
        StringBuilder url=new StringBuilder(apiHttpAddress);
        url.append("?Service="+service+"&V=2.0&UserId=");
        url.append(userId);
        signMap.put("UserId",userId);
        url.append("&BizType=");
        url.append(bizType);
        signMap.put("BizType",bizType);
        url.append("&Time=");
        long time=System.currentTimeMillis()/1000;
        url.append(time);
        signMap.put("Time",time);
        if (param!=null){
            signMap.putAll(param);
            param.forEach((key,value)->{
                url.append("&"+key+"="+value);
            });
        }
        url.append("&Sign=");
        String sign= SignUtil.createSign(signMap,apiKey);
        url.append(sign);
        log.debug("访问url:"+url);
        ResponseEntity<String> response=restTemplate.getForEntity(url.toString(), String.class);
        log.debug("响应结果："+response.getBody());
        if (!HttpStatus.OK.equals(response.getStatusCode())){
            log.info("查询"+service+"失败，错误代码："+response.getStatusCode());
            log.info("访问URL："+url);
            return null;
        }
        return JSONObject.parseObject(response.getBody());
    }

}
