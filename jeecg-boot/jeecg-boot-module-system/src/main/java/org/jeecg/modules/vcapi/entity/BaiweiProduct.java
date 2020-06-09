package org.jeecg.modules.vcapi.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.io.Serializable;

/**
*@Description: 柏维公司产品表
*@Author: OuYang ZhiQiang
*@date: 2020-06-08
**/
@Data
@TableName("baiwei_product")
public class BaiweiProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    /**主键*/
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    @Excel(name = "产品编号", width = 15)
    private String productId;
    @Excel(name = "产品名称",width = 15)
    private String productName;
    @Excel(name = "产品原价", width = 15)
    private String productPrice;
    @Excel(name = "产品折扣", width = 15)
    private String productDiscount;
    @Excel(name = "折后价", width = 15)
    private String productDiscountprice;
    @Excel(name = "产品状态", width = 15)
    private String productState;
}
