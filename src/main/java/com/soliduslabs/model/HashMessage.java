package com.soliduslabs.model;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "hash-handler")
public class HashMessage {
    @Getter
    @Setter
    @DynamoDBAttribute
    private String message;

    @Getter
    @Setter
    @DynamoDBHashKey
    private String hashKey;

    public HashMessage(String message) {
        this.message = message;
    }

}
