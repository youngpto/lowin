package com.young.lowin.verify.impl;

import com.young.lowin.verify.VerifyHandler;
import com.young.lowin.verify.VerifyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-08-26
 * Time: 4:39
 */
@Component
public class DefaultVerifyHandler implements VerifyHandler {

    @Autowired
    private DefaultVerifyObject defaultVerifyObject;

    @Override
    public VerifyObject createVerifyObject(HttpServletRequest request) {
        return defaultVerifyObject;
    }

    @Override
    public boolean verify(Object verifyKey) {
        return true;
    }
}
