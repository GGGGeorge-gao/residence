package com.anju.residence.util;

import cn.hutool.core.util.IdUtil;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.AuthException;
import com.anju.residence.params.JwtParams;
import com.anju.residence.security.model.JwtAuthenticationToken;
import com.anju.residence.security.model.UserDetailsImpl;
import com.anju.residence.security.model.WxSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cygao
 * @date 2021/1/2 16:53
 **/
@Slf4j
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final Clock CLOCK = DefaultClock.INSTANCE;

    /**
     * 生成token（包含Bearer前缀）
     * payload 由四部分组成：userId、username、openId、skey
     * 为了防止session_key被泄露，使用加密后的skey
     *
     * @param username audience
     * @param userId   subject
     * @return token字符串
     */
    public static String generateToken(int userId, String username, String openId, String skey, String unionId) {
        final Date createdDate = CLOCK.now();
        final Date expirationDate = new Date(System.currentTimeMillis() + JwtParams.EXPIRATION);

        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("username", username);

        if (openId != null) {
            payload.put("open_id", openId);
            payload.put("skey", skey);
            payload.put("union_id", unionId);
        }

        return JwtParams.TOKEN_START_WITH +
                Jwts.builder()
                        .setId(IdUtil.randomUUID())
                        .setClaims(payload)
                        .setIssuedAt(createdDate)
                        .setExpiration(expirationDate)
                        .signWith(SignatureAlgorithm.HS512, JwtParams.SECRET)
                        .compact();
    }

    /**
     * 生成token（包含Bearer前缀）
     * payload 由两部分组成：openId、skey
     * 为了防止session_key被泄露，使用加密后的skey
     *
     * @param openId
     * @param skey
     * @return token字符串
     */
    public static String generateToken(String openId, String skey) {
        final Date createdDate = CLOCK.now();
        final Date expirationDate = new Date(System.currentTimeMillis() + JwtParams.EXPIRATION);

        Map<String, Object> payload = new HashMap<>();

        payload.put("open_id", openId);
        payload.put("skey", skey);

        return JwtParams.TOKEN_START_WITH +
                Jwts.builder()
                        .setId(IdUtil.randomUUID())
                        .setClaims(payload)
                        .setIssuedAt(createdDate)
                        .setExpiration(expirationDate)
                        .signWith(SignatureAlgorithm.HS512, JwtParams.SECRET)
                        .compact();
    }

    public static String generateToken(UserDetailsImpl userDetails, WxSession wxSession) {
        return generateToken(userDetails.getUserId(), userDetails.getUsername(), wxSession.getOpenId(), wxSession.getSkey(), wxSession.getUnionId());
    }

    public static String generateToken(UserDetailsImpl userDetails) {
        return generateToken(userDetails.getUserId(), userDetails.getUsername(), null, null, null);
    }

    public static String generateToken(WxSession wxSession) {
        return generateToken(wxSession.getOpenId(), wxSession.getSkey());
    }

    public static String getJwtTokenByRawToken(String rawToken) throws AuthException {
        if (!rawToken.startsWith(JwtParams.TOKEN_START_WITH)) {
            log.error("无效的token开头: {}", rawToken);
            throw new AuthException(ResultCode.TOKEN_ERROR, "token的前缀应为" + JwtParams.TOKEN_START_WITH + " ");
        }
        return rawToken.substring(JwtParams.TOKEN_START_WITH.length());
    }

    public static Claims validateAndParse(String jwtToken) throws AuthException {

        Claims claims;

        try {
            claims = Jwts.parser().setSigningKey(JwtParams.SECRET).parseClaimsJws(jwtToken).getBody();
        } catch (MalformedJwtException e) {
            // token格式不正确
            throw new AuthException(ResultCode.TOKEN_ERROR, "错误的token格式");
        } catch (SignatureException e) {
            // token签名错误
            throw new AuthException(ResultCode.TOKEN_ERROR, "无效的JWT签名");
        } catch (ExpiredJwtException e) {
            // token已过期
            throw new AuthException(ResultCode.TOKEN_ERROR, "token已过期");
        } catch (Exception e) {
            // 未知异常
            throw new AuthException(ResultCode.UNKNOWN_ERROR, e.getMessage());
        }

        return claims;
    }

    /**
     * 检验当前用户的Authentication与需要操作的用户id是否一致以保证安全性
     *
     * @param userId 用户id
     * @return 是否一致
     */
    public static boolean checkUserAuthentication(int userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken) {
            log.info(((UserDetailsImpl) auth.getPrincipal()).getUserId().toString());
            return auth.getPrincipal() != null && ((UserDetailsImpl) auth.getPrincipal()).getUserId().equals(userId);
        }

        return false;
    }

}
