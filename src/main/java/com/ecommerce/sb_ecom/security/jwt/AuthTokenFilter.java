package com.ecommerce.sb_ecom.security.jwt;

import com.ecommerce.sb_ecom.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        System.out.println(request.getServletPath());
        return
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        String path = request.getServletPath();
        System.out.println("path = "+ path);
        try{
            //String jwt = jwtUtils.getJwtFromCookies(request);
            String jwt = resolveToken(request);
            System.out.println("jwt = " + jwt);
            if(jwt != null && jwtUtils.validateJwtToken(jwt)){
                String username = jwtUtils.getUserNameFromJwtTokens(jwt);
                System.out.println("username = " + username);

                //if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                  if(username != null ) {
                        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("Authentication set");
                        logger.debug("Roles from JWT: {}", userDetails.getAuthorities());
                    }
            }

        } catch (Exception e){
            logger.error("jwt authentication failed", e);
        }
        filterChain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request)
    {
        // 1. Check Authorization header
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        System.out.println("jwtUtils.getJwtFromCookies(request) = "+ jwtUtils.getJwtFromCookies(request));
        // 2. Fallback to cookie
        return jwtUtils.getJwtFromCookies(request);
    }
}
