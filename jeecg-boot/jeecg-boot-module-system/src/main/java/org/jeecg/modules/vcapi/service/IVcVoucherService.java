package org.jeecg.modules.vcapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.vcapi.entity.VcVoucher;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.vcapi.vo.VcVoucherVo;

/**
 * @Description: 虚拟卡券数据
 * @Author: jeecg-boot
 * @Date:   2020-04-24
 * @Version: V1.0
 */
public interface IVcVoucherService extends IService<VcVoucher> {

    IPage<VcVoucher> queryPage(Page<VcVoucher> page, VcVoucherVo vcVoucher);
}
