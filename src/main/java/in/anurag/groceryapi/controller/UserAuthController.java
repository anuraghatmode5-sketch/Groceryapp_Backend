package in.anurag.groceryapi.controller;

import in.anurag.groceryapi.dto.LoginRequest;
import in.anurag.groceryapi.model.User;
import in.anurag.groceryapi.repository.UserRepository;
import in.anurag.groceryapi.service.JwtService;
import in.anurag.groceryapi.service.UserService;
import in.anurag.groceryapi.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {

        Optional<User> userOptional =
                userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", "User Not Found");
            return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", "Wrong Credentials");
            return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");

        String token = jwtService.generateToken(user.getId(), claims);

        cookieUtil.create(
                response,
                "GROCERY_AUTH",
                token,
                false,
                24 * 60 * 60
        );

        Map<String, Object> body = new HashMap<>();
        body.put("success", true);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", user.getEmail());
        userMap.put("name", user.getName());
        userMap.put("_id", user.getId());

        body.put("user", userMap);

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


    @GetMapping("/is-auth")
    public ResponseEntity<?> isAuthenticated(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", "Not authorized");

            return ResponseEntity.status(401).body(body);
        }

        // Get user ID from authentication (subject in JWT token)
        String userId = authentication.getName();
        User user = userService.findById(userId);

        if (user == null) {
            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", "User not found");

            return ResponseEntity.status(404).body(body);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("success", true);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user.getName());
        userMap.put("id", user.getId());
        userMap.put("email", user.getEmail());

        // Add cart items if they exist
        if (user.getCartItems() != null && !user.getCartItems().isEmpty()) {
            userMap.put("cartItems", user.getCartItems());
        } else {
            userMap.put("cartItems", new HashMap<>());
        }

        body.put("user", userMap);

        return ResponseEntity.ok(body);
    }


}