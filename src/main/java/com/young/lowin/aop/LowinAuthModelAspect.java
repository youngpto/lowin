package com.young.lowin.aop;

import com.young.lowin.interceptor.RuleHandler;
import com.young.lowin.model.AuthModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-09-20
 * Time: 7:31
 */
@Aspect
@Component
@ConditionalOnClass()
public class LowinAuthModelAspect {

    private Object injectionAuthModel(ProceedingJoinPoint pjp) throws Throwable {
        // 获取切面信息
        MethodSignature sign = (MethodSignature) pjp.getSignature();
        Method method = sign.getMethod();
        Object[] args = pjp.getArgs();

        // 获取当前请求对象主体
        AuthModel authModel = RuleHandler.authModel.get();

        // 请求主体注入
        int position = authModelPosition(method);
        if (position > -1) {
            args[position] = authModel;
        }

        return pjp.proceed(args);
    }

    private int authModelPosition(Method method) {
        int position = -1;
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType().equals(AuthModel.class)) {
                position = i;
            }
        }
        return position;
    }

    @Pointcut("@args(com.young.lowin.annotation.AuthModelAnnotation) && (@annotation(com.young.lowin.annotation.EnableAuthModel) || @within(com.young.lowin.annotation.EnableAuthModel))")
    public void methodAuthModel() {

    }

    @Around("methodAuthModel()")
    public Object beforeRequiredRoles(ProceedingJoinPoint pjp) throws Throwable {
        return injectionAuthModel(pjp);
    }
}
