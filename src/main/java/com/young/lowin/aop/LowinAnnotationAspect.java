package com.young.lowin.aop;

import com.young.lowin.annotation.RequiredPermissions;
import com.young.lowin.annotation.RequiredRoles;
import com.young.lowin.exception.sub.PermissionDeniedException;
import com.young.lowin.interceptor.RuleHandler;
import com.young.lowin.model.AuthModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-08-30
 * Time: 9:57
 */
@Aspect
@Component
public class LowinAnnotationAspect {

    private Object injectionAnnotation(ProceedingJoinPoint pjp) throws Throwable {
        // 获取切面信息
        MethodSignature sign = (MethodSignature) pjp.getSignature();
        Method method = sign.getMethod();
        Object[] args = pjp.getArgs();

        // 获取当前请求对象主体
        AuthModel authModel = RuleHandler.authModel.get();

        // 判断是否需要校验身份
        RequiredRoles requiredRoles = method.getAnnotation(RequiredRoles.class);
        if (requiredRoles != null) {
            Set<String> custom = Arrays.stream(requiredRoles.value()).collect(Collectors.toSet());
            if (!authModel.checkRoles(custom, requiredRoles.logical())) {
                throw new PermissionDeniedException("当前角色不满足要求");
            }
        }

        // 判断是否需要检验权限
        RequiredPermissions requiredPermissions = method.getAnnotation(RequiredPermissions.class);
        if (requiredPermissions != null) {
            Set<String> custom = Arrays.stream(requiredPermissions.value()).collect(Collectors.toSet());
            if (!authModel.checkPermissions(custom, requiredPermissions.logical())) {
                throw new PermissionDeniedException("当前权限不满足要求");
            }
        }

        return pjp.proceed(args);
    }

    @Pointcut("@annotation(com.young.lowin.annotation.RequiredRoles) || @annotation(com.young.lowin.annotation.RequiredPermissions)")
    public void annotation() {

    }

    @Around("annotation()")
    public Object beforeRequiredRoles(ProceedingJoinPoint pjp) throws Throwable {
        return injectionAnnotation(pjp);
    }
}
