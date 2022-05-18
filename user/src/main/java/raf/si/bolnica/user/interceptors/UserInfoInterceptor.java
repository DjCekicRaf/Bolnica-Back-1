package raf.si.bolnica.user.interceptors;

import io.jsonwebtoken.*;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

public class UserInfoInterceptor implements HandlerInterceptor {

    private final LoggedInUser loggedInUser;
    private final String jwtSecret;

    public UserInfoInterceptor(LoggedInUser loggedInUser, String jwtSecret) {
        this.loggedInUser = loggedInUser;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authenticationHeader = request.getHeader("Authorization");
        if (authenticationHeader != null) {
            String content = authenticationHeader.replace("Bearer ", "");
            Jws<Claims> jws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(content);

            String[] roles = jws.getBody().get("roles", String.class).split(",");

            loggedInUser.setUsername(jws.getBody().getSubject());
            loggedInUser.setLBZ(UUID.fromString((String)jws.getBody().get("LBZ")));
            loggedInUser.setRoles(new HashSet<>(Arrays.asList(roles)));
        }

        return true;
    }
}
