package org.jeecg.modules.vcapi.mapper;
import	java.lang.reflect.Parameter;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.vcapi.entity.BaiweiProduct;

import java.util.List;

public interface BaiweiProductMapper extends BaseMapper<BaiweiProduct> {
    List<BaiweiProduct> getBaiweiProduct(@Param("productid") String productid);
}
