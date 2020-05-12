package org.jeecg.modules.vcapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.vcapi.entity.VcCustomer;

import java.util.List;

/**
 * @Description: 客户信息
 * @Author: jeecg-boot
 * @Date:   2020-04-22
 * @Version: V1.0
 */
public interface IVcCustomerService extends IService<VcCustomer> {

    IPage<VcCustomer> queryPage(Page<VcCustomer> page, VcCustomer vcCustomer,boolean isAdmin,String userName);

    VcCustomer getCustomerByUserId(String id);

    List<VcCustomer> exportListData(VcCustomer vcCustomer, boolean isAdmin, String username);
}
