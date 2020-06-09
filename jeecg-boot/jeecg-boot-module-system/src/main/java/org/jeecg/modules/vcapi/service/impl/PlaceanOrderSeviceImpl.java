package org.jeecg.modules.vcapi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.vcapi.entity.VcOrderRecharge;
import org.jeecg.modules.vcapi.entity.req.RechargeReqDto;
import org.jeecg.modules.vcapi.enums.ApiStatusEnum;
import org.jeecg.modules.vcapi.enums.ApiTypeEnum;
import org.jeecg.modules.vcapi.enums.OrderStatusEnum;
import org.jeecg.modules.vcapi.service.IPlaceanOrderSevice;
import org.jeecg.modules.vcapi.service.IVcOrderRechargeService;
import org.jeecg.modules.vcapi.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service("JiaNuoPlaceanOrderSevice")
@Slf4j
public class PlaceanOrderSeviceImpl implements IPlaceanOrderSevice {
    @Autowired
    private  RestTemplate restTemplate;
    @Autowired
    private  IVcOrderRechargeService vcOrderRechargeService;

    @Value("${api.apiKey}")
    private String apiKey;
    @Value("${api.userId}")
    private String userId;
    @Value("${api.http.address}")
    private String apiHttpAddress;

    @Override
    public ResponseBean recharge(VcOrderRecharge vcOrderRecharge,SortedMap<Object, Object> param) {

        JSONObject jsonObject=queryApi(vcOrderRecharge.getBizType(), ApiTypeEnum.SubmitOrder.getCode(),param);
        if (jsonObject==null)
        {
            vcOrderRecharge.setOrderStatus(OrderStatusEnum.ERROR.getValue());
            log.info("访问充值API，网络访问失败！！！");
            vcOrderRecharge.setRequestStatus("-500");
            vcOrderRecharge.setRequestMsg("网络连接失败");
            vcOrderRechargeService.save(vcOrderRecharge);
            return new ResponseBean(500,"网络访问失败！！！","网络访问失败！！！");
        }
        log.debug("访问充值API，成功");
        vcOrderRecharge.setRequestCode(jsonObject.getString("code"));
        vcOrderRecharge.setRequestStatus(jsonObject.getString("status"));
        vcOrderRecharge.setRequestMsg(jsonObject.getString("msg"));

        if (ApiStatusEnum.ERROR.getCode().equals(jsonObject.getString("status"))){
            log.info("访问充值API，结果失败,失败信息："+jsonObject.getString("msg"));
            vcOrderRecharge.setOrderStatus(OrderStatusEnum.FAILED.getValue());
            vcOrderRechargeService.save(vcOrderRecharge);
            return new ResponseBean(400,jsonObject.getString("code"),jsonObject.getString("msg"));
        }
        String orderStatus=jsonObject.getString("OrderStatus");
        OrderStatusEnum orderStatusEnum=OrderStatusEnum.getOrderStatusEnumByCode(orderStatus);
        vcOrderRecharge.setOrderStatus(orderStatusEnum.getValue());
        log.info("访问充值API，访问成功,订单 状态："+orderStatusEnum.getContent());
        vcOrderRechargeService.save(vcOrderRecharge);
        return new ResponseBean(200,orderStatus,orderStatusEnum.getContent());
    }

    @Override
    public ResponseBean recharge(Map queryMap, JSONArray json,VcOrderRecharge vcOrderRecharge) {
        return null;
    }

    @Override
    public ResponseBean recharge(VcOrderRecharge vcOrderRecharge) {
        return null;
    }

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
