package org.jeecg.modules.vcapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.vcapi.entity.BaiweiProduct;
import org.jeecg.modules.vcapi.entity.BaiweiProductKey;
import org.jeecg.modules.vcapi.mapper.BaiweiProductKeyMapper;
import org.jeecg.modules.vcapi.mapper.BaiweiProductMapper;
import org.jeecg.modules.vcapi.service.IBaiweiProductKeySevice;
import org.jeecg.modules.vcapi.service.IBaiweiProductSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BaiweiProductKeySeviceImpl extends ServiceImpl<BaiweiProductKeyMapper, BaiweiProductKey> implements IBaiweiProductKeySevice {

    @Autowired
    private BaiweiProductKeyMapper baiweiProductKeyMapper;

    @Override
    public List<BaiweiProductKey> getBaiweiProductKeyByid(String productid) {
        return baiweiProductKeyMapper.getBaiweiProductKeyByid(productid);
    }

    @Override
    public List<BaiweiProductKey> getBaiweiProductKeyByidlimit(String productid, int sl) {
        return baiweiProductKeyMapper.getBaiweiProductKeyByidlimit(productid,sl);
    }

    @Override
    public void upBaiweiProductKeyByid(String realname,String key) {
        baiweiProductKeyMapper.upBaiweiProductKeyByid(realname,key);
    }
}
