package org.jeecg.modules.vcapi.enums;

/**
 * @program: jeecg-boot-parent
 * @description:
 * @author: Mr.Luke
 * @create: 2019-10-15 15:02
 * @Version V1.0
 */
public enum ApiStatusEnum {
    OK("ok","访问成功"),
    ERROR("error","访问失败"),
    ;

    private String code;
    private String content;

    ApiStatusEnum(String code,String content){
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
