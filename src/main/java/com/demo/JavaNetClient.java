package com.demo;

import com.auth0.net.client.Auth0HttpClient;
import com.auth0.net.client.Auth0HttpRequest;
import com.auth0.net.client.HttpMethod;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;

public class JavaNetClient implements Auth0HttpClient {
    java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

    @Override
    public com.auth0.net.client.Auth0HttpResponse sendRequest(Auth0HttpRequest a0Request) throws IOException {
        System.out.println("******** USING CUSTOM HTTP CLIENT (SYNC) ********");
        // build request
        HttpRequest req = getHttpRequest(a0Request);

        // execute
        java.net.http.HttpResponse<String> response;
        try {
            response = client.send(req, java.net.http.HttpResponse.BodyHandlers.ofString());

            // Transform response to Auth0 response type
            return com.auth0.net.client.Auth0HttpResponse.newBuilder()
                    .withStatusCode(response.statusCode())
                    .withBody(response.body())
                    .build();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    @Override
    public CompletableFuture<com.auth0.net.client.Auth0HttpResponse> sendRequestAsync(Auth0HttpRequest a0Request) {
        System.out.println("******** USING CUSTOM HTTP CLIENT (ASYNC) ********");

        // build request
        HttpRequest req = getHttpRequest(a0Request);

        // execute async and parse response for JSON deserializers
        com.auth0.net.client.Auth0HttpResponse.Builder aResponse = com.auth0.net.client.Auth0HttpResponse.newBuilder();
        return client.sendAsync(req, java.net.http.HttpResponse.BodyHandlers.ofString())
                .thenApply(javaResponse -> {
                    aResponse.withStatusCode(javaResponse.statusCode());
                    aResponse.withBody(javaResponse.body());
                    return aResponse.build();
                });
    }

    private static HttpRequest getHttpRequest(Auth0HttpRequest request) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(request.getUrl()));

        switch(request.getMethod()) {
            // Only GET and POST implemented here for demo
            case GET:
                builder.GET();
                break;
            case POST:
                builder.POST(HttpRequest.BodyPublishers.ofByteArray(request.getBody().getContent()));
                break;
            default:
                throw new RuntimeException("boom");
        }

        // add headers, etc.
        request.getHeaders().forEach(builder::setHeader);

        return builder.build();
    }
}
