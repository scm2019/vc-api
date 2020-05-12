package org.jeecg.modules.vcapi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.vcapi.entity.VcProduct;

/**
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2020-04-21
 * @Version: V1.0
 */
public interface VcProductMapper extends BaseMapper<VcProduct> {

    @Delete("delete from vc_product")
    void deleteAll();
}
