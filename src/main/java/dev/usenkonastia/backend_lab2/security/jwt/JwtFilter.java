package dev.usenkonastia.backend_lab2.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.backend_lab2.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");

            String username = null;
            String jwtToken = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.substring(7);
                username = jwtUtil.extractClaims(jwtToken).getSubject();
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.isTokenValid(jwtToken, username)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username, null, userDetailsService.loadUserByUsername(username).getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            response.setContentType("application/json");
            response.setStatus(UNAUTHORIZED.value());
            ObjectMapper mapper = new ObjectMapper();
            String exMessage = mapper.writeValueAsString(createProblemDetail(ex, request));
            response.getWriter().write(exMessage);
        }
    }

    private ProblemDetail createProblemDetail(ExpiredJwtException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "The token has expired.");
        problemDetail.setType(URI.create("token-expired"));
        problemDetail.setTitle("Expired JWT Token");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return problemDetail;
    }
}
