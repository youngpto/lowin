package com.young.lowin.config;

import cn.hutool.core.util.StrUtil;
import com.young.lowin.model.Rule;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-08-27
 * Time: 9:06
 */
public class RuleFactoryBean {

    @Setter
    @Getter
    private Map<String, Rule> ruleMap;

    public RuleFactoryBean() {
        ruleMap = new LinkedHashMap<>();
    }

    public RuleFactoryBean addRule(Rule rule) {
        if (checkName(rule)) {
            ruleMap.put(rule.getRuleName(), rule);
        }
        return this;
    }

    public RuleFactoryBean addAllRule(Collection<Rule> rules) {
        Map<String, Rule> collect = rules.stream()
                .filter(this::checkName)
                .collect(Collectors.toMap(Rule::getRuleName, rule -> rule));
        ruleMap.putAll(collect);
        return this;
    }

    private boolean checkName(Rule rule) {
        if (StrUtil.isEmpty(rule.getRuleName())) {
            return false;
        }
        return !ruleMap.containsKey(rule.getRuleName());
    }
}
