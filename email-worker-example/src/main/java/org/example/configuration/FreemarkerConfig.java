package org.example.configuration;

import org.example.freemarker.DropboxURLTemplateLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
public class FreemarkerConfig {

	@Value("${dropbox.download-files-url}")
	private String downloadFilesUrl;
	
	@Value("${dropbox.templates-path}")
	private String templatesPath;
	
	@Value("${dropbox.access-token}")
	private String dropboxToken;
	
	@Bean
	public freemarker.template.Configuration freemarkerConfiguration() {
		
		final String ENCODING = "UTF-8";
		// final String TEMPLATES_PATH = "/templates";
		
		freemarker.template.Configuration cfg;
		
		cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
		cfg.setDefaultEncoding(ENCODING);
		cfg.setLocalizedLookup(false);
		// cfg.setClassForTemplateLoading(FreemarkerConfig.class, TEMPLATES_PATH);
		
		cfg.setTemplateLoader(new DropboxURLTemplateLoader(downloadFilesUrl, templatesPath, dropboxToken));
		
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
