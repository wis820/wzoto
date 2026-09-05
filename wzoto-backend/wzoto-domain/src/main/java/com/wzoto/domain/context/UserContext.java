package com.wzoto.domain.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前登录用户上下文 - 存储在ThreadLocal中
 * 放在domain层供infrastructure和interfaces层共同使用
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {

    /** 用户ID */
    private Long userId;

    /** 微信openid */
    private String openid;

    /** 身份类型：PARENT / STUDENT */
    private String identityType;

    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();

    public static void set(UserContext context) {
        CONTEXT.set(context);
    }

    public static UserContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static Long getCurrentUserId() {
        UserContext ctx = get();
        return ctx != null ? ctx.getUserId() : null;
    }

    public static String getCurrentIdentityType() {
        UserContext ctx = get();
        return ctx != null ? ctx.getIdentityType() : null;
    }

    public static boolean isParent() {
        return "PARENT".equals(getCurrentIdentityType());
    }

    public static boolean isStudent() {
        return "STUDENT".equals(getCurrentIdentityType());
    }
}