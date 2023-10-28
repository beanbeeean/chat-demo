package com.example.demo.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@DynamoDBTable(tableName="test")
@Getter
@AllArgsConstructor
public class Lyric {

    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute
    private String lyrics;

    @DynamoDBAttribute
    private String chat;

    @DynamoDBAttribute
    private Map<String,String> map;
}