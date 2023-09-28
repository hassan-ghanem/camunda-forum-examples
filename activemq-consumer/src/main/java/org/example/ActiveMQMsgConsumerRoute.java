package org.example;

import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQMsgConsumerRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// https://camel.apache.org/components/4.0.x/languages/simple-language.html
		// https://camel.apache.org/components/4.0.x/eips/marshal-eip.html
		// https://camel.apache.org/components/4.0.x/eips/unmarshal-eip.html
		from("activemq:queue:{{activemq.queue-name}}")
		    .unmarshal().json(JsonLibrary.Jackson, Map.class)
			.choice()
				.when().simple("${body['type']} == 'type-a'")
					.marshal().json()
					.log("type-a")
					.to("file:target/output?filename=type-a-${date:now:yyyyMMdd}.json")
					.to("log:org.example.erp") // ERP System
					.unmarshal().json(JsonLibrary.Jackson, Map.class)
					.marshal().jacksonxml() 
					.to("file:target/output?filename=type-c-${date:now:yyyyMMdd}.xml")
					.to("log:org.example.logging") // Logging System
				.endChoice()
				.otherwise()
					.marshal().json()
					.log("type-b")
					.to("file:target/output?filename=type-a-${date:now:yyyyMMdd}.json")
					.to("log:org.example.erp"); // ERP System
	}
}
