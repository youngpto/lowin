package com.young.lowin.config;

import cn.hutool.core.collection.CollUtil;
import com.young.lowin.model.RouteRule;
import com.young.lowin.model.Rule;
import com.young.lowin.properties.LowInAutoProperties;
import com.young.lowin.verify.impl.BlackListVerifyHandler;
import com.young.lowin.verify.impl.DefaultVerifyHandler;
import com.young.lowin.verify.impl.R404VerifyHandler;
import com.young.lowin.verify.impl.R500VerifyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-08-26
 * Time: 4:40
 */
@Configuration
@EnableConfigurationProperties(LowInAutoProperties.class)
@RequiredArgsConstructor
public class LowInAutoConfig {

    private final LowInAutoProperties lowInAutoProperties;

    private final DefaultVerifyHandler defaultVerifyHandler;

    private final R404VerifyHandler r404VerifyHandler;

    private final R500VerifyHandler r500VerifyHandler;

    private final BlackListVerifyHandler blackListVerifyHandler;

    @Bean
    public List<RouteRule> routeRules(RouteRuleFactory routeRuleFactory) {
        List<RouteRule> routeRules = new LinkedList<>();
        // 黑名单优先拦截
        if (CollUtil.isNotEmpty(lowInAutoProperties.getBlackList())) {
            List<RouteRule> blackList = lowInAutoProperties.getBlackList().stream().map(s -> new RouteRule(s, "blackList")).collect(Collectors.toList());
            routeRules.addAll(blackList);
        }
        // 白名单放行
        if (CollUtil.isNotEmpty(lowInAutoProperties.getWhiteList())) {
            List<RouteRule> blackList = lowInAutoProperties.getWhiteList().stream().map(s -> new RouteRule(s, "defaultRule")).collect(Collectors.toList());
            routeRules.addAll(blackList);
        }
        // 自定义认证规则处理 配置文件注入
        if (CollUtil.isNotEmpty(lowInAutoProperties.getRouteRules())) {
            routeRules.addAll(lowInAutoProperties.getRouteRules().stream().distinct().collect(Collectors.toList()));
        }
        // 自定义认证规则处理 配置类注入
        if (CollUtil.isNotEmpty(routeRuleFactory.getRouteRules())) {
            routeRules.addAll(routeRuleFactory.getRouteRules());
        }
        // 默认添加对所有路径进行放行
        routeRules.add(new RouteRule("/**", "lowin"));
        return routeRules;
    }

    @Bean
    public Rule defaultRule() {
        return new Rule("lowin", defaultVerifyHandler);
    }

    @Bean
    public Rule r404Rule() {
        return new Rule("r404", r404VerifyHandler);
    }

    @Bean
    public Rule r500Rule() {
        return new Rule("r500", r500VerifyHandler);
    }

    @Bean
    Rule blackListRule() {
        return new Rule("blackList", blackListVerifyHandler);
    }

    @Bean
    @ConditionalOnMissingBean(RuleFactoryBean.class)
    public RuleFactoryBean ruleFactoryBean() {
        return new RuleFactoryBean();
    }

    @Bean
    @ConditionalOnMissingBean(RouteRuleFactory.class)
    public RouteRuleFactory routeRuleFactory() {
        return new RouteRuleFactory();
    }

    /**
     * 内置规则不可被覆盖
     */
    @Bean
    public LinkedHashMap<String, Rule> lowInGlobalRulePool(
            @Qualifier("defaultRule") Rule defaultRule,
            @Qualifier("r404Rule") Rule r404Rule,
            @Qualifier("r500Rule") Rule r500Rule,
            @Qualifier("blackListRule") Rule blackListRule,
            RuleFactoryBean ruleFactoryBean) {
        LinkedHashMap<String, Rule> lowInGlobalRulePool = new LinkedHashMap<>(ruleFactoryBean.getRuleMap());
        lowInGlobalRulePool.put(defaultRule.getRuleName(), defaultRule);
        lowInGlobalRulePool.put(r404Rule.getRuleName(), r404Rule);
        lowInGlobalRulePool.put(r500Rule.getRuleName(), r500Rule);
        lowInGlobalRulePool.put(blackListRule.getRuleName(), blackListRule);
        return lowInGlobalRulePool;
    }
}
