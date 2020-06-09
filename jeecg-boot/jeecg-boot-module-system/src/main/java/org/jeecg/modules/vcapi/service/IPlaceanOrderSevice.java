package org.jeecg.modules.vcapi.service;

import com.alibaba.fastjson.JSONArray;
import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.vcapi.entity.VcOrderRecharge;
import org.jeecg.modules.vcapi.entity.req.RechargeReqDto;

import java.util.Map;
import java.util.SortedMap;

public interface IPlaceanOrderSevice {
    /*佳诺产品下单*/
    ResponseBean recharge(VcOrderRecharge vcOrderRecharge, SortedMap<Object, Object> param);
    /*福禄产品下单*/
    ResponseBean recharge(Map queryMap, JSONArray json,VcOrderRecharge vcOrderRecharge);
    /*柏维产品下单*/
    ResponseBean recharge(VcOrderRecharge vcOrderRecharge);
}
