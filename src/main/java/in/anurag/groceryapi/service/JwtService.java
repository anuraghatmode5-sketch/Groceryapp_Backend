package in.anurag.groceryapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import in.anurag.groceryapi.util.JwtUtil;


import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;

    public String generateToken(String subject, Map<String, Object> claims) {
        return jwtUtil.generateToken(subject,claims);
    }

    public boolean validateToken(String token) {
        return jwtUtil.isTokenValid(token);
    }
}
