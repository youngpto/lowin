package com.young.lowin.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description: 简单封装身份角色权限信息
 * Author: young
 * Date: 2021-08-26
 * Time: 19:37
 */
public class SimpleAuthInfo implements AuthInfo {
    private final Set<String> roles;

    private final Set<String> permissions;

    public SimpleAuthInfo() {
        this.roles = new HashSet<>();
        this.permissions = new HashSet<>();
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public Set<String> getStringPermissions() {
        return permissions;
    }

    @Override
    public void addRole(String role) {
        roles.add(role);
    }

    @Override
    public void addRoles(Collection<String> roles) {
        this.roles.addAll(roles);
    }

    @Override
    public void addPermission(String permission) {
        permissions.add(permission);
    }

    @Override
    public void addPermissions(Collection<String> permissions) {
        this.permissions.addAll(permissions);
    }
}
