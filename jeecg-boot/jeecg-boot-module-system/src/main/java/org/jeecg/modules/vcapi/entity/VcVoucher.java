package org.jeecg.modules.vcapi.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 虚拟卡券数据
 * @Author: jeecg-boot
 * @Date:   2020-04-24
 * @Version: V1.0
 */
@Data
@TableName("vc_voucher")
public class VcVoucher implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**卡名称*/
	@Excel(name = "卡名称", width = 15)
	private String name;
	/**卡号*/
	@Excel(name = "卡号", width = 15)
	private String cardCode;
	/**SKU*/
	@Excel(name = "SKU", width = 15)
	private String sku;

	@Excel(name="卡密",width = 15)
	private String password;

	@Excel(name = "卡描述", width = 15)
	private String cardDesc;

	/**卡状态 0|作废 1|待激活 2|有效 3|冻结 4|挂失 5|失效*/
	//@Excel(name = "卡状态 0|作废 1|待激活 2|有效 3|冻结 4|挂失 5|失效", width = 15)
	private String status;

	@Excel(name = "卡状态描述", width = 15)
	@TableField(exist = false)
	private String statusDesc;
	/**卡面余额*/
	@Excel(name = "卡面余额", width = 15)
	private BigDecimal balance;
	/**卡类型  1 一次性卡 2 重复卡*/
	@Excel(name = "卡类型  1 一次性卡 2 重复卡", width = 15)
	private Integer type;
	/**激活时间*/
	@Excel(name = "激活时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date activeDate;
	/**过期时间*/
	@Excel(name = "过期时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date expireDate;
	/**是否已消费*/
	@Excel(name = "是否已消费", width = 15)
	private Integer isUsed;
	/**卡面图片*/
	@Excel(name = "卡面图片", width = 255)
	private String mark;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private String createBy;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	private String updateBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createTime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date updateTime;
	/**卡面金额*/
	@Excel(name = "卡面金额", width = 15)
	private BigDecimal amount;

	@Excel(name = "发卡行", width = 15)
	private String issuingBank;

	@Excel(name="客户id" ,width =  15)
	private String customerId;

	@Excel(name="客户名称" ,width =  15)
	@TableField(exist = false)
	private String customerName;
}
