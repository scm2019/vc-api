package org.jeecg.modules.vcapi.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.vcapi.entity.VcOrderRecharge;
import org.jeecg.modules.vcapi.enums.OrderStatusEnum;
import org.jeecg.modules.vcapi.service.ICallbackSevice;
import org.jeecg.modules.vcapi.service.IVcOrderRechargeService;
import org.jeecg.modules.vcapi.service.IVcProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
/*福禄平台订单回调*/
@Service
public class CallbackSeviceImpl implements ICallbackSevice {
    @Autowired
    private IVcOrderRechargeService iVcOrderRechargeService;
    @Autowired
    private IVcProductService iVcProductService;

    @Override
    public VcOrderRecharge orderCallback(String str) {
        JSONObject jsStr = JSONObject.parseObject(str);
        QueryWrapper<VcOrderRecharge> wrapper=new QueryWrapper<>();
        wrapper.lambda().eq(VcOrderRecharge::getOrderNo,jsStr.getString("customer_order_no"));
        VcOrderRecharge orderRecharge =iVcOrderRechargeService.getOne(wrapper,false);
        return  orderCallbackDealWithStrJson(str,orderRecharge);
    }

    //在外面处理数据，加上事物，某条数据发生异常不影响其他数据
    @Transactional(rollbackFor = Exception.class)
    public VcOrderRecharge orderCallbackDealWithStrJson(String respStrJson,VcOrderRecharge vcOrderRecharge){
        JSONObject jsonArr=JSONObject.parseObject(respStrJson);
        if(jsonArr.getString("order_status").equals("success")){//成功
            vcOrderRecharge.setOrderStatus(OrderStatusEnum.SUCCESS.getValue());
        }else  if(jsonArr.getString("order_status").equals("processing")){//失败
            vcOrderRecharge.setOrderStatus(OrderStatusEnum.FAILED.getValue());
        }else  if(jsonArr.getString("order_status").equals("failed")){//处理中
            vcOrderRecharge.setOrderStatus(OrderStatusEnum.UNDERWAY.getValue());
        }else  if(jsonArr.getString("order_status").equals("untreated")){//未处理
            vcOrderRecharge.setOrderStatus(OrderStatusEnum.WAIT.getValue());
        }
        vcOrderRecharge.setCallback(1);
        vcOrderRecharge.setCallbackTime(new Date());
        iVcOrderRechargeService.updateById(vcOrderRecharge);
        return vcOrderRecharge;
    }
}
