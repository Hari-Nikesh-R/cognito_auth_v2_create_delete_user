package org.example.model;

public class CognitoProperties {
    public CognitoProperties(String accessKey, String secretKey, String clientId, String userPool, String accessToken, String groups) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.clientId = clientId;
        this.userPool = userPool;
        this.accessToken = accessToken;
        this.groups = groups;
    }

    public CognitoProperties()  {}

    private String accessKey;
    private String secretKey;
    private String clientId;

    private String userPool;
    private String groups;

    public String getUserPool() {
        return userPool;
    }

    public void setUserPool(String userPool) {
        this.userPool = userPool;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
