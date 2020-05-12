package org.jeecg.modules.vcapi.service.impl;

import org.jeecg.modules.vcapi.entity.VcProduct;
import org.jeecg.modules.vcapi.mapper.VcProductMapper;
import org.jeecg.modules.vcapi.service.IVcProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2020-04-21
 * @Version: V1.0
 */
@Service
public class VcProductServiceImpl extends ServiceImpl<VcProductMapper, VcProduct> implements IVcProductService {

}
