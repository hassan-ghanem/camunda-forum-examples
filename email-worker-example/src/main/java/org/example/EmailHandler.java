package org.example;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.variable.value.JsonValue;
import org.example.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ExternalTaskSubscription("send_email")
public class EmailHandler implements ExternalTaskHandler {

	protected static final Logger LOG = LoggerFactory.getLogger(EmailHandler.class);

	@Autowired
	private EmailService emailService;

	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

		String subject;
		String from;
		String templateFile;
		String templateContent;

		JsonValue jsonTo;
		List<String> to;

		JsonValue jsonTemplateData;
		HashMap<String, String> templateData;

		ObjectMapper mapper;

		try {

			subject = externalTask.getVariable("subject");
			from = externalTask.getVariable("from");
			templateFile = externalTask.getVariable("templateFile");
			templateContent = externalTask.getVariable("templateContent");

			// jsonTo: array of strings
			jsonTo = externalTask.getVariableTyped("to");
			// jsonTemplateData: map [key, value]
			jsonTemplateData = externalTask.getVariableTyped("templateData");

			mapper = new ObjectMapper();

			to = mapper.readValue(jsonTo.getValue(), new TypeReference<List<String>>() {});
			templateData = mapper.readValue(jsonTemplateData.getValue(), new TypeReference<HashMap<String, String>>() {});

			emailService.sendHTMLText(subject, from, to, templateFile, templateContent, templateData);

			externalTaskService.complete(externalTask);

		} catch (Exception e) {

			externalTaskService.handleFailure(externalTask, e.getMessage(), e.getMessage(), 0, 0);
		}
	}

}
