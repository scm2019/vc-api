package org.jeecg.modules.vcapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.vcapi.entity.BaiweiProduct;

import java.util.List;

public interface IBaiweiProductSevice extends IService<BaiweiProduct> {
    List<BaiweiProduct> getBaiweiProduct(String productid);
}
