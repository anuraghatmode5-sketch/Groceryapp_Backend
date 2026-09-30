package in.anurag.groceryapi.config;

import in.anurag.groceryapi.security.SellerAuthenticationFilter;
import in.anurag.groceryapi.security.UserAuthenticationFilter;
import in.anurag.groceryapi.util.CookieUtil;
import in.anurag.groceryapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    private final UserAuthenticationFilter userAuthenticationFilter;
    private final SellerAuthenticationFilter sellerAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:3000",
                        "http://localhost:5173",
                        "https://groceryapp-frontend.netlify.app"
                )
        );

        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/login").permitAll()
                        .requestMatchers("/api/seller/login").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/product/list").permitAll()
                        .requestMatchers("/api/product/id").permitAll()
                        .requestMatchers("/api/user/is-auth").hasRole("USER")
                        .requestMatchers("/api/address/add").hasRole("USER")
                        .requestMatchers("/api/address/get").hasRole("USER")
                        .requestMatchers("/api/cart/update").hasRole("USER")
                        .requestMatchers("/api/order/cod").hasRole("USER")
                        .requestMatchers("/api/order/user/**").hasRole("USER")
                        .requestMatchers("/api/order/verify-payment").hasRole("USER")

                        .requestMatchers("/api/seller/is-auth").hasRole("SELLER")
                        .requestMatchers("/api/product/add").hasRole("SELLER")
                        .requestMatchers("/api/product/stock").hasRole("SELLER")
                        .requestMatchers("/api/product/orders").hasRole("SELLER")
                        .requestMatchers("/api/order/all").hasRole("SELLER")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        userAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .addFilterBefore(
                        sellerAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }
}