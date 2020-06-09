package org.jeecg.modules.quartz.job;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.vcapi.entity.VcProduct;
import org.jeecg.modules.vcapi.service.IUpstreamSevice;
import org.jeecg.modules.vcapi.service.IVcProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.math.BigDecimal;
@Slf4j
@Configuration
@EnableScheduling
public class FuLuProductDataJob {
    @Autowired
    private IVcProductService vcProductService;
    @Autowired
    @Qualifier("FuLuUpstreamSevice")
    private IUpstreamSevice iUpstreamFuLuSevice;
    @Scheduled(cron = "00 00 02 * * ?")
    public void fuLuProductDataJob() {
        log.info("开始同步产品数据...........");
        ResponseBean rs = iUpstreamFuLuSevice.getProductByBizType();
        JSONArray json = JSONArray.parseArray(rs.getData().toString());
        if(null!=json&&json.size()>0){
            for (int i = 0; i <json.size(); i++) {
                VcProduct vcProduct = null;
                vcProduct = vcProductService.getById(json.getJSONObject(i).get("ProductId").toString());
                if(null == vcProduct){
                    vcProduct = new VcProduct();
                    vcProduct.setId(json.getJSONObject(i).get("ProductId").toString());
                    vcProduct.setCreateBy("admin");
                }
                vcProduct.setName(json.getJSONObject(i).get("Name").toString());
                vcProduct.setNumSpec(json.getJSONObject(i).get("NumSpec").toString());
                vcProduct.setPrice(new BigDecimal(json.getJSONObject(i).get("Price").toString()));
                vcProduct.setSaleDiscount(new BigDecimal(1));
                vcProduct.setSalePrice(new BigDecimal(json.getJSONObject(i).get("Price").toString()));
                vcProduct.setUnit(null);
                vcProduct.setUpdateBy("admin");
                vcProductService.saveOrUpdate(vcProduct);
            }
        }else{
            log.error("福禄产品数据为空!");
        }
    }
}
