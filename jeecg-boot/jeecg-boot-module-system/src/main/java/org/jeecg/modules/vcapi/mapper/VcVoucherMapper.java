package org.jeecg.modules.vcapi.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.vcapi.entity.VcVoucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.vcapi.vo.VcVoucherVo;

/**
 * @Description: 虚拟卡券数据
 * @Author: jeecg-boot
 * @Date:   2020-04-24
 * @Version: V1.0
 */
public interface VcVoucherMapper extends BaseMapper<VcVoucher> {

    IPage<VcVoucher> queryPage(@Param("page") Page<VcVoucher> page,@Param("item") VcVoucherVo vcVoucher);
}
