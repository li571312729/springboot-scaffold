package com.baili.auth.security.jwt;


import com.baili.auth.security.entity.SelfUserEntity;
import com.baili.auth.util.ResultUtil;
import com.baili.common.utils.JacksonUtils;
import com.baili.common.utils.Utils;
import com.baili.config.context.SpringContextUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * JWT接口请求校验拦截器
 * 请求接口时会进入这里验证Token是否合法和过期
 *
 * @author Administrator
 */
@Slf4j
public class JWTAuthenticationTokenFilter extends BasicAuthenticationFilter {

    // 不需要添加token的请求
    private static final String tokenUrl = "/user/sms/code";

    public JWTAuthenticationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String requestURI = request.getRequestURI();
        if (!hasTokenUrl(requestURI)) {
            // 获取请求头中JWT的Token(实际上是登陆时候生成的UUID)
            String tokenHeader;
            if (Utils.notEmpty(request.getHeader("baili"))) {
                tokenHeader = request.getHeader("baili");
            } else {
                tokenHeader = request.getParameter("baili");
            }

            RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.applicationContext.getBean("redisTemplate");
            String token = null;
            if (Utils.notEmpty(tokenHeader)) {
                token = (String) redisTemplate.opsForValue().get(tokenHeader);
            }

            if (null != tokenHeader) {
                Claims claims = null;
                try {
                    // 解析JWT
                    claims = Jwts.parser()
                            .setSigningKey("baili")
                            .parseClaimsJws(token)
                            .getBody();
                    Long userId = Long.valueOf(claims.getId());
                    if (null == userId) {
                        Map<String, Object> resultData = new HashMap<>(16);
                        resultData.put("code", 800);
                        resultData.put("message", "token无效");
                        ResultUtil.responseJson(response, resultData);
                    } else {
                        // 获取角色
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        String authority = claims.get("authorities").toString();
                        if (!StringUtils.isEmpty(authority)) {
                            List<Map<String, String>> authorityMap = JacksonUtils.json2pojo(authority, List.class);
                            for (Map<String, String> role : authorityMap) {
                                if (!StringUtils.isEmpty(role)) {
                                    authorities.add(new SimpleGrantedAuthority(role.get("authority")));
                                }
                            }
                        }
                        //组装参数
                        SelfUserEntity selfUserEntity = new SelfUserEntity();
                        selfUserEntity.setUsername(claims.getSubject());
                        selfUserEntity.setUserId(Long.valueOf(claims.getId()));
                        selfUserEntity.setAuthorities(authorities);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(selfUserEntity, userId, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        request.setAttribute("admin", userId);
                    }
                } catch (ExpiredJwtException e) {
                    log.info("Token过期");
                    Map<String, Object> resultData = new HashMap<>(16);
                    resultData.put("code", 800);
                    resultData.put("message", "Token过期");
                    ResultUtil.responseJson(response, resultData);
                    throw new ExpiredJwtException(null, claims, e.getMessage());
                } catch (Exception e) {
                    Map<String, Object> resultData = new HashMap<>(16);
                    resultData.put("code", 800);
                    resultData.put("message", "token无效");
                    ResultUtil.responseJson(response, resultData);
                    throw new Exception("token无效");
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean hasTokenUrl(String reqUrl) {
        List<String> list = parseParam(tokenUrl);
        for (String url : list) {
            if (reqUrl.toLowerCase().contains(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解析list配置文件
     *
     * @param param
     * @return
     */
    public static List<String> parseParam(String param) {

        List<String> list = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isBlank(param)) {
            return list;
        }

        String[] split = param.split(",");
        list = Arrays.asList(split);
        return list;
    }
}
