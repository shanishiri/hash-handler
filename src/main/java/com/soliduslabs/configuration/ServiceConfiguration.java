package com.soliduslabs.configuration;

import com.soliduslabs.dao.DynamoDbClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ServiceConfiguration {

    @Bean
    DynamoDbClient dynamoDbClient() {
        return new DynamoDbClient();
    }

}
