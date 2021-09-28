package com.young.lowin.plugins;

import com.young.lowin.annotation.Logical;

import java.util.LinkedList;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-09-28
 * Time: 11:12
 */
public class ConditionLink extends Condition implements IQueryAuth {
    private boolean precondition;

    private final LinkedList<Condition> conditionLinkedList;

    public ConditionLink(boolean precondition, Logical logical) {
        super(logical);
        this.precondition = precondition;
        conditionLinkedList = new LinkedList<>();
    }

    public ConditionLink(boolean precondition) {
        this(precondition, Logical.AND);
    }

    public ConditionLink() {
        this(true, Logical.AND);
    }

    public ConditionLink and(ConditionLink conditionLink) {
        conditionLink.setLogical(Logical.AND);
        conditionLinkedList.add(conditionLink);
        return this;
    }

    public ConditionLink or(ConditionLink conditionLink) {
        conditionLink.setLogical(Logical.OR);
        conditionLinkedList.add(conditionLink);
        return this;
    }

    public ConditionLink eq(Logical logical, Set<String> targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::eq));
        return this;
    }

    public ConditionLink eq(Set<String> targetAuth) {
        return eq(Logical.AND, targetAuth);
    }

    public ConditionLink notEq(Logical logical, Set<String> targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::notEq));
        return this;
    }

    public ConditionLink notEq(Set<String> targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(Logical.AND, targetAuth, this::notEq));
        return this;
    }

    public ConditionLink in(Logical logical, Set<String> targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(Logical.AND, targetAuth, this::in));
        return this;
    }

    public ConditionLink in(Set<String> targetAuth) {
        return in(Logical.AND, targetAuth);
    }

    public ConditionLink notIn(Logical logical, Set<String> targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::notIn));
        return this;
    }

    public ConditionLink notIn(Set<String> targetAuth) {
        return notIn(Logical.AND, targetAuth);
    }

    public ConditionLink likeLeft(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::likeLeft));
        return this;
    }

    public ConditionLink likeLeft(String targetAuth) {
        return likeLeft(Logical.AND, targetAuth);
    }

    public ConditionLink notLikeLeft(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::notLikeLeft));
        return this;
    }

    public ConditionLink notLikeLeft(String targetAuth) {
        return notLikeLeft(Logical.AND, targetAuth);
    }

    public ConditionLink anyLikeLeft(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::anyLikeLeft));
        return this;
    }

    public ConditionLink anyLikeLeft(String targetAuth) {
        return anyLikeLeft(Logical.AND, targetAuth);
    }

    public ConditionLink notAnyLikeLeft(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::notAnyLikeLeft));
        return this;
    }

    public ConditionLink notAnyLikeLeft(String targetAuth) {
        return notAnyLikeLeft(Logical.AND, targetAuth);
    }

    public ConditionLink likeRight(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::likeRight));
        return this;
    }

    public ConditionLink likeRight(String targetAuth) {
        return likeRight(Logical.AND, targetAuth);
    }

    public ConditionLink notLikeRight(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::notLikeRight));
        return this;
    }

    public ConditionLink notLikeRight(String targetAuth) {
        return notLikeRight(Logical.AND, targetAuth);
    }

    public ConditionLink anyLikeRight(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::anyLikeRight));
        return this;
    }

    public ConditionLink anyLikeRight(String targetAuth) {
        return anyLikeRight(Logical.AND, targetAuth);
    }

    public ConditionLink notAnyLikeRight(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::notAnyLikeRight));
        return this;
    }

    public ConditionLink notAnyLikeRight(String targetAuth) {
        return notAnyLikeRight(Logical.AND, targetAuth);
    }

    public ConditionLink like(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::like));
        return this;
    }

    public ConditionLink like(String targetAuth) {
        return like(Logical.AND, targetAuth);
    }

    public ConditionLink notLike(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::notLike));
        return this;
    }

    public ConditionLink notLike(String targetAuth) {
        return notLike(Logical.AND, targetAuth);
    }

    public ConditionLink anyLike(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::anyLike));
        return this;
    }

    public ConditionLink anyLike(String targetAuth) {
        return anyLike(Logical.AND, targetAuth);
    }

    public ConditionLink notAnyLike(Logical logical, String targetAuth) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, this::notAnyLike));
        return this;
    }

    public ConditionLink notAnyLike(String targetAuth) {
        return notAnyLike(Logical.AND, targetAuth);
    }

    public <R> ConditionLink judge(Logical logical, R targetAuth, ConditionNode.ConditionMethod<R> method) {
        conditionLinkedList.add(ConditionNode.conditionNode(logical, targetAuth, method));
        return this;
    }

    public <R> ConditionLink judge(R targetAuth, ConditionNode.ConditionMethod<R> method) {
        return judge(Logical.AND, targetAuth, method);
    }

    @Override
    public boolean execute(Set<String> currentAuth) {
        for (Condition condition : conditionLinkedList) {
            if (condition.getLogical().equals(Logical.AND)) {
                precondition = precondition && condition.execute(currentAuth);
            }
            if (condition.getLogical().equals(Logical.OR)) {
                precondition = precondition || condition.execute(currentAuth);
            }
        }
        return precondition;
    }
}
