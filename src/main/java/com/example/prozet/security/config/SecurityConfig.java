package com.example.prozet.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.memberAuth.repository.MemberAuthRepository;
import com.example.prozet.security.auth.PrincipalDetails;
import com.example.prozet.security.auth.PrincipalDetailsService;
import com.example.prozet.security.jwt.JwtAuthorizationFilter;
import com.example.prozet.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberAuthRepository memberAuthRepository;

    @Autowired
    private PrincipalDetailsService principalDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilter(corsConfig.corsFilter());
        httpSecurity.csrf().ignoringAntMatchers("/h2-console/**").disable();
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.httpBasic().disable().apply(new MyCustomDsl());
        httpSecurity.formLogin().disable();
        httpSecurity.authorizeRequests(authorize -> authorize
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/v1/api/member/**", "/v1/api/project/**").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN"));

        return httpSecurity.build();

    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            http.addFilter(new JwtAuthorizationFilter(authenticationManager, jwtProvider, memberRepository,
                    memberAuthRepository, principalDetailsService));
        }
    }

}
