package org.jeecg.modules.quartz.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.vcapi.entity.VcProduct;
import org.jeecg.modules.vcapi.entity.res.ProductResDto;
import org.jeecg.modules.vcapi.service.IVcProductService;
import org.jeecg.modules.vcapi.service.VcRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class SyncProductDataJob {

    @Autowired
    private VcRechargeService vcRechargeService;

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private IVcProductService vcProductService;


    @Scheduled(cron = "00 00 02 * * ?")
    public void syncProductDataJob(){
        log.info("开始同步产品数据...........");
        List<DictModel> bizTypeList = sysDictService.queryDictItemsByCode("BizType");
        log.info("开始根据bizType 查询对应的产品数据");
        if(CollectionUtils.isNotEmpty(bizTypeList)){
            //删除vc_product表中数据
            //vcProductService.deleteAll();
            bizTypeList.stream().forEach(bizType->{
                log.info("开始根据具体的bizType对应的产品数据");
                ResponseBean productResp = vcRechargeService.getProductByBizType(bizType.getValue());
                if(productResp.getCode() == 200){
                    Object objData = productResp.getData();
                    if(null != objData){
                        List<ProductResDto> productResDtos = JSONArray.parseArray(objData.toString(), ProductResDto.class);
                        if(CollectionUtils.isNotEmpty(productResDtos)){
                            productResDtos.stream().forEach(dto->{
                                //先去库里查询是否存在改code 的产品数据
                                VcProduct vcProduct = null;
                                vcProduct = vcProductService.getById(dto.getProductId());
                                if(null == vcProduct){
                                    vcProduct = new VcProduct();
                                    vcProduct.setId(dto.getProductId());
                                    vcProduct.setCreateBy("admin");
                                }
                                vcProduct.setName(dto.getName());
                                vcProduct.setNumSpec(dto.getNumSpec());
                                vcProduct.setPrice(dto.getPrice());
                                vcProduct.setSaleDiscount(dto.getSaleDiscount());
                                vcProduct.setSalePrice(dto.getSalePrice());
                                vcProduct.setUnit(dto.getUnit());
                                vcProduct.setUpdateBy("admin");
                                vcProductService.saveOrUpdate(vcProduct);
                            });
                        }
                    }
                }else{
                    log.error("根据具体的bizType对应的产品数据出错，bizType:"+bizType.getValue());
                }
            });
        }else{
            log.error("查询BizType 数据为空!!!!");
        }
    }
}
