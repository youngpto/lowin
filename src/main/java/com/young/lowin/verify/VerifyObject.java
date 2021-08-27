package com.young.lowin.verify;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Description: 身份校验对象
 * Author: young
 * Date: 2021-08-26
 * Time: 3:32
 */
public interface VerifyObject {

    /**
     * 返回从请求中解析出的待校验信息
     *
     * @return 待校验信息（校验证书）
     */
    Object getVerifyKey();

    /**
     * 返回校验通过后得到的身份信息
     * tips: 可能与待校验信息一致也可能有更多信息被查询得到
     *
     * @return 校验成功后完善的身份信息（许可证）
     */
    Object getAuthLicence();
}
