package in.anurag.groceryapi.service;

import in.anurag.groceryapi.dto.UserRegisterRequest;
import in.anurag.groceryapi.dto.UserResponse;
import in.anurag.groceryapi.model.User;
import in.anurag.groceryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRegisterRequest request){

        userRepository.findByEmail(request.getEmail()).ifPresent(u->{
            throw new IllegalArgumentException("Email already in use");
        });

        User user =  new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCartItems(new HashMap<>());

        User saved =  userRepository.save(user);

        return new UserResponse(saved.getId(),saved.getEmail(),saved.getName());
    }

    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
