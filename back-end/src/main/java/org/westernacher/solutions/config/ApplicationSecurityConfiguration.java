package org.westernacher.solutions.config;

import org.westernacher.solutions.service.UserService;
import org.westernacher.solutions.web.filters.JwtAuthenticationFilter;
import org.westernacher.solutions.web.filters.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ApplicationSecurityConfiguration(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
//                .antMatchers("/*", "/","/cart", "/cart/*", "/save-order", "/users/register").permitAll()
//                .antMatchers("/sweatshirts/change-rating", "/sweatshirts/add").permitAll()
//                .antMatchers("/orders/add", "/orders/cart").permitAll()
//                .antMatchers("/orders/waiting", "/orders/delivered", "/orders/deliver").permitAll()  .antMatchers("/orders/remove", "/orders/remove-selected", "orders/remove-delivered-flag").hasAuthority("ADMIN")
//                .antMatchers("/logs/all").hasAuthority("MODERATOR")
//                .antMatchers("/sweatshirts/add").hasAuthority("MODERATOR")
//                .antMatchers("/users/all").hasAuthority("MODERATOR")
//                .antMatchers("/users/delete").hasAuthority("ADMIN")
//                .antMatchers("/users/edit").hasAuthority("ADMIN")
//                .antMatchers("/logs/delete", "/logs/delete/all").hasAuthority("ADMIN")
//                .antMatchers("/organization", "/organization/promote", "/organization/demote").hasAuthority("ADMIN")
                .antMatchers("/**", "/get-card").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.userService))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**"
                , corsConfiguration);
        return source;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userService)
                .passwordEncoder(this.bCryptPasswordEncoder);
    }
}
