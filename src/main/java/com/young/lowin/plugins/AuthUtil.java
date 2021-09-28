package com.young.lowin.plugins;

import com.young.lowin.interceptor.RuleHandler;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-09-28
 * Time: 15:15
 */
public class AuthUtil {

    public static boolean queryRoles(ConditionLink conditionLink, String... currentAuth) {
        return conditionLink.execute(Arrays.stream(currentAuth).collect(Collectors.toSet()));
    }

    public static void checkRoles(ConditionLink conditionLink, AuthFunction authFunction, String... currentAuth) {
        if (queryRoles(conditionLink, currentAuth)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    public static boolean queryRoles(ConditionLink conditionLink) {
        return conditionLink.execute(RuleHandler.authModel.get().getRoles());
    }

    public static void checkRoles(ConditionLink conditionLink, AuthFunction authFunction) {
        if (queryRoles(conditionLink)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    public static boolean queryPermissions(ConditionLink conditionLink, String... currentAuth) {
        return conditionLink.execute(Arrays.stream(currentAuth).collect(Collectors.toSet()));
    }

    public static void checkPermissions(ConditionLink conditionLink, AuthFunction authFunction, String... currentAuth) {
        if (queryRoles(conditionLink, currentAuth)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    public static boolean queryPermissions(ConditionLink conditionLink) {
        return conditionLink.execute(RuleHandler.authModel.get().getPermissions());
    }

    public static void checkPermissions(ConditionLink conditionLink, AuthFunction authFunction) {
        if (queryRoles(conditionLink)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    public static boolean query(Set<String> currentAuth, ConditionLink conditionLink) {
        return conditionLink.execute(currentAuth);
    }

    public static void check(ConditionLink conditionLink, AuthFunction authFunction, String... currentAuth) {
        if (queryRoles(conditionLink, currentAuth)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    public static boolean query(ConditionLink conditionLink, String... currentAuth) {
        return conditionLink.execute(Arrays.stream(currentAuth).collect(Collectors.toSet()));
    }

    public static void check(ConditionLink conditionLink, AuthFunction authFunction) {
        if (queryRoles(conditionLink)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    public static interface AuthFunction {
        void authFunction();
    }
}
