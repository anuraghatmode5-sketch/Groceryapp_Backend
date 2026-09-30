package in.anurag.groceryapi.controller;

import in.anurag.groceryapi.dto.UserRegisterRequest;
import in.anurag.groceryapi.dto.UserResponse;
import in.anurag.groceryapi.model.User;
import in.anurag.groceryapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse user = userService.register(request);
        Map<String, Object> body = new HashMap<>();
        body.put("success",true);

        Map<String, Object> data = new HashMap<>();
        data.put("id",user.getId());
        data.put("email",user.getEmail());
        data.put("name",user.getName());
        body.put("user",data);

        return ResponseEntity.ok(body);

    }

}
