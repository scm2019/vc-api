package org.jeecg.modules.vcapi.enums;

public enum  RoleCodeEnum {

    ADMIN("admin","超级管理员"),
    CUSTOMER("customer","客户"),
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

    RoleCodeEnum(String roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }
}
