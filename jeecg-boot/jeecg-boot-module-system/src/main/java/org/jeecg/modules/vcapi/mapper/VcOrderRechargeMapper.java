package org.jeecg.modules.vcapi.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.vcapi.entity.VcOrderRecharge;

/**
 * @Description: vc_order_recharge
 * @Author: jeecg-boot
 * @Date:   2019-10-14
 * @Version: V1.0
 */
public interface VcOrderRechargeMapper extends BaseMapper<VcOrderRecharge> {

    List<VcOrderRecharge> getVcOrderRechargeNew(VcOrderRecharge vcOrderRecharge);

    IPage<VcOrderRecharge> queryPage(@Param("page") Page<VcOrderRecharge> page,@Param("item") VcOrderRecharge vcOrderRecharge,@Param("isAdmin") Boolean isAdmin,@Param("username") String username);

    List<VcOrderRecharge> exportExcelData(@Param("item") VcOrderRecharge vcOrderRecharge,@Param("isAdmin") Boolean isAdmin,@Param("username") String username);
}
