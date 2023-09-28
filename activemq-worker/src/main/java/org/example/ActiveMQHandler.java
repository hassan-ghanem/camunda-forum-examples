package org.example;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@ExternalTaskSubscription("send_activemq_msg")
public class ActiveMQHandler implements ExternalTaskHandler {

	@Autowired
	private Sender sender;
	
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		
		String queueName;
		JsonValue jsonData;
		
		try {
			
			jsonData = externalTask.getVariableTyped("jsonData");
			queueName = externalTask.getVariable("queueName");
			
			sender.send(queueName, jsonData.getValue());
			
			externalTaskService.complete(externalTask);
			
		} catch (Exception e) {

			externalTaskService.handleFailure(externalTask, e.getMessage(), e.getMessage(), 0, 0);
		}
	}

}
