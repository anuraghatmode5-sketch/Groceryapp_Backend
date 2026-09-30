package in.anurag.groceryapi.controller;

import in.anurag.groceryapi.dto.LoginRequest;
import in.anurag.groceryapi.service.JwtService;
import in.anurag.groceryapi.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerAuthController {

    @Value("${app.seller.email}")
    private String sellerEmail;

    @Value("${app.seller.password}")
    private String sellerPassword;

    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response){
        if(!sellerEmail.equals(request.getEmail()) || !sellerPassword.equals(request.getPassword())){
            Map<String,Object> body = new HashMap<>();
            body.put("success",false);
            body.put("message","Wrong Credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }

        Map<String,Object> claims = new HashMap<>();
        claims.put("role","SELLER");
        String token = jwtService.generateToken(sellerEmail,claims);

        cookieUtil.create(response, "GROCERY_AUTH", token, false, 24 * 60 * 60);

        Map<String,Object> body = new HashMap<>();
        body.put("success",true);
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("email",sellerEmail);
        userMap.put("name", "Seller");
        userMap.put("_id","seller:admin");
        body.put("user",userMap);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/is-auth")
    public ResponseEntity<?> isAuthenticated(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", "Not authorized");
            return ResponseEntity.status(401).body(body);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("success", true);

        return ResponseEntity.ok(body);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        cookieUtil.clear(response, "GROCERY_AUTH");

        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("message", "Logged Out");

        return ResponseEntity.ok(body);
    }
}
