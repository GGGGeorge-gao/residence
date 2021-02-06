package com.anju.residence.security.jwt;

import cn.hutool.core.util.StrUtil;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.AuthException;
import com.anju.residence.security.model.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author cygao
 * @date 2021/1/2 16:53
 **/
@Slf4j
@Component
public class JwtTokenUtil implements Serializable {

  private static final long serialVersionUID = -3301605591108950415L;

  private static final Clock CLOCK = DefaultClock.INSTANCE;

  public static String generateToken(UserDetailsImpl userDetails) {
    return generateToken(userDetails.getUsername(), String.valueOf(userDetails.getUserId()));
  }

  /**
   * 生成token（包含Bearer前缀）
   * @param username audience
   * @param userId subject
   * @return token字符串
   */
  public static String generateToken(String username, String userId) {
    final Date createdDate = CLOCK.now();
    final Date expirationDate = new Date(System.currentTimeMillis() + JwtProperty.EXPIRATION);

    log.info("subject: {}", userId);

    return JwtProperty.TOKEN_START_WITH +
            Jwts.builder()
                    .setId(UUID.randomUUID().toString())
                    .setAudience(username)
                    .setSubject(userId)
                    .setIssuedAt(createdDate)
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS512, JwtProperty.SECRET)
                    .compact();
  }

  public static String getJwtTokenByRawToken(String rawToken) throws AuthException {
    if (!rawToken.startsWith(JwtProperty.TOKEN_START_WITH)) {
      log.error("无效的token开头: {}", rawToken);
      throw new AuthException(ResultCode.INVALID_TOKEN_START_WITH);
    }
    return rawToken.substring(JwtProperty.TOKEN_START_WITH.length());
  }

  public static Claims validateAndParse(String jwtToken) throws AuthException {

    Claims claims;

    try {
      claims = Jwts.parser().setSigningKey(JwtProperty.SECRET).parseClaimsJws(jwtToken).getBody();
    } catch (MalformedJwtException e) {
      // token格式不正确
      throw new AuthException(ResultCode.INVALID_TOKEN_FORMAT, "Invalid token format");
    } catch (SignatureException e) {
      // token签名错误
      throw new AuthException(ResultCode.INVALID_TOKEN_SIGNATURE, "Invalid token signature");
    } catch (ExpiredJwtException e) {
      // token已过期
      throw new AuthException(ResultCode.EXPIRED_TOKEN, "The token is expired, please login again");
    } catch (Exception e) {
      // 未知异常
      throw new AuthException(ResultCode.UNKNOWN_ERROR, e.getMessage());
    }

    // 判断token是否过期
//    if (claims.getIssuedAt().before(new Date())) {
//      throw new JwtException("Expired token");
//    }

    return claims;
  }


//  public boolean validateToken(String token, UserDetails userDetails) {
//    final String username = getUsername(token);
//    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//  }

  public String getUsername(String token) {
    final Claims claims = getClaims(token);
    return claims.getSubject();
  }

  public Claims getClaims(String token) throws SignatureException {
    return Jwts.parser()
            .setSigningKey(JwtProperty.SECRET)
            .parseClaimsJws(token)
            .getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpiration(token);
    return expiration.before(CLOCK.now());
  }

  public Date getExpiration(String token) {
    final Claims claims = getClaims(token);
    return claims.getExpiration();
  }

  public String getUsername(Claims claims) {
    return claims.getSubject();
  }

  public String getUserId(Claims claims) {
    return claims.getAudience();
  }

  public boolean isExpired(Claims claims) {
    return getExpiration(claims).before(CLOCK.now());
  }

  public Date getExpiration(Claims claims) {
    return claims.getExpiration();
  }

  public Date getIssuedAt(Claims claims) {
    return claims.getIssuedAt();
  }

  public boolean validateToken(Claims claims, UserDetailsImpl userDetails) {
    return !isExpired(claims) && StrUtil.equals(getUserId(claims), String.valueOf(userDetails.getUserId()));
  }

}
