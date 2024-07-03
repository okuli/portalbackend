package com.supportportal.impersonate.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportportal.domain.UserPrincipal;
import com.supportportal.utility.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImpersonateAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException {
        appendTokenForImpersonatePrincipal(response, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        appendTokenForImpersonatePrincipal(httpServletResponse, authentication);
    }

    private void appendTokenForImpersonatePrincipal(HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        log.trace("Impersonating user {}...", authentication.getPrincipal());
        SwitchUserGrantedAuthority grantedAuthority = authentication.getAuthorities()
                .stream()
                .filter(authority -> authority.getClass().isAssignableFrom(SwitchUserGrantedAuthority.class))
                .findFirst()
                .map(SwitchUserGrantedAuthority.class::cast)
                .orElseThrow(() -> new IllegalStateException("No switch authority found."));

        String prevPrincipalUsername;
        Object prevPrincipal = grantedAuthority.getSource().getPrincipal();
        if (prevPrincipal.getClass().isAssignableFrom(String.class)) {
            prevPrincipalUsername = (String) prevPrincipal;
        } else if (prevPrincipal.getClass().isAssignableFrom(UserPrincipal.class)) {
            prevPrincipalUsername = ((UserPrincipal) prevPrincipal).getUsername();
        } else {
            throw new IllegalStateException("Could not extract principal for object " + prevPrincipal);
        }

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("jwtToken", jwtTokenProvider.generateJwtToken(principal, Optional.of(prevPrincipalUsername)));
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(httpServletResponse.getWriter(), tokenMap);
    }
}
