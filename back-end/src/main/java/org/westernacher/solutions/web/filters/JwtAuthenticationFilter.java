package org.westernacher.solutions.web.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.westernacher.solutions.domain.entities.User;
import org.westernacher.solutions.domain.models.binding.UserLoginBindingModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginBindingModel loginBindingModel = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginBindingModel.class);

            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginBindingModel.getUsername(),
                            loginBindingModel.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = ((User) authResult.getPrincipal());
//        String authority = user.getAuthorities()
//                .stream()
//                .findFirst()
//                .orElse(null)
//                .getAuthority();
        String authority = null;

        if(user.getAuthorities().stream().filter(o -> o.getAuthority().equals("ROOT-ADMIN")).findFirst().isPresent() == true)
        {
            authority = "ROOT-ADMIN";
        }
        else if(user.getAuthorities().stream().filter(o -> o.getAuthority().equals("ADMIN")).findFirst().isPresent() == true)
        {
            authority = "ADMIN";
        }
        else if(user.getAuthorities().stream().filter(o -> o.getAuthority().equals("MODERATOR")).findFirst().isPresent() == true)
        {
            authority = "MODERATOR";
        }
        else if(user.getAuthorities().stream().filter(o -> o.getAuthority().equals("ROLE_USER")).findFirst().isPresent() == true)
        {
            authority = "ROLE_USER";
        }

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 1200000))
                .claim("role", authority)
                .signWith(SignatureAlgorithm.HS256, "Secret".getBytes())
                .compact();

        response.getWriter()
                .append("Authorization: Bearer " + token);

        response.addHeader("Authorization", "Bearer " + token);
    }
}
