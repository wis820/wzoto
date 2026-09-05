package com.wzoto.infrastructure.interceptor;

import com.wzoto.infrastructure.config.JwtProperties;
import com.wzoto.infrastructure.util.JwtUtil;
import com.wzoto.domain.context.UserContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT登录态拦截器
 * 从Header中提取Token → 解析 → 注入UserContext
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // OPTIONS预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader(jwtProperties.getHeaderName());
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(jwtProperties.getTokenPrefix())) {
            log.warn("未携带有效的JWT Token, uri={}", request.getRequestURI());
            response.setStatus(401);
            return false;
        }

        String token = authHeader.substring(jwtProperties.getTokenPrefix().length());
        if (!jwtUtil.validateToken(token)) {
            log.warn("JWT Token验证失败, uri={}", request.getRequestURI());
            response.setStatus(401);
            return false;
        }

        Claims claims = jwtUtil.parseToken(token);
        Long userId = claims.get("userId", Long.class);
        String openid = claims.get("openid", String.class);
        String identityType = claims.get("identityType", String.class);

        // 注入上下文
        UserContext context = new UserContext(userId, openid, identityType);
        UserContext.set(context);

        log.debug("JWT认证通过, userId={}, identityType={}", userId, identityType);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}