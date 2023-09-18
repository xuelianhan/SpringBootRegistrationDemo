package com.github.register.domain.role;

import jakarta.persistence.*;

import java.util.Date;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
@Entity
@Table(name = "auth_role")
public class AuthRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 20)
    private RoleEnum roleName;

    @Column(name = "create_time")
    private Date createTime;

    private String description;

    public AuthRole() {}

    public AuthRole(RoleEnum roleName) {
        this.roleName = roleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleEnum getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleEnum name) {
        this.roleName = name;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
