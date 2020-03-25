package org.jeecg.modules.vcapi.enums;

/**
 * @program: jeecg-boot-parent
 * @description: 业务类型
 * @author: Mr.Luke
 * @create: 2019-10-14 17:22
 * @Version V1.0
 */
public enum OrderStatusEnum {

    WAIT("WAIT",0,"等待充值"),
    ERROR("ERROR",1,"网络异常"),
    SUCCESS("SUCCESS",2,"充值成功"),
    FAILED("FAILED",3,"充值失败"),
    UNDERWAY("UNDERWAY",4,"下单成功，充值中"),
    NOTEXIST("NOTEXIST",5,"订单不存在，下单失败"),
    NUDEFOUNDREPOSE("NUDEFOUNDREPOSE",6,"未知状态"),
    ;

    private String code;
    private Integer value;
    private String content;

    OrderStatusEnum(String code,Integer value, String content){
        this.code=code;
        this.value=value;
        this.content=content;
    }

    public String getCode(){
        return this.code;
    }

    public Integer getValue(){
        return this.value;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return this.code;
    }

    public static OrderStatusEnum getOrderStatusEnumByCode(String code){
        for(OrderStatusEnum sexEnum : OrderStatusEnum.values()){
            if(code.equals(sexEnum.getCode())){
                return sexEnum;
            }
        }
        return NUDEFOUNDREPOSE;
    }
}
