package org.jeecg.modules.vcapi.enums;

public enum BizTypeEnum {
    BAIWEI("BAIWEI","柏维"),
    FULU("FULU","福禄"),
    OIL("OIL","佳诺OIL"),
    MOBILE("MOBILE","佳诺MOBILE"),
    ECARD("ECARD","佳诺ECARD"),
    ;

    private String roleCode;

    private String roleName;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    BizTypeEnum(String roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }
}
