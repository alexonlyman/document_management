package home_group.doc_service.configuration;

import home_group.doc_service.filer.JwtFiler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecuriryConfig {
    private static final String[] WHITE_LIST = {
            "/registration",
            "/login"
    };

    private final JwtFiler filer;
    private final AuthenticationProvider provider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorization ->
                        authorization.requestMatchers(WHITE_LIST)
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .formLogin((login -> login.loginPage("/login")))
                .logout(LogoutConfigurer::permitAll)
                .cors(withDefaults())
                .authenticationProvider(provider)
                .addFilterBefore(filer, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults());
        return http.build();

    }

}
