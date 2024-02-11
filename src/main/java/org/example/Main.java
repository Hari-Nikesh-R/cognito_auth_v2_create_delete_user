package org.example;

import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.CognitoAuthRequest;
import org.example.service.CognitoRegisterService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.example.utils.Constants.ENTER_EMAIL;
import static org.example.utils.Constants.ENTER_PASSWORD;
import static org.example.utils.Constants.INTERNAL_REGISTER_DESCRIPTION;
import static org.example.utils.Constants.REGISTER_ALL_STATIC_USER;

/**
 *  delete_user_request.json -> delete specific user in the json
 *  properties.json -> It is used to connect to the userPool
 *  user_request.json -> static data can be injected.
 */


public class Main {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static void description() {
        System.out.println("1. Register User");
        System.out.println("2. Delete User");
        System.out.println("3. Delete All User");
    }

    public static void main(String[] args) throws IOException {
        CognitoRegisterService cognitoRegisterService = new CognitoRegisterService();
        while (true) {
            description();
            controller(cognitoRegisterService);
        }
    }

    private static void controller(CognitoRegisterService cognitoRegisterService) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println(INTERNAL_REGISTER_DESCRIPTION);
                int registerChoice = scanner.nextInt();
                if (registerChoice == 2) {
                    scanner.nextLine();
                    SignUpRequest signUpRequest = new SignUpRequest();
                    System.out.println(ENTER_EMAIL);
                    signUpRequest.setUsername(scanner.nextLine());
                    System.out.println(ENTER_PASSWORD);
                    signUpRequest.setPassword(scanner.nextLine());
                    System.out.println(cognitoRegisterService.register(signUpRequest));
                } else {
                    constructSignUpRequest(cognitoRegisterService);
                    System.out.println(REGISTER_ALL_STATIC_USER);
                }
                break;
            case 2:
                System.out.println(cognitoRegisterService.deleteUser());
                break;
            case 3:
                System.out.println(cognitoRegisterService.deleteAllUser());
                break;
            default:
                System.exit(0);
        }
    }


    /**
     * Constructs sign-up requests and registers users using the provided CognitoRegisterService.
     *
     * @param cognitoRegisterService the CognitoRegisterService instance used to register users
     * @throws IOException if an I/O error occurs
     */
    private static void constructSignUpRequest(CognitoRegisterService cognitoRegisterService) throws IOException {
            File file = new File("src/main/resources/user_request.json");
            List<CognitoAuthRequest> cognitoAuthRequests = mapper.readValue(file, new TypeReference<List<CognitoAuthRequest>>() {
            });
            cognitoAuthRequests.forEach(req -> {
                SignUpRequest signUpRequest = new SignUpRequest();
                signUpRequest.setUsername(req.getUsername());
                signUpRequest.setPassword(req.getPassword());
                cognitoRegisterService.register(signUpRequest);
            });
        }
}