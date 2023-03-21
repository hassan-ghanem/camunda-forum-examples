package org.example;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.client.variable.value.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import kong.unirest.Empty;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.RequestBodyEntity;
import kong.unirest.Unirest;

@Configuration
@PropertySource("classpath:application.yml")
public class GenericHandler {

	protected static final Logger LOG = LoggerFactory.getLogger(GenericHandler.class);
	
	@Value("${directus.items-base-url}")
	private String directusItemsBaseUrl;
	
	@Value("${directus.access-token}")
	private String token;
	
	// inputs
	private final String ID = "id";
	private final String COLLECTION = "collection";
	private final String JSON_DATA = "jsonData";
	
	// output
	private final String JSON_OUTPUT = "jsonOutput";
	
	
	private RequestBodyEntity directusUpdate(String collection, String id, JsonValue jsonData) {
		
		return Unirest.patch(this.directusItemsBaseUrl + "{collection}/{id}")
				.routeParam("collection", collection)
				.routeParam("id", id)
				.accept("application/json")
				.header("Content-Type", "application/json")
				.header("Authorization", this.token)
				.body(jsonData.getValue());
	}
	
	@Bean
	@ExternalTaskSubscription("directus_update")
	public ExternalTaskHandler directusUpdateHandler() {
		
		return (externalTask, externalTaskService) -> {
			
			
			String id;
			String collection;
			JsonValue jsonData;
			
			HttpResponse<Empty> response;
			
			try {
				
				collection = externalTask.getVariable(COLLECTION);
				id = externalTask.getVariable(ID);
				jsonData = externalTask.getVariableTyped(JSON_DATA);
				
				response = this.directusUpdate(collection, id, jsonData).asEmpty();
				
				if (response.getStatus() >= 400) {
					
					throw new Exception(response.getStatusText());
				}
				
				externalTaskService.complete(externalTask);
	
				
			} catch (Exception e) {
				
				externalTaskService.handleFailure(externalTask, e.getMessage(), e.getMessage(), 0, 0);
			}
		};
	}
	
	@Bean
	@ExternalTaskSubscription("directus_update_with_result")
	public ExternalTaskHandler directusUpdateWithResultHandler() {
		
		return (externalTask, externalTaskService) -> {
			
			
			String id;
			String collection;
			JsonValue jsonData;
			
			HttpResponse<JsonNode> jsonResponse;
			JsonValue jsonOutput;
			Map<String, Object> localVariables;
			
			try {
				
				collection = externalTask.getVariable(COLLECTION);
				id = externalTask.getVariable(ID);
				jsonData = externalTask.getVariableTyped(JSON_DATA);
				
				jsonResponse = this.directusUpdate(collection, id, jsonData).asJson();
				
				if (jsonResponse.getStatus() >= 400) {
					
					throw new Exception(jsonResponse.getStatusText());
				}
				
				localVariables = new HashMap<>();
				
				jsonOutput = ClientValues.jsonValue(jsonResponse.getBody().toString());
				localVariables.put(JSON_OUTPUT, jsonOutput);
				
				externalTaskService.complete(externalTask, null, localVariables);
	
				
			} catch (Exception e) {
				
				externalTaskService.handleFailure(externalTask, e.getMessage(), e.getMessage(), 0, 0);
			}
		};
	}
}
