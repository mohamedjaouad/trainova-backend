package mohamedjaouad.TRAINOVA.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import mohamedjaouad.TRAINOVA.exceptions.ErrorsPayload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public JWTAuthFilter(JWTTools jwtTools, UserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.jwtTools = jwtTools;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            sendUnauthorized(response, "Token mancante nell'header Authorization");
            return;
        }

        String accessToken = authHeader.replace("Bearer ", "");

        try {
            jwtTools.verifyToken(accessToken);
            String userId = jwtTools.extractIdFromToken(accessToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            sendUnauthorized(response, "Token non valido, effettua nuovamente il login");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorsPayload payload = new ErrorsPayload(message, LocalDateTime.now());
        response.getWriter().write(objectMapper.writeValueAsString(payload));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher matcher = new AntPathMatcher();

        return request.getMethod().equalsIgnoreCase("OPTIONS") ||
                matcher.match("/auth/**", request.getServletPath());
    }
}
