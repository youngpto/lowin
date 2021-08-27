package com.young.lowin.exception.sub;

import com.young.lowin.exception.LowInException;

/**
 * Created with IntelliJ IDEA.
 * Description: 请求格式错误
 * Author: young
 * Date: 2021-08-26
 * Time: 18:36
 */
public class RequestFormatException extends LowInException {
    public RequestFormatException(String message) {
        super(message);
    }
}
