package com.young.lowin.verify;

import com.young.lowin.exception.sub.VerifyFailedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * Description: 校验身份处理器
 * <pre>
 *     解析request对象得到携带身份信息（校验证书）的认证对象
 *     认证对象通过认证后，授予相关身份信息（许可证）
 * </pre>
 * Author: young
 * Date: 2021-08-26
 * Time: 3:08
 */
public interface VerifyHandler {

    /**
     * 生成认证对象，只含有校验证书
     *
     * @param request web请求
     * @return 认证对象
     */
    VerifyObject createVerifyObject(HttpServletRequest request);

    /**
     * 校验当前认证对象证书是否合格
     *
     * @return 校验结果
     */
    boolean verify(Object verifyKey) throws VerifyFailedException;

    default void verifySuccessCallBack(VerifyObject verifyObject) {

    }

    default boolean isAllowExecution(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    default boolean isLoginURL(HttpServletRequest request) {
        return request.getRequestURI().toLowerCase().contains("login");
    }

    default boolean isLogoutURL(HttpServletRequest request) {
        return request.getRequestURI().toLowerCase().contains("logout");
    }

    default boolean isRegisterURL(HttpServletRequest request) {
        return request.getRequestURI().toLowerCase().contains("register");
    }
}
