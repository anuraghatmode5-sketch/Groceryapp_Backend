package in.anurag.groceryapi.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public void create(HttpServletResponse httpServletResponse, String name, String value, Boolean secure, Integer maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        if(maxAge != null) {
            cookie.setMaxAge(maxAge);
        }
        cookie.setAttribute("SameSite", "None");
        httpServletResponse.addCookie(cookie);
    }

    public void clear(HttpServletResponse httpServletResponse, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }

    public Cookie getCookie(HttpServletRequest request, String name) {

        if(request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }
}
