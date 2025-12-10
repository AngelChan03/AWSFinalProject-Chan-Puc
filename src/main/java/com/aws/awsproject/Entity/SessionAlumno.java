package com.aws.awsproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;


@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionAlumno {

    private String id;

    private Long fecha;

    private Integer alumnoId;

    private Boolean active;

    private String sessionString;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
