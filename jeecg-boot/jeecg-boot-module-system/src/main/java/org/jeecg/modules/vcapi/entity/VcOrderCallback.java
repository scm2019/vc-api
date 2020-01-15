package org.jeecg.modules.vcapi.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: vc_order_callback
 * @Author: jeecg-boot
 * @Date:   2019-10-14
 * @Version: V1.0
 */
@Data
@Builder
@TableName("vc_order_callback")
public class VcOrderCallback implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**负责充值账号ID*/
	@Excel(name = "负责充值账号ID", width = 15)
	private String userId;
	/**被充值的用户账号*/
	@Excel(name = "被充值的用户账号", width = 15)
	private String accountVal;
	@Excel(name = "业务类型", width = 15)
	private String bizType;
	/**用户订单号*/
	@Excel(name = "用户订单号", width = 15)
	private String orderNo;
	/**订单状态*/
	@Excel(name = "订单状态", width = 15)
	private Integer orderStatus;
	/**回调状态*/
	@Excel(name = "回调状态", width = 15)
	private String callbackStatus;
	/**回调时间*/
	@Excel(name = "回调时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date callbackTime;
}
