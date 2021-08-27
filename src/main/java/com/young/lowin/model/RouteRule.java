package com.young.lowin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * Description: 路由规则模型
 * Author: young
 * Date: 2021-08-26
 * Time: 12:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteRule {

    private String routePath;

    private String ruleName;
}
