package com.young.lowin.verify.impl;

import com.young.lowin.exception.sub.VerifyFailedException;
import com.young.lowin.verify.VerifyHandler;
import com.young.lowin.verify.VerifyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-10-05
 * Time: 22:24
 */
@Component
public class R500VerifyHandler implements VerifyHandler {

    @Autowired
    private DefaultVerifyObject defaultVerifyObject;

    @Override
    public VerifyObject createVerifyObject(HttpServletRequest request) {
        return defaultVerifyObject;
    }

    @Override
    public boolean verify(Object verifyKey) throws VerifyFailedException {
        throw new VerifyFailedException("500接口异常");
    }
}
