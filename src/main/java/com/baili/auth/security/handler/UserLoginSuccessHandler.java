package com.baili.auth.security.handler;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baili.admin.entity.SysUser;
import com.baili.admin.service.SysUserService;
import com.baili.auth.security.entity.SelfUserEntity;
import com.baili.auth.util.JWTTokenUtil;
import com.baili.auth.util.ResultUtil;
import com.baili.config.redis.RedisCache;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  登录成功处理类
 */
@Slf4j
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    RedisCache redisCache;
    @Autowired
    private SysUserService sysUserService;
    /**
     * 登录成功返回结果
     */
    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        // 组装JWT
        SelfUserEntity selfUserEntity =  (SelfUserEntity) authentication.getPrincipal();
        SysUser one = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, selfUserEntity.getUserId())
                .eq(SysUser::getStatus, 1).last("limit 1"));
        String token = JWTTokenUtil.createAccessToken(selfUserEntity);
        String uuid = IdUtil.simpleUUID();
        redisCache.setCacheObject(uuid, token);
        // 封装返回参数
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("code",200);
        resultData.put("message", "登录成功");
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        map.put("token", uuid);
        map.put("phone", one.getMobile());
        map.put("name",one.getName());
        resultData.put("data",map);
        ResultUtil.responseJson(response,resultData);
    }
}