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
 * @Description: 客户信息
 * @Author: jeecg-boot
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@Data
@TableName("vc_customer")
public class VcCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**用户id*/
	//@Excel(name = "用户id", width = 15)
	private String userId;
	@Excel(name = "用户名称",width = 15)
	@TableField(exist = false)
	private String userName;
	/**公司名称*/
	@Excel(name = "公司名称", width = 15)
	private String companyName;
	/**对接人*/
	@Excel(name = "对接人", width = 15)
	private String broker;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
	private String phone;
	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
	private String email;
	/**营业执照*/
	@Excel(name = "营业执照", width = 15)
	private String certificate;

	private Integer radioValue;

	/**授信（类似花呗额度）*/
	@Excel(name = "授信额度", width = 15)
	private BigDecimal quota;
	/**加款*/
	@Excel(name = "余额", width = 15)
	private BigDecimal money;
	/**
	 * 折扣
	 */
	@Excel(name="折扣",width = 15)
	private BigDecimal discount;

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
}
