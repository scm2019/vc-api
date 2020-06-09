package org.jeecg.modules.vcapi.entity;

import java.io.Serializable;

/**
 * 福禄平台订单表实体
 * 表名 nideshop_commodity_order
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2020-04-13 19:25:08
 */
public class CommodityOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private String id;
    //订单id
    private String customerOrderNo;
    //订单请求福禄平台时间戳
    private String timeStamp;
    //订单创建时间
    private String createTime;
    //福禄平台商品编号
    private String productId;
    //购买数量
    private Integer buyNum;
    //充值账号
    private String chargeAccount;
    //充值数额，只有在充值话费的时候才会有这个值
    private String chargeValue;
    //三方平台返回码
    private String code;
    //三方平台返回结果内容
    private String message;
    //订单类型：1-话费 2-流量 3-卡密 4-直充
    private String orderType;
    //交易单价
    private Double orderPrice;
    //订单状态，1请求成功，2请求失败,订单交易终止，3，交易完成，定时任务只扫描状态为1的订单
    private String orderStatus;
    //本系统订单下的商品id
    private String orderGoodsId;
    //本系统下的order表的id
    private String orderSn;
    //订单类型1本系统，2外部
    private String orderUserType;
    //订单归属账号
    private String orderUser;

    public String getOrderUserType() {
        return orderUserType;
    }

    public void setOrderUserType(String orderUserType) {
        this.orderUserType = orderUserType;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    /**
     * 设置：主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public String getId() {
        return id;
    }
    /**
     * 设置：订单id
     */
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    /**
     * 获取：订单id
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }
    /**
     * 设置：订单请求福禄平台时间戳
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * 获取：订单请求福禄平台时间戳
     */
    public String getTimeStamp() {
        return timeStamp;
    }
    /**
     * 设置：订单创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：订单创建时间
     */
    public String getCreateTime() {
        return createTime;
    }
    /**
     * 设置：福禄平台商品编号
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * 获取：福禄平台商品编号
     */
    public String getProductId() {
        return productId;
    }
    /**
     * 设置：购买数量
     */
    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    /**
     * 获取：购买数量
     */
    public Integer getBuyNum() {
        return buyNum;
    }
    /**
     * 设置：充值账号
     */
    public void setChargeAccount(String chargeAccount) {
        this.chargeAccount = chargeAccount;
    }

    /**
     * 获取：充值账号
     */
    public String getChargeAccount() {
        return chargeAccount;
    }
    /**
     * 设置：充值数额，只有在充值话费的时候才会有这个值
     */
    public void setChargeValue(String chargeValue) {
        this.chargeValue = chargeValue;
    }

    /**
     * 获取：充值数额，只有在充值话费的时候才会有这个值
     */
    public String getChargeValue() {
        return chargeValue;
    }
    /**
     * 设置：三方平台返回码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取：三方平台返回码
     */
    public String getCode() {
        return code;
    }
    /**
     * 设置：三方平台返回结果内容
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取：三方平台返回结果内容
     */
    public String getMessage() {
        return message;
    }
    /**
     * 设置：订单类型：1-话费 2-流量 3-卡密 4-直充
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取：订单类型：1-话费 2-流量 3-卡密 4-直充
     */
    public String getOrderType() {
        return orderType;
    }
    /**
     * 设置：交易单价
     */
    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    /**
     * 获取：交易单价
     */
    public Double getOrderPrice() {
        return orderPrice;
    }
    /**
     * 设置：订单状态，1请求成功，2请求失败,订单交易终止，3，交易完成，定时任务只扫描状态为1的订单
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 获取：订单状态，1请求成功，2请求失败,订单交易终止，3，交易完成，定时任务只扫描状态为1的订单
     */
    public String getOrderStatus() {
        return orderStatus;
    }
    /**
     * 设置：本系统订单下的商品id
     */
    public void setOrderGoodsId(String orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    /**
     * 获取：本系统订单下的商品id
     */
    public String getOrderGoodsId() {
        return orderGoodsId;
    }
    /**
     * 设置：本系统下的order表的id
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    /**
     * 获取：本系统下的order表的id
     */
    public String getOrderSn() {
        return orderSn;
    }
}
