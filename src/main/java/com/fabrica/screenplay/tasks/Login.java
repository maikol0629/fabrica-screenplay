package com.fabrica.screenplay.tasks;

import com.fabrica.screenplay.interactions.DoPost;
import com.fabrica.screenplay.models.LoginRequest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import io.restassured.response.Response;
import com.google.gson.Gson;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Login implements Task {

    private final String endpoint;
    private final LoginRequest credentials;

    public Login(String endpoint, LoginRequest credentials) {
        this.endpoint = endpoint;
        this.credentials = credentials;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String requestBody = new Gson().toJson(credentials);
        actor.attemptsTo(DoPost.to(endpoint, requestBody));

        Response response = CallAnApi.as(actor).getLastResponse();
        String token = response.jsonPath().getString("token");
        if (token == null || token.isEmpty()) {
            token = response.jsonPath().getString("access_token");
        }
        if (token == null || token.isEmpty()) {
            token = response.jsonPath().getString("data.token");
        }
        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("No se pudo obtener el token de login. Respuesta: " + response.getBody().asString());
        }
        actor.remember("token", token);
    }

    public static Login with(String endpoint, LoginRequest credentials) {
        return instrumented(Login.class, endpoint, credentials);
    }
}
