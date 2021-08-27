package com.young.lowin.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.young.lowin.annotation.Logical;
import com.young.lowin.annotation.RequiredPermissions;
import com.young.lowin.annotation.RequiredRoles;
import com.young.lowin.annotation.SkipRuleHandle;
import com.young.lowin.auth.AuthHandler;
import com.young.lowin.exception.sub.PermissionDeniedException;
import com.young.lowin.exception.sub.RequestFormatException;
import com.young.lowin.exception.sub.VerifyFailedException;
import com.young.lowin.model.AuthInfo;
import com.young.lowin.model.Rule;
import com.young.lowin.verify.VerifyHandler;
import com.young.lowin.verify.VerifyObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description: 规则流程处理器
 * Author: young
 * Date: 2021-08-26
 * Time: 17:09
 */
@Component
public class RuleHandler {

    /**
     * 拦截器接入点
     *
     * @param request  请求体
     * @param response 响应体
     * @param method   对应接口
     * @param rule     认证授权规则
     */
    public void accessPoint(HttpServletRequest request, HttpServletResponse response, Method method, Rule rule) throws RequestFormatException, VerifyFailedException, PermissionDeniedException {
        // 被SkipRuleHandle注解修饰的方法即使被拦截也会被放行
        SkipRuleHandle annotation = method.getAnnotation(SkipRuleHandle.class);
        if (annotation != null) {
            return;
        }
        // 此处可以先对web请求进行参数校验，参数校验错误抛出请求格式错误异常
        VerifyHandler verifyHandler = rule.getVerifyHandler();
        if (!verifyHandler.isAllowExecution(request, response)) {
            throw new RequestFormatException("校验所需参数不足或类型错误");
        }
        // 以下三种情况应当放行
        if (verifyHandler.isLoginURL(request) || verifyHandler.isLogoutURL(request) || verifyHandler.isRegisterURL(request)) {
            return;
        }
        Object authLicence = verify(verifyHandler, request);
        List<AuthHandler> authHandlers = rule.getAuthHandlers();

        RequiredRoles requiredRoles = method.getAnnotation(RequiredRoles.class);
        RequiredPermissions requiredPermissions = method.getAnnotation(RequiredPermissions.class);
        if (requiredPermissions == null && requiredRoles == null) {
            return;
        }
        Set<String> rAnn = new HashSet<>();
        Set<String> pAnn = new HashSet<>();
        if (requiredRoles != null && ArrayUtil.isNotEmpty(requiredRoles.value())) {
            rAnn = Arrays.stream(requiredRoles.value()).collect(Collectors.toSet());
        }
        if (requiredPermissions != null && ArrayUtil.isNotEmpty(requiredPermissions.value())) {
            pAnn = Arrays.stream(requiredPermissions.value()).collect(Collectors.toSet());
        }
        Set<String> rTemp = new HashSet<>();
        Set<String> pTemp = new HashSet<>();
        for (AuthHandler authHandler : authHandlers) {
            AuthInfo authInfo = authHandler.getAuthInfo(authLicence);
            rTemp.addAll(authInfo.getRoles());
            pTemp.addAll(authInfo.getStringPermissions());
        }
        if (requiredRoles != null) {
            if (!isAllowAuth(rAnn, rTemp, requiredRoles.logical())) {
                throw new PermissionDeniedException("权限不足");
            }
        }
        if (requiredPermissions != null) {
            if (!isAllowAuth(pAnn, pTemp, requiredPermissions.logical())) {
                throw new PermissionDeniedException("权限不足");
            }
        }
    }

    private Object verify(VerifyHandler verifyHandler, HttpServletRequest request) throws VerifyFailedException {
        VerifyObject verifyObject = verifyHandler.createVerifyObject(request);
        if (verifyHandler.verify(verifyObject.getVerifyKey())) {
            verifyHandler.verifySuccessCallBack(verifyObject);
        }
        return verifyObject.getAuthLicence();
    }

    private boolean isAllowAuth(Set<String> target, Set<String> current, Logical logical) {
        if (logical.equals(Logical.AND)) {
            return CollUtil.containsAll(current, target);
        } else {
            return CollUtil.containsAny(current, target);
        }
    }
}
