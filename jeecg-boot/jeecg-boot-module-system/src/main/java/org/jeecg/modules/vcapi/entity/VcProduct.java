package org.jeecg.modules.vcapi.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2020-04-21
 * @Version: V1.0
 */
@Data
@TableName("vc_product")
public class VcProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**产品名称*/
	@Excel(name = "产品名称", width = 15)
	private String name;
	/**原价*/
	@Excel(name = "原价", width = 15)
	private BigDecimal price;
	/**折扣*/
	@Excel(name = "折扣", width = 15)
	private BigDecimal saleDiscount;
	/**销售价格*/
	@Excel(name = "销售价格", width = 15)
	private BigDecimal salePrice;
	/**单位*/
	@Excel(name = "单位", width = 15)
	private String unit;
	/**数字描述*/
	@Excel(name = "数字描述", width = 15)
	private String numSpec;
}
