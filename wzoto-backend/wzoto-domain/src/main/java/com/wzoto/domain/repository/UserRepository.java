package com.wzoto.domain.repository;

import com.wzoto.domain.entity.User;

/**
 * 用户仓储接口 - 领域层定义，基础设施层实现
 */
public interface UserRepository {

    /**
     * 根据openid查询用户
     */
    User findByOpenid(String openid);

    /**
     * 根据ID查询用户
     */
    User findById(Long id);

    /**
     * 保存用户（新增或更新）
     */
    User save(User user);

    /**
     * 更新用户身份
     */
    boolean updateIdentity(Long userId, String identityType);

    /**
     * 更新手机号
     */
    boolean updatePhone(Long userId, String phone);

    /**
     * 根据昵称查找用户（开发环境用）
     */
    User findByNickname(String nickname);
}