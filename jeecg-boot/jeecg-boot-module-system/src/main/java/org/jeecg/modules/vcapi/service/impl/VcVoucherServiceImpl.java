package org.jeecg.modules.vcapi.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.vcapi.entity.VcVoucher;
import org.jeecg.modules.vcapi.mapper.VcVoucherMapper;
import org.jeecg.modules.vcapi.service.IVcVoucherService;
import org.jeecg.modules.vcapi.vo.VcVoucherVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 虚拟卡券数据
 * @Author: jeecg-boot
 * @Date:   2020-04-24
 * @Version: V1.0
 */
@Service
public class VcVoucherServiceImpl extends ServiceImpl<VcVoucherMapper, VcVoucher> implements IVcVoucherService {

    private final VcVoucherMapper vcVoucherMapper;

    @Autowired
    public VcVoucherServiceImpl(VcVoucherMapper vcVoucherMapper){
        this.vcVoucherMapper = vcVoucherMapper;
    }
    /**
     * 列表查询数据，根据 cardCode name sku 模糊查询  过期时间  激活时间 范围查询 卡状态  查询
     * @param page
     * @param vcVoucher
     * @return
     */
    @Override
    public IPage<VcVoucher> queryPage(Page<VcVoucher> page, VcVoucherVo vcVoucher) {
        return vcVoucherMapper.queryPage(page,vcVoucher);
    }
}
