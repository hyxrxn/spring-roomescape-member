package roomescape.controller;

import static roomescape.config.WebMvcConfiguration.SECRET_KEY;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.servlet.HandlerInterceptor;

public class CheckAdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    String role = claims.get("role", String.class);
                    if ("admin".equals(role)) {
                        return true;
                    }
                }
            }
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근할 수 없는 페이지입니다.");
        return false;
    }
}
