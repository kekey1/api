package gov.ca.emsa.pulse;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.google.common.base.Predicate;

import springfox.documentation.PathProvider;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@PropertySource("classpath:environment.properties")
@EnableSwagger2
public class SwaggerConfig implements EnvironmentAware {

	@Autowired ServletContext context;
	@Autowired private Environment env;

	@Override
    public void setEnvironment(final Environment environment) {
        this.env = environment;
    }

    @Bean
    public Docket customDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .pathProvider(pathProvider())
            .select()
            .paths(this.paths())
            .build();
    }

    private ApiInfo apiInfo() {
    	return new ApiInfo("PULSE", "Patient Unified Lookup System for Emergencies", "0.0.1",
                           "http://terms/of/service.url", "pulse@ainq.com",
                           "License Text", "LICENSE");
    }

    private PathProvider pathProvider() {
    	return new AbsolutePathProvider(context);
    }

    @SuppressWarnings("unchecked")
    private Predicate<String> paths() {
        return or(
                  regex("/greeting.*")
                  );
    }

    private class AbsolutePathProvider extends RelativePathProvider {
    	public AbsolutePathProvider(ServletContext context) {
    		super(context);
    	}

    	@Override
    	public String getApplicationBasePath() {
    		return env.getProperty("basePath");
    	}
    }
}
