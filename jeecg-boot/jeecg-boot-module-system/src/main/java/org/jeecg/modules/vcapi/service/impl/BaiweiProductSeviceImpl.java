package org.jeecg.modules.vcapi.service.impl;
import	java.awt.Desktop.Action;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.vcapi.entity.BaiweiProduct;
import org.jeecg.modules.vcapi.mapper.BaiweiProductMapper;
import org.jeecg.modules.vcapi.service.IBaiweiProductSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BaiweiProductSeviceImpl extends ServiceImpl<BaiweiProductMapper, BaiweiProduct> implements IBaiweiProductSevice {

    @Autowired
    private BaiweiProductMapper baiweiProductMapper;

    @Override
    public List<BaiweiProduct> getBaiweiProduct(String productid) {
        return baiweiProductMapper.getBaiweiProduct(productid);
    }
}
