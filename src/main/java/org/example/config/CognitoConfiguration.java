package org.example.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.CognitoProperties;

import java.io.File;
import java.io.IOException;


/**
 * Provides configuration for connecting to Amazon Cognito.
 */
public class CognitoConfiguration {
    /**
     * Constructs a new CognitoConfiguration instance.
     * @throws IOException if an I/O error occurs
     */
    private CognitoConfiguration() throws IOException {
        this.mapper = new ObjectMapper();
        this.cognitoProperties = constructCognitoProperties();
    }
    private ObjectMapper mapper;
    private CognitoProperties cognitoProperties;
    /**
     * Gets the Cognito properties.
     * @return the Cognito properties
     */
    public CognitoProperties getCognitoProperties() {
        return cognitoProperties;
    }
    /**
     * Gets the Cognito properties from file properties.json.
     * @return the Cognito properties
     */
    private CognitoProperties constructCognitoProperties() throws IOException {
        File file = new File("src/main/resources/properties.json");
        return mapper.readValue(file, CognitoProperties.class);
    }


    private static CognitoConfiguration cognitoConfigurationInstance;
    /**
     * Gets the singleton instance of CognitoConfiguration.
     * @return the singleton instance of CognitoConfiguration
     * @throws IOException if an I/O error occurs
     */

    public static CognitoConfiguration getCognitoConfigurationInstance() throws IOException {
        if (cognitoConfigurationInstance == null) {
            cognitoConfigurationInstance = new CognitoConfiguration();
        }
        return cognitoConfigurationInstance;
    }
    /**
     * Gets the Amazon Cognito Identity Provider client.
     * @return the Amazon Cognito Identity Provider client
     * @throws IOException if an I/O error occurs
     */
    public AWSCognitoIdentityProvider getAWSCognitoIdentityProvider() throws IOException {
        BasicAWSCredentials credentials = new BasicAWSCredentials(cognitoProperties.getAccessKey(), cognitoProperties.getSecretKey());

        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_1)
                .build();
    }


}
