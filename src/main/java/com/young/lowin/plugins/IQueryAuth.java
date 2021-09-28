package com.young.lowin.plugins;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.young.lowin.model.AuthModel;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: young
 * Date: 2021-09-27
 * Time: 15:37
 */
public interface IQueryAuth {

    /**
     * 两者互为子集即视为完全一致
     *
     * @param currentAuth
     * @param targetAuth
     * @return
     */
    default boolean eq(Set<String> currentAuth, Set<String> targetAuth) {
        return CollUtil.containsAll(currentAuth, targetAuth) && CollUtil.containsAll(targetAuth, currentAuth);
    }

    /**
     * 两者不相等即不完全一致
     *
     * @param currentAuth
     * @param targetAuth
     * @return
     */
    default boolean notEq(Set<String> currentAuth, Set<String> targetAuth) {
        return !eq(currentAuth, targetAuth);
    }

    /**
     * 两者包含一个或多个共同元素即部分一致
     *
     * @param currentAuth
     * @param targetAuth
     * @return
     */
    default boolean in(Set<String> currentAuth, Set<String> targetAuth) {
        return CollUtil.containsAny(currentAuth, targetAuth);
    }

    /**
     * 两者没有共同元素即完全不一致
     *
     * @param currentAuth
     * @param targetAuth
     * @return
     */
    default boolean notIn(Set<String> currentAuth, Set<String> targetAuth) {
        return !in(currentAuth, targetAuth);
    }

    default boolean like(Set<String> currentAuth, String targetAuth) {
        for (String s : currentAuth) {
            if (!s.contains(targetAuth)) {
                return false;
            }
        }
        return true;
    }

    default boolean notLike(Set<String> currentAuth, String targetAuth) {
        return !like(currentAuth, targetAuth);
    }

    default boolean anyLike(Set<String> currentAuth, String targetAuth) {
        for (String s : currentAuth) {
            if (s.contains(targetAuth)) {
                return true;
            }
        }
        return false;
    }

    default boolean notAnyLike(Set<String> currentAuth, String targetAuth) {
        return !anyLike(currentAuth, targetAuth);
    }

    default boolean likeLeft(Set<String> currentAuth, String targetAuth) {
        for (String s : currentAuth) {
            if (!s.startsWith(targetAuth)) {
                return false;
            }
        }
        return true;
    }

    default boolean notLikeLeft(Set<String> currentAuth, String targetAuth) {
        return !likeLeft(currentAuth, targetAuth);
    }

    default boolean anyLikeLeft(Set<String> currentAuth, String targetAuth) {
        for (String s : currentAuth) {
            if (s.startsWith(targetAuth)) {
                return true;
            }
        }
        return false;
    }

    default boolean notAnyLikeLeft(Set<String> currentAuth, String targetAuth) {
        return !anyLikeLeft(currentAuth, targetAuth);
    }

    default boolean likeRight(Set<String> currentAuth, String targetAuth) {
        for (String s : currentAuth) {
            if (!s.endsWith(targetAuth)) {
                return false;
            }
        }
        return true;
    }

    default boolean notLikeRight(Set<String> currentAuth, String targetAuth) {
        return !likeRight(currentAuth, targetAuth);
    }

    default boolean anyLikeRight(Set<String> currentAuth, String targetAuth) {
        for (String s : currentAuth) {
            if (s.endsWith(targetAuth)) {
                return true;
            }
        }
        return false;
    }

    default boolean notAnyLikeRight(Set<String> currentAuth, String targetAuth) {
        return !anyLikeRight(currentAuth, targetAuth);
    }
}
