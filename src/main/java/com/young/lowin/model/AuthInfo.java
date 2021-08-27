package com.young.lowin.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * Description: 身份角色权限信息
 * Author: young
 * Date: 2021-08-26
 * Time: 19:18
 */
public interface AuthInfo extends Serializable {
    Collection<String> getRoles();

    Collection<String> getStringPermissions();

    void addRole(String role);

    void addRoles(Collection<String> roles);

    void addPermission(String permission);

    void addPermissions(Collection<String> permissions);
}
