package org.jeecg.modules.vcapi.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 充值请求参数实体
 * @author: Mr.Luke
 * @create: 2019-10-15 15:15
 * @Version V1.0
 */
@Data
@ApiModel("充值请求参数实体")
public class RechargeReqDto {

    @ApiModelProperty("业务类型")
    private String bizType;
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("购买商品ID")
    private String productId;
    @ApiModelProperty("充值账户号")
    private String accountVal;
    @ApiModelProperty("购买数量")
    private Integer buyNum;
    @ApiModelProperty("客户端IP")
    private String customerIP;
    @ApiModelProperty("特定产品充值时需要的参数，默认直接为空")
    private String extraData;

    @ApiModelProperty("回调地址")
    private String callbackAddress;
}
