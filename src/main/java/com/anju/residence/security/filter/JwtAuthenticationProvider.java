package com.anju.residence.security.filter;

import com.anju.residence.entity.Role;
import com.anju.residence.entity.User;
import com.anju.residence.entity.WxUser;
import com.anju.residence.enums.ResultCode;
import com.anju.residence.exception.AuthException;
import com.anju.residence.util.JwtTokenUtil;
import com.anju.residence.security.model.JwtAuthenticationToken;
import com.anju.residence.security.model.UserDetailsImpl;
import com.anju.residence.security.model.WxSession;
import com.anju.residence.service.UserService;
import com.anju.residence.service.WxUserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author cygao
 * @date 2021/2/22 12:31 上午
 **/
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final WxUserService wxUserService;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationProvider(UserService userService, WxUserService wxUserService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.wxUserService = wxUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authToken = (JwtAuthenticationToken) authentication;

        String rawToken = authToken.getJwtToken();

        log.info("Header Authorization: " + rawToken);

        String jwtToken = JwtTokenUtil.getJwtTokenByRawToken(rawToken);
        Claims claims = JwtTokenUtil.validateAndParse(jwtToken);

        // 判断是否是微信用户
        if (claims.get("open_id") != null) {
            checkAndSetWxSession(authToken, claims);
            authentication.setAuthenticated(true);
            return authentication;
        }

        String username = (String) claims.get("username");


        UserDetailsImpl userDetails = userService.loadUserByUsername(username);
        authentication = new JwtAuthenticationToken(userDetails, rawToken);
        System.out.println(authentication.getAuthorities());
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

    private void checkAndSetWxSession(JwtAuthenticationToken auth, Claims claims) {
        // TODO 验证skey(3rd_session)的一致性，对openid和sessionKey进行BCrypt加密，与数据库的进行比对
        String openId = (String) claims.get("open_id");
        String skey = (String) claims.get("skey");
        String sessionKey = wxUserService.getSessionKeyByOpenId(openId).orElseThrow(() -> new AuthException(ResultCode.WECHAT_ERROR, "该用户没有sessionKey存在"));

        if (!passwordEncoder.matches(openId + sessionKey, skey)) {
            throw new AuthException(ResultCode.WECHAT_ERROR, "无效的skey");
        }

        WxSession wxSession = WxSession.builder().openId(openId).sessionKey(sessionKey).skey(skey).build();
        auth.setWxSession(wxSession);
        User user;
        WxUser wxUser = wxUserService.getWxUserByOpenId(openId).get();
        user = wxUser.getUser();
        if (user == null)
        {
            user = new User();
            user.setUsername(openId);
            wxUser.setUser(user);
            user = userService.save(user);
        }

        ArrayList<Role> objects = new ArrayList<>();
        objects.add(new Role(3,"wx_user"));
        user.setRoles(objects);

        auth.setUserDetails(new UserDetailsImpl(user));

        auth.setAuthenticated(true);
    }
}
