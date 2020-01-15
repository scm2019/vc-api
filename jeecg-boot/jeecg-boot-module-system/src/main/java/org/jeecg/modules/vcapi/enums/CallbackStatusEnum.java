package org.jeecg.modules.vcapi.enums;

/**
 * @program: jeecg-boot-parent
 * @description: 回调状态枚举
 * @author: Mr.Luke
 * @create: 2019-10-16 13:24
 * @Version V1.0
 */
public enum CallbackStatusEnum {
    OK("ok","回调成功"),
    ERROR("error","回调失败"),
    ;

    private String code;
    private String content;

    CallbackStatusEnum(String code,String content){
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
