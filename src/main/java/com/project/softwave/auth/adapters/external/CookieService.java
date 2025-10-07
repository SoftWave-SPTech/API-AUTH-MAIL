package com.project.softwave.auth.adapters.external;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    public void addJwtCookie(HttpServletResponse response, String jwtToken) {
        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); // Use HTTPS in production
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3600); // Expiration time in seconds
        response.addCookie(jwtCookie);
    }

    public void clearJwtCookie(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); // Use HTTPS in production
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // Delete the cookie
        response.addCookie(jwtCookie);
    }
}