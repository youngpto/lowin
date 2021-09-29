package com.young.lowin.plugins;

import com.young.lowin.annotation.Logical;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-09-28
 * Time: 11:03
 */
public abstract class Condition {

    public Condition(Logical logical) {
        this.logical = logical;
    }

    private Logical logical;

    public Logical getLogical() {
        return logical;
    }

    private void setLogical(Logical logical) {
        this.logical = logical;
    }

    public Condition logical(Logical logical) {
        setLogical(logical);
        return this;
    }

    public abstract boolean execute(Set<String> currentAuth);
}
