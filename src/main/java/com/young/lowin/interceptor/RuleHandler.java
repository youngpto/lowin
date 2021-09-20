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
import com.young.lowin.model.AuthModel;
import com.young.lowin.model.IAuthModel;
import com.young.lowin.model.Rule;
import com.young.lowin.verify.VerifyHandler;
import com.young.lowin.verify.VerifyObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
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
@Slf4j
public class RuleHandler {

    public final static ThreadLocal<AuthModel> authModel = new ThreadLocal<>();

    /**
     * 拦截器接入点
     *
     * @param request  请求体
     * @param response 响应体
     * @param method   对应接口
     * @param rule     认证授权规则
     */
    public void accessPoint(HttpServletRequest request, HttpServletResponse response, Method method, Rule rule) throws RequestFormatException, VerifyFailedException {
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

        // 声明请求对象主体
        AuthModel authModel = new AuthModel();

        // 检验身份
        Object authLicence = verify(verifyHandler, request);
        if (verifyHandler instanceof IAuthModel) {
            IAuthModel iAuthModel = (IAuthModel) verifyHandler;
            iAuthModel.customInfo(authModel);
        }

        // 身份授权，只根据定义的方法获取当前用户拥有的权限，在aop切面中判断是否满足授权结果
        List<AuthHandler> authHandlers = rule.getAuthHandlers();
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        for (AuthHandler authHandler : authHandlers) {
            AuthInfo authInfo = authHandler.getAuthInfo(authLicence);
            roles.addAll(authInfo.getRoles());
            permissions.addAll(authInfo.getStringPermissions());

            if (authHandler instanceof IAuthModel) {
                IAuthModel iAuthModel = (IAuthModel) authHandler;
                iAuthModel.customInfo(authModel);
            }
        }
        authModel.add("roles", roles).add("permissions", permissions);

        request.setAttribute("auth", authModel);
        RuleHandler.authModel.set(authModel);
    }

    private Object verify(VerifyHandler verifyHandler, HttpServletRequest request) throws VerifyFailedException {
        VerifyObject verifyObject = verifyHandler.createVerifyObject(request);
        if (verifyHandler.verify(verifyObject.getVerifyKey())) {
            verifyHandler.verifySuccessCallBack(verifyObject);
        } else {
            throw new VerifyFailedException("身份认证错误！");
        }
        return verifyObject.getAuthLicence();
    }
}
