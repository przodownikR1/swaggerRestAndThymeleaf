package pl.java.scalatech.config;

import static org.ajar.swaggermvcui.SwaggerSpringMvcUi.WEB_JAR_RESOURCE_LOCATION;
import static org.ajar.swaggermvcui.SwaggerSpringMvcUi.WEB_JAR_RESOURCE_PATTERNS;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.GzipResourceResolver;

// @Configuration
// @EnableWebMvc
@Slf4j
@ComponentScan(basePackages = { "pl.java.scalatech.web" }, useDefaultFilters = false, includeFilters = { @Filter(RestController.class),
        @Filter(Controller.class), @Filter(Component.class) })
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(new SwaggerInterceptor());

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);

        registry.addResourceHandler("/favicon.ico").addResourceLocations("/images/favicon.png");
        registry.addResourceHandler("/favicon2.ico").addResourceLocations("/images/favicon.ico");

        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926).resourceChain(true)
                .addResolver(new GzipResourceResolver());

        registry.addResourceHandler("/images/**").addResourceLocations("/images/").setCachePeriod(31556926);

        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926).resourceChain(true).addResolver(new GzipResourceResolver());

        registry.addResourceHandler(WEB_JAR_RESOURCE_PATTERNS).addResourceLocations(WEB_JAR_RESOURCE_LOCATION).setCachePeriod(0);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/sdoc.jsp");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
}
