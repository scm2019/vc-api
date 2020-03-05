package org.jeecg.modules.vcapi.enums;

/**
 * @description: 主动回调状态
 * @author: Mr.Luke
 * @create: 2020-03-05 15:11
 * @Version V1.0
 */
public enum  AgainCallbackStatus {

    WAIT("1","等待回调"),
    SUCCESS("2","回调成功"),
    FAIL("3","回调失败"),
            ;

    private String code;
    private String content;

    AgainCallbackStatus(String code,String content){
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
