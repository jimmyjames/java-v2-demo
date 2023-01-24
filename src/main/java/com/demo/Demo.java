package com.demo;


import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.Response;

public class Demo {

    public static void main(String[] args) throws Exception {
        String domain = System.getenv("A0_DEMO_DOMAIN");
        String token = System.getenv("A0_MGMT_TOKEN");

        ManagementAPI api = ManagementAPI.newBuilder(domain, token).build();
//        ManagementAPI api = ManagementAPI.newBuilder(domain, token)
//                .withHttpClient(new JavaNetClient())
//                .build();

        // list users
        Response<UsersPage> usersPageResponse = api.users().list(null)
                .addHeader("custom-header", "custom-val")
                .execute();

        System.out.println("response code: " + usersPageResponse.getStatusCode());
        System.out.println("response headers: " + usersPageResponse.getHeaders());
        System.out.println("first user found: " + usersPageResponse.getBody().getItems().get(0).getEmail());



    }
}
