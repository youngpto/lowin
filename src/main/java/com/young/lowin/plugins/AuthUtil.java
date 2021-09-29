package com.young.lowin.plugins;

import com.young.lowin.interceptor.RuleHandler;
import com.young.lowin.model.AuthModel;

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

    public static AuthModel getAuthModel() {
        return RuleHandler.authModel.get();
    }

    @Deprecated
    public static boolean queryRoles(ConditionLink conditionLink, String... currentAuth) {
        return query(conditionLink, currentAuth);
    }

    @Deprecated
    public static boolean queryRoles(ConditionLinkAdapter adapter, String... currentAuth) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        return queryRoles(conditionLink, currentAuth);
    }

    @Deprecated
    public static void checkRoles(ConditionLink conditionLink, AuthFunction authFunction, String... currentAuth) {
        if (queryRoles(conditionLink, currentAuth)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    @Deprecated
    public static void checkRoles(ConditionLinkAdapter adapter, AuthFunction authFunction, String... currentAuth) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        checkRoles(conditionLink, authFunction, currentAuth);
    }

    public static boolean queryRoles(ConditionLink conditionLink) {
        return query(RuleHandler.authModel.get().getRoles(), conditionLink);
    }

    public static boolean queryRoles(ConditionLinkAdapter adapter) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        return queryRoles(conditionLink);
    }

    public static void checkRoles(ConditionLink conditionLink, AuthFunction authFunction) {
        if (queryRoles(conditionLink)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    public static void checkRoles(ConditionLinkAdapter adapter, AuthFunction authFunction) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        checkRoles(conditionLink, authFunction);
    }

    @Deprecated
    public static boolean queryPermissions(ConditionLink conditionLink, String... currentAuth) {
        return query(conditionLink, currentAuth);
    }

    @Deprecated
    public static boolean queryPermissions(ConditionLinkAdapter adapter, String... currentAuth) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        return queryPermissions(conditionLink, currentAuth);
    }

    @Deprecated
    public static void checkPermissions(ConditionLink conditionLink, AuthFunction authFunction, String... currentAuth) {
        if (queryRoles(conditionLink, currentAuth)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    @Deprecated
    public static void checkPermissions(ConditionLinkAdapter adapter, AuthFunction authFunction, String... currentAuth) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        checkPermissions(conditionLink, authFunction, currentAuth);
    }

    public static boolean queryPermissions(ConditionLink conditionLink) {
        return query(RuleHandler.authModel.get().getPermissions(), conditionLink);
    }

    public static boolean queryPermissions(ConditionLinkAdapter adapter) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        return queryPermissions(conditionLink);
    }

    public static void checkPermissions(ConditionLink conditionLink, AuthFunction authFunction) {
        if (queryPermissions(conditionLink)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    public static void checkPermissions(ConditionLinkAdapter adapter, AuthFunction authFunction) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        checkPermissions(conditionLink, authFunction);
    }

    public static boolean query(Set<String> currentAuth, ConditionLink conditionLink) {
        if (conditionLink == null) {
            System.err.println("Warning: condition is null!");
            return true;
        }
        return conditionLink.execute(currentAuth);
    }

    public static boolean query(Set<String> currentAuth, ConditionLinkAdapter adapter) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        return query(currentAuth, conditionLink);
    }

    public static void check(ConditionLink conditionLink, AuthFunction authFunction, String... currentAuth) {
        if (query(conditionLink, currentAuth)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    public static void check(ConditionLinkAdapter adapter, AuthFunction authFunction, String... currentAuth) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        check(conditionLink, authFunction, currentAuth);
    }

    public static boolean query(ConditionLink conditionLink, String... currentAuth) {
        return query(Arrays.stream(currentAuth).collect(Collectors.toSet()), conditionLink);
    }

    public static boolean query(ConditionLinkAdapter adapter, String... currentAuth) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        return query(conditionLink, currentAuth);
    }

    public static void check(ConditionLink conditionLink, AuthFunction authFunction) {
        if (query(conditionLink)) {
            if (authFunction != null) {
                authFunction.authFunction();
            }
        }
    }

    public static void check(ConditionLinkAdapter adapter, AuthFunction authFunction) {
        ConditionLink conditionLink = new ConditionLink();
        adapter.conditionLinkLogic(conditionLink);
        check(conditionLink, authFunction);
    }

    public static interface AuthFunction {
        void authFunction();
    }
}
