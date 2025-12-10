package com.aws.awsproject.service;

import com.aws.awsproject.entity.SessionAlumno;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

@Service
public class DynamoDbService {

    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    @Value("${aws.region}")
    private String region;


    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private DynamoDbEnhancedClient getDynamoDbClient() {
        DynamoDbClient dynamoDbClient;

        dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(region))
                .build();

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    private DynamoDbTable<SessionAlumno> getTable() {
        return getDynamoDbClient().table(tableName, TableSchema.fromBean(SessionAlumno.class));
    }

    public SessionAlumno createSession(Integer alumnoId) {
        SessionAlumno session = new SessionAlumno();
        session.setId(UUID.randomUUID().toString());
        session.setFecha(Instant.now().getEpochSecond());
        session.setAlumnoId(alumnoId);
        session.setActive(true);
        session.setSessionString(generateRandomString(128));

        getTable().putItem(session);

        return session;
    }

    public boolean verifySession(Integer alumnoId, String sessionString) {
        DynamoDbTable<SessionAlumno> table = getTable();

        return table.scan().items().stream()
                .anyMatch(session ->
                        session.getSessionString().equals(sessionString) &&
                                session.getAlumnoId().equals(alumnoId) &&
                                Boolean.TRUE.equals(session.getActive())
                );
    }

    public void logoutSession(Integer alumnoId, String sessionString) {
        DynamoDbTable<SessionAlumno> table = getTable();

        Optional<SessionAlumno> sessionOpt = table.scan().items().stream()
                .filter(session ->
                        session.getSessionString().equals(sessionString) &&
                                session.getAlumnoId().equals(alumnoId)
                )
                .findFirst();

        if (sessionOpt.isPresent()) {
            SessionAlumno session = sessionOpt.get();
            session.setActive(false);
            table.updateItem(session);
        }
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
