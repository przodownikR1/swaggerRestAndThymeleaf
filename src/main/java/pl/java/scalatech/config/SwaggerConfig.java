package pl.java.scalatech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@Configuration
@EnableSwagger
@PropertySource(value = "classpath:swagger.properties", ignoreResourceNotFound = false)
public class SwaggerConfig {
    private SpringSwaggerConfig springSwaggerConfig;
    @Value("${swagger.title}:project swagger")
    private static String title;
    @Value("${swagger.description}:project desc")
    private static String description;
    @Value("${swagger.termsOfServiceUrl}:terms of service url ")
    private static String termsOfServiceUrl;
    @Value("${swagger.contact}:contact ")
    private static String contact;
    @Value("${swagger.license}:license name ")
    private static String license;
    @Value("${swagger.licenseUrl}:license url ")
    private static String licenseUrl;

    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }

    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).includePatterns(".*api/*.*");
    }

    private static ApiInfo apiInfo() {
        return new ApiInfo(title, description, termsOfServiceUrl, contact, license, licenseUrl);
    }
}