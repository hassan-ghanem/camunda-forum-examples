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

@Component
@Import(SimpleJavaMailSpringSupport.class)
public class EmailService {

	@Autowired
	private Mailer mailer;

	@Autowired
	private Configuration freemarkerConfiguration;

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

		Template template = null;
		StringWriter stringWriter;
		String messageBody;

		try {

			if (templateContent != null && !templateContent.isEmpty()) {

				template = new Template("generic_template", new StringReader(templateContent), freemarkerConfiguration);

			} else {

				// test.ftlh
				// templateFile = "test"
				template = freemarkerConfiguration.getTemplate(templateFile);
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
