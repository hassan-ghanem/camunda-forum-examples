package org.example.email;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

@Component
@Import(SimpleJavaMailSpringSupport.class)
public class EmailService {

	@Autowired
	private Mailer mailer;

	private Email email;

	private EmailPopulatingBuilder emailBuilder(String subject, String from, List<String> to) {

		return EmailBuilder.startingBlank().from(null, from).to(null, to).withSubject(subject);
	}

	public void sendPlainText(String subject, String from, List<String> to, String messageBody) {

		email = emailBuilder(subject, from, to).withPlainText(messageBody).buildEmail();

		mailer.sendMail(email, true);
	}

	public void sendHTMLText(String subject, String from, List<String> to, String templateFile, String templateContent,
			HashMap<String, String> templateData) throws Exception {

		final String FREEMARKER_VER = "2.3.31";
		final String ENCODING = "UTF-8";
		final String TEMPLATES_PATH = "/templates";

		Template template = null;
		Configuration cfg;
		StringWriter stringWriter;
		String messageBody;

		try {

			cfg = new Configuration(new Version(FREEMARKER_VER));
			cfg.setDefaultEncoding(ENCODING);
			
			if (templateContent != null && !templateContent.isEmpty()) {
				
				template = new Template("generic_template", new StringReader(templateContent), cfg);
				
			} else {
				
				cfg.setClassForTemplateLoading(EmailService.class, TEMPLATES_PATH);

				template = cfg.getTemplate(templateFile);
			}
			

			stringWriter = new StringWriter();
			template.process(templateData, stringWriter);

			messageBody = stringWriter.getBuffer().toString();

			email = emailBuilder(subject, from, to).withHTMLText(messageBody).buildEmail();

			mailer.sendMail(email, true);

		} catch (Exception e) {

			throw e;
		}

	}
}
