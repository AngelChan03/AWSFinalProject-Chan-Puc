package com.aws.awsproject.service;

import com.aws.awsproject.entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
public class SnsService {

    @Value("${aws.sns.topic-arn}")
    private String topicArn;

    @Value("${aws.region}")
    private String region;


    private SnsClient getSnsClient() {
        return SnsClient.builder()
                .region(Region.of(region))
                .build();
    }

    public void sendStudentEmail(Student student) {
        String message = String.format(
                """
                        Información del Alumno:

                        Nombre: %s %s
                        Matrícula: %s
                        Promedio: %.2f

                        Este es un mensaje automático del sistema SICEI.""",
                student.getNombres(),
                student.getApellidos(),
                student.getMatricula(),
                student.getPromedio()
        );

        String subject = "Información del Alumno - " + student.getNombres() + " " + student.getApellidos();

        SnsClient snsClient = getSnsClient();

        try {
            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .subject(subject)
                    .build();

            snsClient.publish(request);
        } finally {
            snsClient.close();
        }
    }
}
