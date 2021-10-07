package com.young.lowin.config;

import cn.hutool.core.collection.CollUtil;
import com.young.lowin.model.RouteRule;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-10-07
 * Time: 21:47
 */
public class RouteRuleFactory {

    @Getter
    private final List<RouteRule> routeRules;

    public RouteRuleFactory() {
        routeRules = new LinkedList<>();
    }

    public RouteRuleFactory addRouteRule(RouteRule routeRule) {
        if (routeRule != null) {
            routeRules.add(routeRule);
        }
        return this;
    }

    public RouteRuleFactory addRouteRule(String route,String rule) {
        return addRouteRule(new RouteRule(route, rule));
    }

    public RouteRuleFactory addRouteRules(Collection<RouteRule> insert) {
        if (CollUtil.isNotEmpty(insert)) {
            routeRules.addAll(insert);
        }
        return this;
    }
}
