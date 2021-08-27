package com.young.lowin.model;

import com.young.lowin.auth.AuthHandler;
import com.young.lowin.verify.VerifyHandler;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description: 认证授权规则模型
 * Author: young
 * Date: 2021-08-26
 * Time: 12:50
 */
@Data
public class Rule {

    private String ruleName;

    private VerifyHandler verifyHandler;

    private List<AuthHandler> authHandlers;

    public Rule() {
        this.authHandlers = new LinkedList<>();
    }

    public Rule(String ruleName, VerifyHandler verifyHandler) {
        this();
        this.ruleName = ruleName;
        this.verifyHandler = verifyHandler;
    }

    public Rule ruleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public Rule bindVerifyHandler(VerifyHandler verifyHandler) {
        this.verifyHandler = verifyHandler;
        return this;
    }

    public Rule addAuthHandler(AuthHandler authHandler) {
        this.authHandlers.add(authHandler);
        return this;
    }

    public static Rule create() {
        return new Rule();
    }

    public static Rule defaultRule(VerifyHandler verifyHandler) {
        return create().ruleName("lowin").bindVerifyHandler(verifyHandler);
    }
}
