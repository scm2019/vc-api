package org.jeecg.modules.vcapi.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.vcapi.entity.VcCustomer;
import org.jeecg.modules.vcapi.mapper.VcCustomerMapper;
import org.jeecg.modules.vcapi.service.IVcCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 客户信息
 * @Author: jeecg-boot
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@Service
public class VcCustomerServiceImpl extends ServiceImpl<VcCustomerMapper, VcCustomer> implements IVcCustomerService {

    private final VcCustomerMapper vcCustomerMapper;

    @Autowired
    public VcCustomerServiceImpl(VcCustomerMapper vcCustomerMapper){
        this.vcCustomerMapper = vcCustomerMapper;
    }

    @Override
    public IPage<VcCustomer> queryPage(Page<VcCustomer> page, VcCustomer vcCustomer, boolean isAdmin, String userName) {
        return vcCustomerMapper.queryPage(page,vcCustomer,isAdmin,userName);
    }

    @Override
    public VcCustomer getCustomerByUserId(String id) {
        return vcCustomerMapper.getCustomerByUserId(id);
    }

    @Override
    public List<VcCustomer> exportListData(VcCustomer vcCustomer, boolean isAdmin, String username) {
        return vcCustomerMapper.exportListData(vcCustomer,isAdmin,username);
    }
}
