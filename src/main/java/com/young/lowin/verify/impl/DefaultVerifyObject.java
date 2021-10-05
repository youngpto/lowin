package com.young.lowin.verify.impl;

import com.young.lowin.verify.VerifyObject;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Description: 使用单例模式
 * Author: young
 * Date: 2021-10-05
 * Time: 22:37
 */
@Component
public class DefaultVerifyObject implements VerifyObject {
    @Override
    public Object getVerifyKey() {
        return "lowin";
    }

    @Override
    public Object getAuthLicence() {
        return "lowin";
    }
}
