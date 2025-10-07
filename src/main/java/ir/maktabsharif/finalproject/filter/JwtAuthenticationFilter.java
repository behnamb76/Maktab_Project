package ir.maktabsharif.finalproject.filter;

import ir.maktabsharif.finalproject.service.CustomUserDetailsService;
import ir.maktabsharif.finalproject.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*@Component
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final AntPathRequestMatcher loginPath = new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtService jwtService;

    protected JwtAuthenticationFilter(JwtService jwtService, AuthenticationManager authenticationManager) {
        super(loginPath, authenticationManager);
        this.jwtService = jwtService;
    }

    public Authentication authenticationMapper(HttpServletRequest httpServletRequest) throws IOException {
        LoginReqDTO loginDTO = objectMapper.readValue(httpServletRequest.getInputStream(), LoginReqDTO.class);
        return new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, ServletException, IOException {
        Authentication authentication = authenticationMapper(request);
        if (authentication == null) {
            return null;
        }
        Authentication authenticate = getAuthenticationManager().authenticate(authentication);
        if (authenticate == null) {
            throw new ServletException("Authentication Failed");
        }
        return authenticate;
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException{
        User principal = (User) authResult.getPrincipal();
        String token = jwtService.generateToken(principal.getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Invalid username or password\"}");
    }
}*/

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;


    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService, CustomUserDetailsService userDetailsService1) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService1;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
