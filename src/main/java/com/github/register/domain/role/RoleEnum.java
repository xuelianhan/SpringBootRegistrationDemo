package com.github.register.domain.role;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
public enum RoleEnum {

    ROLE_USER,
    ROLE_MODERATOR,
    ROLE_ADMIN;


    public static RoleEnum getRoleEnum(String roleName) {
        return RoleEnum.valueOf("ROLE_" + roleName.toUpperCase());
    }

}
