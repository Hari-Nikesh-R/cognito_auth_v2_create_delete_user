package org.example;

import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.CognitoRegisterService;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *  delete_user_request.json -> delete specific user in the json
 *  properties.json -> It is used to connect to the userPool
 *  user_request.json -> static data can be injected.
 */


public class Main {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CognitoRegisterService cognitoRegisterService = new CognitoRegisterService();

        /**
         *  Register with user attributes
         * custom:role
         * custom:tenant_id
         * email
         * family_name
         * given_name
         * locale
         * phone_number
         * zoneinfo
         */
//        constructSignUpRequest(cognitoRegisterService);

        /**
         * ==================
         *  Delete all users
         *  ================
          */
        cognitoRegisterService.deleteAllUser();
    }

    /**
     * Constructs sign-up requests and registers users using the provided CognitoRegisterService.
     *
     * @param cognitoRegisterService the CognitoRegisterService instance used to register users
     * @throws IOException if an I/O error occurs
     */
    private static void constructSignUpRequest(CognitoRegisterService cognitoRegisterService) throws IOException {
            File file = new File("src/main/resources/user_request.json");
            List<SignUpRequest> signUpRequests = mapper.readValue(file, new TypeReference<List<SignUpRequest>>() {
            });
            signUpRequests.forEach(cognitoRegisterService::register);
        }
}