package org.jeecg.modules.vcapi.enums;

/**
 * @program: jeecg-boot-parent
 * @description: api接口类型枚举
 * @author: Mr.Luke
 * @create: 2019-10-15 15:28
 * @Version V1.0
 */
public enum ApiTypeEnum {

    SubmitOrder("SubmitOrder","充值接口"),
    QueryOrder("QueryOrder","查询接口"),
    QueryBalance("QueryBalance","查询账户余额接口"),
    QueryProducts("QueryProducts","商品查询接口"),
    ;

    private String code;
    private String content;

    ApiTypeEnum(String code,String content){
        this.code=code;
        this.content=content;
    }

    public String getCode(){
        return this.code;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
