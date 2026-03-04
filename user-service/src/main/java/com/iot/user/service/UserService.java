package com.iot.user.service;

import com.iot.common.pojo.entity.SysUser;
import com.iot.user.config.JwtConfig;
import com.iot.user.exception.BusinessException;
import com.iot.user.pojo.req.LoginReq;
import com.iot.user.pojo.req.UserInfoReq;
import com.iot.user.pojo.resp.LoginResp;
import com.iot.user.pojo.resp.UserInfoResp;
import com.iot.user.repository.SysUserRepository;
import com.iot.user.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Kai
 * @Description:
 * @Date: 2026/02/16 02:51
 * @Version: 1.0
 */

@Service
public class UserService {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JwtConfig jwtConfig;

    public LoginResp login(LoginReq req) {
        if (req.getPhone().isEmpty()) {
            throw new BusinessException("手机号不能为空");
        }
        SysUser user = userRepository.findByPhone(req.getPhone()).orElse(null);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        boolean isFirstLogin = !Boolean.TRUE.equals(redisTemplate.hasKey("login:" + user.getId()));
        LoginResp resp = new LoginResp();
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setPhone(user.getPhone());
        resp.setToken(this.fillToken(user.getId(), isFirstLogin));
        return resp;
    }

    public UserInfoResp getUserInfo(UserInfoReq req) {
        if (req.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        SysUser user = userRepository.findById(req.getUserId()).orElse(null);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UserInfoResp resp = new UserInfoResp();
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setPhone(user.getPhone());
        resp.setStatus(user.getStatus());
        return resp;
    }

    public SysUser clientUserInfo(UserInfoReq req) {
        return userRepository.findById(req.getUserId()).orElse(null);
    }

    private String fillToken(Long userId, boolean isFirstLogin) {
        if (!isFirstLogin) {
            redisTemplate.delete("login:" + userId);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        String token = JwtUtils.createJWT(jwtConfig.getUserSecretKey(), jwtConfig.getTtl(), claims);
        redisTemplate.opsForValue().set("login:" + userId, token, 30, TimeUnit.DAYS);
        return token;
    }
}
