package org.jeecg.modules.vcapi.service.impl;
import com.alibaba.fastjson.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.vcapi.entity.BaiweiProduct;
import org.jeecg.modules.vcapi.entity.BaiweiProductKey;
import org.jeecg.modules.vcapi.entity.VcCustomer;
import org.jeecg.modules.vcapi.entity.VcOrderRecharge;
import org.jeecg.modules.vcapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
@Service("BaiWeiPlaceanOrderSevice")
public class PlaceanOrderBaiWeiSeviceImpl implements IPlaceanOrderSevice {
    @Autowired
    private IBaiweiProductKeySevice iBaiweiProductKeySevice;
    @Autowired
    private IVcOrderRechargeService vcOrderRechargeService;
    @Autowired
    private IBaiweiProductSevice iBaiweiProductSevice;
    @Autowired
    private IVcCustomerService customerService;

    @Override
    public ResponseBean recharge(VcOrderRecharge vcOrderRecharge) {
        List<BaiweiProduct> baiweiProducts=iBaiweiProductSevice.getBaiweiProduct(vcOrderRecharge.getProductId());
        if(null==baiweiProducts||baiweiProducts.size()<=0){
            return new ResponseBean(200,"该分类下无此产品","该分类下无此产品");
        }
        List<BaiweiProductKey> list=iBaiweiProductKeySevice.getBaiweiProductKeyByid(vcOrderRecharge.getProductId());
        if(null==list||list.size()<vcOrderRecharge.getBuyNum()){
            return new ResponseBean(200,"产品库存不足","产品库存不足");
        }

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        VcCustomer customer = customerService.getCustomerByUserId(sysUser.getId());
        int  totalprices=Integer.valueOf(baiweiProducts.get(0).getProductDiscountprice())*vcOrderRecharge.getBuyNum()*customer.getDiscount().intValue();

        if((customer.getMoney().intValue()+customer.getQuota().intValue())<totalprices){
            return new ResponseBean(200,"余额不足","余额不足");
        }
        if(customer.getMoney().intValue()>totalprices){
            customer.setMoney(new BigDecimal((customer.getMoney().intValue())-totalprices));
        }else if(customer.getMoney().intValue()<totalprices&&customer.getQuota().intValue()>totalprices){
            customer.setQuota(new BigDecimal(customer.getQuota().intValue()-totalprices));
        }else if(customer.getMoney().intValue()<totalprices&&customer.getQuota().intValue()<totalprices){
            customer.setQuota(new BigDecimal(customer.getQuota().intValue()-totalprices+customer.getMoney().intValue()));
        }
        List<BaiweiProductKey> miyuelist=iBaiweiProductKeySevice.getBaiweiProductKeyByidlimit(vcOrderRecharge.getProductId(),vcOrderRecharge.getBuyNum());
        String[] miyue=new String[miyuelist.size()];
        for (int i = 0; i < miyuelist.size(); i++) {
            miyue[i]=miyuelist.get(i).getProductKey();
            iBaiweiProductKeySevice.upBaiweiProductKeyByid(sysUser.getUsername(),miyuelist.get(i).getProductKey());
        }

        vcOrderRecharge.setProductName(baiweiProducts.get(0).getProductName());
        vcOrderRecharge.setRequestCode("0");
        vcOrderRecharge.setRequestMsg("操作成功");
        vcOrderRecharge.setRequestStatus("ok");
        vcOrderRecharge.setOrderStatus(2);
        System.err.println(vcOrderRecharge);
        customerService.updateById(customer);
        vcOrderRechargeService.save(vcOrderRecharge);
        return new ResponseBean(200,"OK",miyue);
    }

    @Override
    public ResponseBean recharge(VcOrderRecharge vcOrderRecharge, SortedMap<Object, Object> param) {
        return null;
    }

    @Override
    public ResponseBean recharge(Map queryMap, JSONArray json, VcOrderRecharge vcOrderRecharge) {
        return null;
    }


}
