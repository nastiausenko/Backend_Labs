package dev.usenkonastia.backend_lab2.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.backend_lab2.service.exception.UserNotFoundException;
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

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

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
                if (userDetailsService.loadUserByUsername(username) == null) {
                    throw new UserNotFoundException(username);
                }
                if (jwtUtil.isTokenValid(jwtToken, username)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username, null, userDetailsService.loadUserByUsername(username).getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            handleException(response, HttpStatus.UNAUTHORIZED, "Expired JWT Token", ex.getMessage(), request);
        } catch (UserNotFoundException ex) {
            handleException(response, HttpStatus.NOT_FOUND, "User Not Found", ex.getMessage(), request);
        }
    }

    private void handleException(HttpServletResponse response, HttpStatus status, String title, String detail, HttpServletRequest request) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status.value());
        ObjectMapper mapper = new ObjectMapper();
        ProblemDetail problemDetail = createProblemDetail(status, title, detail, request);
        String responseBody = mapper.writeValueAsString(problemDetail);
        response.getWriter().write(responseBody);
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(URI.create(title.toLowerCase().replace(" ", "-")));
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return problemDetail;
    }
}

