package com.example.prozet.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.ErrorResponse;
import com.example.prozet.enum_pakage.JwtCode;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.memberAuth.domain.entity.MemberAuthEntity;
import com.example.prozet.modules.memberAuth.repository.MemberAuthRepository;
import com.example.prozet.security.auth.PrincipalDetails;
import com.example.prozet.security.auth.PrincipalDetailsService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    PrincipalDetailsService principalDetailsService;

    private JwtProvider jwtProvider;

    private MemberRepository memberRepository;

    private MemberAuthRepository memberAuthRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider,
            MemberRepository memberRepository, MemberAuthRepository memberAuthRepository,
            PrincipalDetailsService principalDetailsService) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.memberAuthRepository = memberAuthRepository;
        this.memberRepository = memberRepository;
        this.principalDetailsService = principalDetailsService;
    }

    private final String NO_CHECK_URL = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String servletPath = request.getServletPath();

        if (request.getRequestURI().equals(NO_CHECK_URL + "/google") || servletPath.equals("/h2-console")
                || request.getRequestURI().equals("/confirm-invite")) {
            chain.doFilter(request, response);
            return;

        } else {
            String refreshToken = jwtProvider.extractRefreshToken(request).orElse(null);

            if (refreshToken != null) {

                JwtCode isRefreshValid = jwtProvider.isTokenValid(refreshToken);

                if (isRefreshValid == JwtCode.ACCESS) {

                    checkRefreshTokenIsValid(response, refreshToken);
                    return;

                } else if (isRefreshValid == JwtCode.EXPIRED) {

                    jwtExceptionHandler(response, ErrorCode.EXPIRED_REFRESH_TOKEN_ERROR);

                } else {

                    jwtExceptionHandler(response, ErrorCode.INVALIDED_REFRESH_TOKEN_ERROR);
                }

            }

            checkAccessTokenAndAuthentication(request, response, chain);

        }

    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtProvider.extractAccessToken(request).orElse(null);

        if (accessToken != null) {

            JwtCode isValid = jwtProvider.isTokenValid(accessToken);

            if (isValid == JwtCode.ACCESS) {

                jwtProvider.extractUsername(accessToken).ifPresent(
                        username -> memberRepository.findByUsername(username).ifPresent(
                                this::saveAuthentication));

                filterChain.doFilter(request, response);
                return;

            } else if (isValid == JwtCode.EXPIRED) {

                jwtExceptionHandler(response, ErrorCode.EXPIRED_ACCESS_TOKEN_ERROR);

            } else {

                jwtExceptionHandler(response, ErrorCode.INVALIDED_ACCESS_TOKEN_ERROR);
            }

        } else {

            jwtExceptionHandler(response, ErrorCode.TOKEN_NULL_ERROR);
        }

    }

    private void checkRefreshTokenIsValid(HttpServletResponse response, String refreshToken)
            throws StreamWriteException, DatabindException, IOException {

        Optional<MemberAuthEntity> memberAuthEntity = memberAuthRepository.findByRefreshToken(refreshToken);

        if (memberAuthEntity.isPresent()) {

            String accssToken = jwtProvider
                    .createAccessToken(new PrincipalDetails(memberAuthEntity.get().getMemberEntity()));
            saveAuthentication(memberAuthEntity.get().getMemberEntity());
            jwtProvider.setHeaderAccessToken(response, accssToken);

        } else {

            jwtExceptionHandler(response, ErrorCode.INVALIDED_REFRESH_TOKEN_ERROR);

        }

    }

    private void saveAuthentication(MemberEntity memberEntity) {

        UserDetails userDetails = principalDetailsService.loadUserByUsername(memberEntity.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

    }

    public void jwtExceptionHandler(HttpServletResponse response, ErrorCode errorCode)
            throws StreamWriteException, DatabindException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setStatus(errorCode.getHttpStatus().value());
        ResponseEntity<?> responseEntity = ErrorResponse.toResponseEntity(errorCode);
        new ObjectMapper().writeValue(response.getWriter(), responseEntity);

    }

}
