package org.jeecg.modules.vcapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.vcapi.entity.VcOrderRecharge;

import java.util.List;

/**
 * @Description: vc_order_recharge
 * @Author: jeecg-boot
 * @Date:   2019-10-14
 * @Version: V1.0
 */
public interface IVcOrderRechargeService extends IService<VcOrderRecharge> {

    List<VcOrderRecharge> getVcOrderRechargeList(VcOrderRecharge vcOrderRecharge);

    List<VcOrderRecharge> getAgainCallBack();

    IPage<VcOrderRecharge> queryPage(Page<VcOrderRecharge> page, VcOrderRecharge vcOrderRecharge, Boolean isAdmin, String username);

    List<VcOrderRecharge> exportExcelData(VcOrderRecharge vcOrderRecharge, Boolean isAdmin, String username);
}
