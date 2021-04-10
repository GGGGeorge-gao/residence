package com.anju.residence.util;

import com.anju.residence.security.model.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author cygao
 * @date 2021/4/10
 **/
public class SecurityUtil {

    public static int getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken)) {
            return 0;
        }
        return ((JwtAuthenticationToken) auth).getUserDetails().getUserId();
    }
}
