package com.soliduslabs;

import com.soliduslabs.dao.DynamoDbClient;
import com.soliduslabs.dao.DynamoDbClientMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    DynamoDbClient dynamoDbClient() {
        return new DynamoDbClientMock();
    }

}
