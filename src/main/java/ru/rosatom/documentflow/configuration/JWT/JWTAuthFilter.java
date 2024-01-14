package ru.rosatom.documentflow.configuration.JWT;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.rosatom.documentflow.exceptions.TokenExpiredException;
import ru.rosatom.documentflow.services.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService userService;
    private final JWTUtil jwtUtil;
    private final RestAuthEntryPoint restAuthEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = jwtUtil.getToken(request);
        try {
            if (!Objects.isNull(authToken)) {
                String username = jwtUtil.getUsernameFromToken(authToken);
                if (!Objects.isNull(username)) {
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    } else throw new TokenExpiredException("Token expired");
                }
            }
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException ex) {
            SecurityContextHolder.clearContext();
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            restAuthEntryPoint.commence(request, response, authenticationException);
        }
    }
}