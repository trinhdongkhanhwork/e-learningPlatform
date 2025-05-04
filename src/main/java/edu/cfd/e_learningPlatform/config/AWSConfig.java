package edu.cfd.e_learningPlatform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;

@Configuration
public class AWSConfig {

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;
//
//    @Value("${aws.s3.region}")
//    private String regionS3;


    @Bean
    public CloudWatchLogsClient cloudWatchLogsClient() {
        return CloudWatchLogsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

//    @Bean
//    public AmazonS3 amazonS3() {
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(Regions.fromName(regionS3))
//                .withCredentials(new AWSStaticCredentialsProvider(
//                        new BasicAWSCredentials(accessKey, secretKey)))
//                .build();
//    }
}
