package org.jeecg.modules.vcapi.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.vcapi.enums.ApiStatusEnum;
import org.jeecg.modules.vcapi.enums.ApiTypeEnum;
import org.jeecg.modules.vcapi.service.IUpstreamSevice;
import org.jeecg.modules.vcapi.util.HttpClientHelper;
import org.jeecg.modules.vcapi.util.InputParamSign;
import org.jeecg.modules.vcapi.util.jsonMessageUtils;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
/*
* 查询所有福禄产品
* */
@Service("FuLuUpstreamSevice")
@Slf4j
public class UpstreamFuluSeviceImpl implements IUpstreamSevice {
    @Override
    public ResponseBean getProductByBizType() {
        log.info("开始查询福禄平台所有产品...........");
        Map<String, String> params=new HashMap<>();
        Map<String,String> queryMap=new HashMap<>();
        JSONObject jsonObject=queryList(queryMap);
        Map<String, String> jsonMap=JSONObject.toJavaObject(jsonObject,Map.class);
        JSONArray jsonArr=jsonObject.getJSONArray("result");
        DecimalFormat df = new DecimalFormat("#.00");
        for (int i = 0; i < jsonArr.size();i++){
            jsonArr.getJSONObject(i).put("SaleDiscount",1);
            jsonArr.getJSONObject(i).put("NumSpec",1);
            jsonArr.getJSONObject(i).put("SalePrice",df.format(Double.valueOf(jsonArr.getJSONObject(i).get("purchase_price").toString())*100));
            jsonArr.getJSONObject(i).put("purchase_price",df.format(Double.valueOf(jsonArr.getJSONObject(i).get("purchase_price").toString())*100));
        }
        jsonArr=jsonSerial(jsonArr);
        return new ResponseBean(200,jsonObject.getString("msg"),jsonArr);
    }

    @Override
    public ResponseBean getProductByBizType(String bizType) {
        return null;
    }

    public JSONObject queryList(Map<String,String> params) {
        Map<String, Object> map=jsonMessageUtils.jsonStr(JSON.toJSONString(params),"fulu.goods.list.get");
        map.put("sign", InputParamSign.Sign(map, jsonMessageUtils.SYS_SECRET));
        String mapJson = JSON.toJSONString(map);//map转
        String respStrJson= HttpClientHelper.sendJsonHttpPost("https://openapi.fulu.com/api/getway",mapJson);
        JSONObject jsStr = JSONObject.parseObject(respStrJson);
        JSONArray jsonArr=jsStr.getJSONArray("result");
        return jsStr;
    }

    public JSONArray jsonSerial(Object object){
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
        serializer.config(SerializerFeature.DisableCircularReferenceDetect, true);
        serializer.getNameFilters().add(new NameFilter()
        {
            @Override
            public String process(Object object, String propertyName,
                                  Object propertyValue)
            {
                if (propertyName.matches("product_name"))
                    return "Name";
                if (propertyName.matches("product_id"))
                    return "ProductId";
                if (propertyName.matches("purchase_price"))
                    return "Price";
                return propertyName;
            }
        });
        serializer.write(object);
        return JSON.parseArray(out.toString());
    }

}
