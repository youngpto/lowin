package com.young.lowin.interceptor;

import com.young.lowin.model.RouteRule;
import com.young.lowin.model.Rule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description: web拦截器
 * Author: young
 * Date: 2021-08-26
 * Time: 1:44
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LowInInterceptor implements HandlerInterceptor {

    private final List<RouteRule> routeRules;

    private final RuleHandler ruleHandler;

    private final LinkedHashMap<String, Rule> lowInGlobalRulePool;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 不是请求方法的直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 获取请求的方法 通用概念上即为controller下对应的接口函数
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 解决跨域问题
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response.setStatus(HttpStatus.OK.value());
            response.addHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
            response.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
            response.addHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
            return false;
        }

        String requestURI = request.getRequestURI();

        AntPathMatcher antPathMatcher = new AntPathMatcher();

        // 没有匹配上表示该路径未被拦截
        for (RouteRule routeRule : routeRules) {
            if (antPathMatcher.match(routeRule.getRoutePath(), requestURI)) {
                String ruleName = routeRule.getRuleName();
                if (!lowInGlobalRulePool.containsKey(ruleName)) {
                    continue;
                }
                ruleHandler.accessPoint(request, response, method, lowInGlobalRulePool.get(ruleName));
                break;
            }
        }
        return true;
    }
}
