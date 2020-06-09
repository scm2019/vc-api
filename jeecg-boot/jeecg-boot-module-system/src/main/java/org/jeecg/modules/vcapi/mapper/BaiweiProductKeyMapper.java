package org.jeecg.modules.vcapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.vcapi.entity.BaiweiProduct;
import org.jeecg.modules.vcapi.entity.BaiweiProductKey;

import java.util.List;

public interface BaiweiProductKeyMapper extends BaseMapper<BaiweiProductKey> {
    List<BaiweiProductKey> getBaiweiProductKeyByid(@Param("productid") String productid);
    List<BaiweiProductKey> getBaiweiProductKeyByidlimit(@Param("productid") String productid,@Param("sl") int sl);
    void upBaiweiProductKeyByid(@Param("realname") String realname,@Param("key") String key);
}
