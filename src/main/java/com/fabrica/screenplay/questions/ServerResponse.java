package com.fabrica.screenplay.questions;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import io.restassured.response.Response;

public class ServerResponse {

    private ServerResponse() {}

    public static Question<String> body() {
        return actor -> {
            Response response = CallAnApi.as(actor).getLastResponse();
            return response.getBody().asString();
        };
    }

    public static Question<Response> full() {
        return actor -> CallAnApi.as(actor).getLastResponse();
    }

    public static Question<Object> jsonPath(String path) {
        return actor -> CallAnApi.as(actor).getLastResponse().jsonPath().get(path);
    }

    public static Question<String> jsonPathString(String path) {
        return actor -> CallAnApi.as(actor).getLastResponse().jsonPath().getString(path);
    }

    public static Question<Integer> jsonPathInt(String path) {
        return actor -> CallAnApi.as(actor).getLastResponse().jsonPath().getInt(path);
    }

    public static Question<Double> jsonPathDouble(String path) {
        return actor -> CallAnApi.as(actor).getLastResponse().jsonPath().getDouble(path);
    }
}
