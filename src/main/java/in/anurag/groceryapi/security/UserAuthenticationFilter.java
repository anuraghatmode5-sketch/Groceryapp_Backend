package in.anurag.groceryapi.security;

import in.anurag.groceryapi.util.CookieUtil;
import in.anurag.groceryapi.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            Cookie cookie = cookieUtil.getCookie(request, "GROCERY_AUTH");

            if (cookie != null && cookie.getValue() != null) {

                String token = cookie.getValue();

                if (jwtUtil.isTokenValid(token)) {

                    String role = jwtUtil.getRoleFromToken(token);

                    // Only allow SELLER role for seller endpoints
                    if ("USER".equals(role)) {

                        String subject = jwtUtil.getSubjectFromToken(token);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        subject,
                                        null,
                                        Collections.singletonList(
                                                new SimpleGrantedAuthority("ROLE_USER")
                                        )
                                );

                        SecurityContextHolder.getContext()
                                .setAuthentication(authentication);
                    }
                }
            }

        } catch (Exception e) {
            // Clear authentication on any error
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}

