package org.jeecg.modules.vcapi.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import org.jeecgframework.poi.excel.annotation.ExcelIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: vc_order_recharge
 * @Author: jeecg-boot
 * @Date:   2019-10-14
 * @Version: V1.0
 */
@Data
@Builder
@TableName("vc_order_recharge")
@AllArgsConstructor
@NoArgsConstructor
public class VcOrderRecharge implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;

	/**负责充值的账户ID*/
	@Excel(name = "负责充值的账户ID", width = 15)
	private String userId;
	/**业务类型(签名)*/
	@Excel(name = "业务类型(签名)", width = 15)
	private String bizType;
	/**充值账号*/
	@Excel(name = "充值账号", width = 15)
	private String accountVal;
	/**用户订单号*/
	@Excel(name = "用户订单号", width = 15)
	private String orderNo;
	/**标准产品 ID(签名)*/
	@Excel(name = "标准产品 ID(签名)", width = 15)
	private String productId;

	@Excel(name = "标准产品名称", width = 15)
	@TableField(exist = false)
	private String productName;

	/**购买数量*/
	@Excel(name = "购买数量", width = 15)
	private Integer buyNum;
	/**此 IP 需填写下单人的 IP，而不是接口调用方 IP*/
	@Excel(name = "此 IP 需填写下单人的 IP，而不是接口调用方 IP", width = 15)
	private String customerIp;
	/**特定产品充值时需要的参数，默认直接为空*/
	@Excel(name = "特定产品充值时需要的参数，默认直接为空", width = 15)
	private String extraData;
	/**请求响应代码*/
	@Excel(name = "请求响应代码", width = 15)
	private String requestCode;
	/**请求状态*/
	@Excel(name = "请求状态", width = 15)
	private String requestStatus;
	/**请求响应结果信息*/
	@Excel(name = "请求响应结果信息", width = 15)
	private String requestMsg;
	/**新增时间*/
	@Excel(name = "新增时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createTime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date updateTime;
	/**订单状态*/
	@Excel(name = "订单状态", width = 15)
	private Integer orderStatus;
	/**是否回调*/
	@Excel(name = "是否回调", width = 15)
	private Integer callback;
	/**回调时间*/
	@Excel(name = "回调时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date callbackTime;

	@Excel(name = "回调地址", width = 255)
	private String callbackAddress;

	private String createBy;
	private String updateBy;


	@Excel(name = "客户回调结果", width = 15)
	private String customerCallbackResult;
	/**用户订单号*/
	@Excel(name = "重新回调状态 null 不需要回调 1 等待回调，2，回调成功，3 回调失败", width = 15)
	private String againCallbackStatus;
	/**回调状态*/
	@Excel(name = "重新回调结", width = 15)
	private String againCallbackResult;
	/**回调时间*/
	@Excel(name = "回调时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date againCallbackTimes;

}
