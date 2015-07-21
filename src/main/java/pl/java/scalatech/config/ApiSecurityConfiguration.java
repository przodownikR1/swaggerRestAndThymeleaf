package pl.java.scalatech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(1)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryAuthentication = auth.inMemoryAuthentication();
        inMemoryAuthentication.withUser("slawek").password("qaz123").roles("USER");
        inMemoryAuthentication.withUser("admin").password("qaz123").roles("ADMIN");
        inMemoryAuthentication.withUser("dev").password("qaz123").roles("DEV");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**").headers().cacheControl().disable().httpBasic().and().csrf().disable().authorizeRequests().antMatchers(HttpMethod.GET)
                .hasRole("DEV").antMatchers(HttpMethod.POST).hasRole("ADMIN").antMatchers(HttpMethod.PUT).hasRole("ADMIN").antMatchers(HttpMethod.DELETE)
                .hasRole("ADMIN").anyRequest().authenticated().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
}