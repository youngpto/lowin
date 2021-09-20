package com.young.lowin.model;

import cn.hutool.core.collection.CollUtil;
import com.young.lowin.annotation.AuthModelAnnotation;
import com.young.lowin.annotation.Logical;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description: 提供给外部使用的权限属性
 * Author: young
 * Date: 2021-08-27
 * Time: 16:37
 */
@AuthModelAnnotation
public class AuthModel extends HashMap<String, Object> {

    public AuthModel add(String key, Object value) {
        put(key, value);
        return this;
    }

    public AuthModel addAll(Map<? extends String, ?> map) {
        putAll(map);
        return this;
    }

    public boolean hasRules(String... rules) {
        return hasRules(Logical.AND, rules);
    }

    public boolean hasRules(Logical logical, String... rules) {
        return checkRoles(Arrays.stream(rules).collect(Collectors.toSet()), logical);
    }

    public boolean hasPermissions(String... permissions) {
        return hasPermissions(Logical.AND, permissions);
    }

    public boolean hasPermissions(Logical logical, String... permissions) {
        return checkPermissions(Arrays.stream(permissions).collect(Collectors.toSet()), logical);
    }

    @SuppressWarnings("unchecked")
    public boolean checkRoles(Set<String> custom, Logical logical) {
        return checkLogical((Set<String>) get("roles"), custom, logical);
    }

    @SuppressWarnings("unchecked")
    public boolean checkPermissions(Set<String> custom, Logical logical) {
        return checkLogical((Set<String>) get("permissions"), custom, logical);
    }

    private boolean checkLogical(Set<String> custom, Set<String> required, Logical logical) {
        if (logical.equals(Logical.AND)) {
            return CollUtil.containsAll(custom, required);
        } else {
            return CollUtil.containsAny(custom, required);
        }
    }
}
