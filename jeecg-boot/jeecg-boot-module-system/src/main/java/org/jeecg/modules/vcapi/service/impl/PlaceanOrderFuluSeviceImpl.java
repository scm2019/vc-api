package org.jeecg.modules.vcapi.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.vcapi.entity.VcOrderRecharge;
import org.jeecg.modules.vcapi.enums.OrderStatusEnum;
import org.jeecg.modules.vcapi.service.IPlaceanOrderSevice;
import org.jeecg.modules.vcapi.service.IVcOrderRechargeService;
import org.jeecg.modules.vcapi.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("FuluPlaceanOrderSevice")
public class PlaceanOrderFuluSeviceImpl implements IPlaceanOrderSevice {
    @Autowired
    private IVcOrderRechargeService vcOrderRechargeService;
    @Override
    public ResponseBean recharge(Map queryMap, JSONArray json,VcOrderRecharge vcOrderRecharge) {
        for (int i = 0; i < json.size(); i++) {
             String ProductId=json.getJSONObject(i).get("ProductId").toString();
             if(ProductId.equals(queryMap.get("productId"))){
                 queryMap.put("product_type",json.getJSONObject(i).get("product_type"));
                 vcOrderRecharge.setProductName(json.getJSONObject(i).get("Name").toString());
             }
        }

        String url="";
        String modhUrl="";
        if(queryMap.get("product_type").equals("直充")){
            url="https://openapi.fulu.com/api/getway";
            modhUrl="fulu.order.direct.add";
        }else  if(queryMap.get("product_type").equals("卡密")){
            url="https://openapi.fulu.com/api/getway";
            modhUrl="fulu.order.card.add";
        }else{
            vcOrderRecharge.setRequestStatus("-500");
            vcOrderRecharge.setRequestMsg("商品类型无法匹配");
            vcOrderRechargeService.save(vcOrderRecharge);
            return new ResponseBean(506,"商品类型无法匹配：",queryMap.get("product_type"));
        }
        //查询条件转换成json
        Map<String,Object> bizContentMap= AssembleBizContentMap(queryMap);
        String queryMapJson =  JSON.toJSONString(bizContentMap);//map转json

        //获取报文
        Map<String, Object> map= jsonMessageUtils.jsonStr(queryMapJson,modhUrl);
        map.put("sign", InputParamSign.Sign(map, jsonMessageUtils.SYS_SECRET));
        String mapJson = JSON.toJSONString(map);//map转

        //请求获取数据
        String respStrJson= HttpClientHelper.sendJsonHttpPost(url,mapJson);
        JSONObject jsonObject = JSON.parseObject(respStrJson);
        if(jsonObject==null){
            vcOrderRecharge.setOrderStatus(1);
            vcOrderRecharge.setRequestCode(jsonObject.get("code").toString());
            vcOrderRecharge.setRequestMsg(jsonObject.get("message").toString());
            vcOrderRecharge.setRequestStatus(jsonObject.get("code").toString());
            vcOrderRechargeService.save(vcOrderRecharge);
            return new ResponseBean(200, OrderStatusEnum.ERROR.getCode(),respStrJson);
        }
        if(jsonObject.get("code").toString().equals("2004")){
            vcOrderRecharge.setOrderStatus(3);
            vcOrderRecharge.setRequestCode(jsonObject.get("code").toString());
            vcOrderRecharge.setRequestMsg(jsonObject.get("message").toString());
            vcOrderRecharge.setRequestStatus(jsonObject.get("code").toString());
            vcOrderRechargeService.save(vcOrderRecharge);
            return new ResponseBean(200, OrderStatusEnum.FAILED.getCode(),respStrJson);
        }
        vcOrderRecharge.setRequestCode(jsonObject.get("code").toString());
        vcOrderRecharge.setRequestMsg(jsonObject.get("message").toString());
        vcOrderRecharge.setRequestStatus(jsonObject.get("code").toString());
        vcOrderRechargeService.save(vcOrderRecharge);
        return new ResponseBean(200, OrderStatusEnum.WAIT.getCode(),respStrJson);
    }

    @Override
    public ResponseBean recharge(VcOrderRecharge vcOrderRecharge) {
        return null;
    }

    @Override
    public ResponseBean recharge(VcOrderRecharge vcOrderRecharge, SortedMap<Object, Object> param) {
        return null;
    }

    public  Map<String,Object>  AssembleBizContentMap(Map queryMap){
        Map<String,Object> bizContentMap=new HashMap();
        bizContentMap.put("product_id",queryMap.get("productId").toString());//商品id
        if(queryMap.get("orderUserType").toString().equals("1")){
            bizContentMap.put("customer_order_no", UUID.randomUUID().toString().replaceAll("-", ""));//订单id
        }else{
            bizContentMap.put("customer_order_no",queryMap.get("orderNo"));//订单id
        }
        bizContentMap.put("buy_num",Integer.parseInt(queryMap.get("buyNum").toString()));//充值数量
        if(queryMap.get("product_type").equals("直充")){
            bizContentMap.put("charge_account",queryMap.get("accountVal"));//充值账号
        }
        return bizContentMap;//map转
    }

}
