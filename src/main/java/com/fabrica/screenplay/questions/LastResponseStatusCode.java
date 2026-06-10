package com.fabrica.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import io.restassured.response.Response;

public class LastResponseStatusCode implements Question<Integer> {

    @Override
    public Integer answeredBy(Actor actor) {
        Response lastResponse = CallAnApi.as(actor).getLastResponse();
        return lastResponse.getStatusCode();
    }

    public static LastResponseStatusCode is() {
        return new LastResponseStatusCode();
    }
}
