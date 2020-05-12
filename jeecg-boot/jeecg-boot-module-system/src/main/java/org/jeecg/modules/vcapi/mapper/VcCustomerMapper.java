package org.jeecg.modules.vcapi.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.vcapi.entity.VcCustomer;

/**
 * @Description: 客户信息
 * @Author: jeecg-boot
 * @Date:   2020-04-22
 * @Version: V1.0
 */
public interface VcCustomerMapper extends BaseMapper<VcCustomer> {

    IPage<VcCustomer> queryPage(@Param("page") Page<VcCustomer> page,@Param("item") VcCustomer vcCustomer,@Param("isAdmin") boolean isAdmin,@Param("userName") String userName);

    VcCustomer getCustomerByUserId(@Param("userId") String userId);

    List<VcCustomer> exportListData(@Param("item") VcCustomer vcCustomer, @Param("isAdmin") boolean isAdmin, @Param("userName") String username);
}
