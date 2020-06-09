package org.jeecg.modules.vcapi.service.impl;
import	java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.vcapi.entity.BaiweiProduct;
import org.jeecg.modules.vcapi.mapper.BaiweiProductMapper;
import org.jeecg.modules.vcapi.service.IBaiweiProductSevice;
import org.jeecg.modules.vcapi.service.IUpstreamSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/*查询所有柏维产品*/
@Service("BaiweiUpstreamSevice")
@Slf4j
public class UpstreamBaiweiSeviceImpl implements IUpstreamSevice {

    @Autowired
    private IBaiweiProductSevice iBaiweiProductSevice;
    @Override
    public ResponseBean getProductByBizType(String bizType) {
        log.info("开始查询柏维平台所有产品...........");
        List<BaiweiProduct> list=iBaiweiProductSevice.getBaiweiProduct("");
        if(null==list||list.size()<=0){
            return new ResponseBean(200,"该分类下无产品","该分类下无产品");
        }
        String str = JSON.toJSONString(list);
        JSONArray json = JSONArray.parseArray(str);
        json=jsonSerial(json);
        return new ResponseBean(200,"OK",json);
    }

    @Override
    public ResponseBean getProductByBizType() {
        return null;
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
                if (propertyName.matches("productName"))
                    return "Name";
                if (propertyName.matches("productDiscountprice"))
                    return "SalePrice";
                if (propertyName.matches("productPrice"))
                    return "Price";
                if (propertyName.matches("productDiscount"))
                    return "SaleDiscount";
                return propertyName;
            }
        });
        serializer.write(object);
        return JSON.parseArray(out.toString());
    }
}
