package org.example.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;
import com.amazonaws.services.cognitoidp.model.AdminConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.CognitoConfiguration;
import org.example.model.CognitoAuthRequest;
import org.example.model.CognitoProperties;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service class for managing user registration and deletion in Amazon Cognito.
 */
public class CognitoRegisterService {
    private final AWSCognitoIdentityProvider cognitoUserPool;
    private final CognitoProperties config;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs a new CognitoRegisterService instance.
     * @throws IOException if an I/O error occurs
     */
    public CognitoRegisterService() throws IOException {
        this.config = CognitoConfiguration.getCognitoConfigurationInstance().getCognitoProperties();
        cognitoUserPool = CognitoConfiguration.getCognitoConfigurationInstance().getAWSCognitoIdentityProvider();
    }

    // deleting specific user
    /**
     * Deletes a specific user.
     * @return a message indicating the deletion status
     * @throws IOException if an I/O error occurs
     */
    public String deleteUser() throws IOException {
        deleteUserByUsername();
        return "USER_DELETED";
    }


    /**
     * Deletes all users in the user pool.
     * @return a message indicating the deletion status
     */
    public String deleteAllUser() {
        ListUsersRequest listUsersRequest = new ListUsersRequest();
        listUsersRequest.setUserPoolId(config.getUserPool());
        ListUsersResult listUsersResult = cognitoUserPool.listUsers(listUsersRequest);
        // Iterating and deleting all user
        listUsersResult.getUsers().forEach(user -> {
            String userName = user.getUsername();
            System.out.println("Deleting user: " + userName);
            cognitoUserPool.adminDeleteUser(new AdminDeleteUserRequest().withUserPoolId(config.getUserPool()).withUsername(userName));
            System.out.println("User deleted: " + userName);
        });

        return "USER_ALL_DELETED_SUCCESSFULLY";
    }


    //    Register user and confirms with admin username and password
    /**
     * Registers a user and confirms the registration using admin username and password.
     * @param request the sign-up request
     * @return a message indicating the registration status
     */
    public String register(SignUpRequest request) {
        try {
            AdminConfirmSignUpRequest adminConfirmSignUpRequest = new AdminConfirmSignUpRequest();
            adminConfirmSignUpRequest.setUsername(request.getUsername());
            adminConfirmSignUpRequest.setUserPoolId(config.getUserPool());
            request.setClientId(config.getClientId());
            request.setUserAttributes(request.getUserAttributes());
            cognitoUserPool.signUp(request);
            cognitoUserPool.adminConfirmSignUp(adminConfirmSignUpRequest);
            return "User registered successfully";
        } catch (AWSCognitoIdentityProviderException e) {
            System.out.println(e.getErrorMessage());
            return e.getErrorMessage();
        } catch (Exception e) {
            System.out.println("SETTING_NEW_PASSWORD" + e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Deletes users by username specified in a delete_user_request JSON file.
     * @throws IOException if an I/O error occurs
     */
    private void deleteUserByUsername() throws IOException {
        File file = new File("src/main/resources/delete_user_request.json");
        List<CognitoAuthRequest> cognitoAuthRequests = mapper.readValue(file, new TypeReference<List<CognitoAuthRequest>>() {
        });
        cognitoAuthRequests.forEach(req -> {
            AdminDeleteUserRequest adminDeleteUserRequest = new AdminDeleteUserRequest();
            adminDeleteUserRequest.setUsername(req.getUsername());
            adminDeleteUserRequest.setUserPoolId(config.getUserPool());
            cognitoUserPool.adminDeleteUser(adminDeleteUserRequest);
        });
    }


}
