package pl.java.scalatech.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Service;

import pl.java.scalatech.annotation.SecurityComponent;

@Configuration
@EnableWebSecurity
@Slf4j
@ComponentScan(basePackages = { "pl.java.scalatech.security" }, useDefaultFilters = false, includeFilters = { @Filter(Service.class),
        @Filter(SecurityComponent.class) })
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final int MAX_SESSIONS = 3;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryAuthentication = auth.inMemoryAuthentication();
        inMemoryAuthentication.withUser("slawek").password("qaz123").roles("USER");
        inMemoryAuthentication.withUser("admin").password("qaz123").roles("ADMIN");
        inMemoryAuthentication.withUser("dev").password("qaz123").roles("DEV");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**").antMatchers("/css/**").antMatchers("/js/**").antMatchers("/images/**").antMatchers("/favicon.ico");
    }

    @Configuration
    @Order(3)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api").antMatcher("api/**");
            http.csrf().disable();
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.authorizeRequests().anyRequest().hasRole("DEV").and().httpBasic();
        }
    }

    @Order(2)
    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/welcome", "/info", "/api/ping", "/signup", "/about", "/register").permitAll().antMatchers("/admin")
                    .access("hasRole('ROLE_ADMIN')").antMatchers("/hello").access("hasRole('ROLE_USER')").anyRequest().authenticated();

            login(http);
            logout(http);
        }
    }

    private static void login(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").failureUrl("/login?error=true").defaultSuccessUrl("/welcome").permitAll().and().sessionManagement()
                .sessionFixation().newSession().maximumSessions(MAX_SESSIONS).maxSessionsPreventsLogin(true);
        /*
         * failureHandler((request, response, authentication) -> {
         * response.setStatus(HttpStatus.UNAUTHORIZED.value());
         * log.info("+++ unauthorized ...user : {} , {}", request.getUserPrincipal(), authentication.getMessage());
         * })
         */
    }

    private static void logout(HttpSecurity http) throws Exception {
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/welcome").addLogoutHandler((request, response, authentication) -> {
            log.info("+++ success logout : user :  {} : message ", request.getUserPrincipal(), authentication.isAuthenticated());
        }).invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll();
    }
}