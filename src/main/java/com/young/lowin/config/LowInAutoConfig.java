package com.young.lowin.config;

import cn.hutool.core.collection.CollUtil;
import com.young.lowin.model.RouteRule;
import com.young.lowin.model.Rule;
import com.young.lowin.properties.LowInAutoProperties;
import com.young.lowin.verify.impl.DefaultVerifyHandler;
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

    @Bean
    public List<RouteRule> routeRules() {
        List<RouteRule> routeRules = new LinkedList<>();
        if (CollUtil.isNotEmpty(lowInAutoProperties.getRouteRules())) {
            routeRules.addAll(lowInAutoProperties.getRouteRules().stream().distinct().collect(Collectors.toList()));
        }
        routeRules.add(new RouteRule("/**", "lowin"));
        return routeRules;
    }

    @Bean
    public Rule defaultRule() {
        return new Rule("lowin", defaultVerifyHandler);
    }

    @Bean
    @ConditionalOnMissingBean(RuleFactoryBean.class)
    public RuleFactoryBean ruleFactoryBean() {
        return new RuleFactoryBean();
    }

    @Bean
    public LinkedHashMap<String, Rule> lowInGlobalRulePool(@Qualifier("defaultRule") Rule defaultRule,
                                                           RuleFactoryBean ruleFactoryBean) {
        LinkedHashMap<String, Rule> lowInGlobalRulePool = new LinkedHashMap<>(ruleFactoryBean.getRuleMap());
        lowInGlobalRulePool.put(defaultRule.getRuleName(), defaultRule);
        return lowInGlobalRulePool;
    }
}
