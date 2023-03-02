package org.example.configuration;

import org.example.email.EmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FreemarkerConfig {

	@Bean
	public freemarker.template.Configuration freemarkerConfiguration() {
		
		final String ENCODING = "UTF-8";
		final String TEMPLATES_PATH = "/templates";
		
		freemarker.template.Configuration cfg;
		
		cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
		cfg.setDefaultEncoding(ENCODING);
		cfg.setClassForTemplateLoading(EmailService.class, TEMPLATES_PATH);
		
		return cfg;
	}
}
