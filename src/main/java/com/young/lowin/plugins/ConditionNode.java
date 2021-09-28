package com.young.lowin.plugins;

import com.young.lowin.annotation.Logical;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-09-28
 * Time: 11:05
 */
public abstract class ConditionNode<R> extends Condition {

    private final R targetAuth;

    public abstract boolean method(Set<String> currentAuth, R targetAuth);

    public ConditionNode(R targetAuth, Logical logical) {
        super(logical);
        this.targetAuth = targetAuth;
    }

    public ConditionNode(R targetAuth) {
        super(Logical.AND);
        this.targetAuth = targetAuth;
    }

    @Override
    public boolean execute(Set<String> currentAuth) {
        return method(currentAuth, targetAuth);
    }

    public static <T> ConditionNode<T> conditionNode(T targetAuth, ConditionMethod<T> method) {
        return new ConditionNode<T>(targetAuth) {
            @Override
            public boolean method(Set<String> currentAuth, T targetAuth) {
                return method.conditionMethod(currentAuth, targetAuth);
            }
        };
    }

    public static <T> ConditionNode<T> conditionNode(Logical logical, T targetAuth, ConditionMethod<T> method) {
        return new ConditionNode<T>(targetAuth, logical) {
            @Override
            public boolean method(Set<String> currentAuth, T targetAuth) {
                return method.conditionMethod(currentAuth, targetAuth);
            }
        };
    }

    public interface ConditionMethod<T> {
        boolean conditionMethod(Set<String> currentAuth, T targetAuth);
    }
}
