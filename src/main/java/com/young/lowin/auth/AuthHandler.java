package com.young.lowin.auth;

import com.young.lowin.model.AuthInfo;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * Description: 授权处理器
 * Author: young
 * Date: 2021-08-26
 * Time: 13:05
 */
public interface AuthHandler {

    AuthInfo getAuthInfo(Object authLicence);
}
