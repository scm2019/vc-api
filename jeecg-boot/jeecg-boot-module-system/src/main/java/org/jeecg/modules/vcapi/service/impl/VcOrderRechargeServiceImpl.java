package org.jeecg.modules.vcapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.vcapi.entity.VcOrderRecharge;
import org.jeecg.modules.vcapi.mapper.VcOrderRechargeMapper;
import org.jeecg.modules.vcapi.service.IVcOrderRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Description: vc_order_recharge
 * @Author: jeecg-boot
 * @Date:   2019-10-14
 * @Version: V1.0
 */
@Service
public class VcOrderRechargeServiceImpl extends ServiceImpl<VcOrderRechargeMapper, VcOrderRecharge> implements IVcOrderRechargeService {

    private final VcOrderRechargeMapper vcOrderRechargeMapper;

    @Autowired
    public VcOrderRechargeServiceImpl(VcOrderRechargeMapper vcOrderRechargeMapper) {
        this.vcOrderRechargeMapper = vcOrderRechargeMapper;
    }

    @Override
    public List<VcOrderRecharge> getVcOrderRechargeList(VcOrderRecharge vcOrderRecharge) {
        return vcOrderRechargeMapper.getVcOrderRechargeNew(vcOrderRecharge);
    }
}
