package com.soliduslabs.dao;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.soliduslabs.model.HashMessage;

public class DynamoDbClientImpl implements DynamoDbClient {

    private final String region="us-west-2";
    private final String tableName = "hash-handler";
    private final DynamoDBMapper dynamoDBMapper;

    public DynamoDbClientImpl() {
        DefaultAWSCredentialsProviderChain defaultAWSCredentialsProviderChain = DefaultAWSCredentialsProviderChain.getInstance();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(defaultAWSCredentialsProviderChain).withRegion(region).build();
        dynamoDBMapper = new DynamoDBMapper(client);
    }

    @Override
    public HashMessage getStringByHashKey(String hashKey) {
        return dynamoDBMapper.load(HashMessage.class, hashKey);
    }

    @Override
    public HashMessage saveNewHashKey(HashMessage hashMessage) {
        dynamoDBMapper.save(hashMessage, DynamoDBMapperConfig
                .builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER)
                .build());
        return hashMessage;
    }

}
