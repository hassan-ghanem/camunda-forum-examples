package org.example.configuration;

import org.example.freemarker.DropboxURLTemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FreemarkerConfig {

	@Bean
	public freemarker.template.Configuration freemarkerConfiguration() {
		
		final String ENCODING = "UTF-8";
		// final String TEMPLATES_PATH = "/templates";
		
		freemarker.template.Configuration cfg;
		
		cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
		cfg.setDefaultEncoding(ENCODING);
		cfg.setLocalizedLookup(false);
		// cfg.setClassForTemplateLoading(FreemarkerConfig.class, TEMPLATES_PATH);
		
		cfg.setTemplateLoader(new DropboxURLTemplateLoader());
		
		/*
		 * https://freemarker.apache.org/docs/pgui_config_templateloading.html#autoid_41
		 * 
		 * If your template source accesses the templates through an URL, you needn't
		 * implement a TemplateLoader from scratch; you can choose to subclass
		 * freemarker.cache.URLTemplateLoader instead and just implement the URL
		 * getURL(String templateName) method.
		 */
		
		return cfg;
	}
}
