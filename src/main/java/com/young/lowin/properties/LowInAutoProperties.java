package com.young.lowin.properties;

import com.young.lowin.model.RouteRule;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-08-26
 * Time: 4:50
 */
@ConfigurationProperties(prefix = "lowin")
@Data
public class LowInAutoProperties {

    List<RouteRule> routeRules;
}
