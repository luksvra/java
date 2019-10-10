package ar.com.hipotecario.api.vendedores.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import ar.com.hipotecario.config.BHConfig;
import ar.com.hipotecario.rest.spgateway.client.ClientSPGateway;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {ClientSPGateway.class})
@Import(BHConfig.class)
@EnableSwagger2
public class AppConfig extends WebMvcConfigurerAdapter {
	

}
