package org.jeecg.modules.vcapi.entity.res;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 商品响应实体DTO
 * @author: Mr.Luke
 * @create: 2019-10-14 17:42
 * @Version V1.0
 */
@Data
public class ProductResDto {

    private String ProductId;
    private String Name;
    private BigDecimal SaleDiscount;
    private BigDecimal SalePrice;
    private BigDecimal Price;
    private String  Unit;
    private String NumSpec;
}
