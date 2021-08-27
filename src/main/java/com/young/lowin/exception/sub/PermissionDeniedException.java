package com.young.lowin.exception.sub;

import com.young.lowin.exception.LowInException;

/**
 * Created with IntelliJ IDEA.
 * Description: 权限不足异常
 * Author: young
 * Date: 2021-08-27
 * Time: 10:19
 */
public class PermissionDeniedException extends LowInException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
