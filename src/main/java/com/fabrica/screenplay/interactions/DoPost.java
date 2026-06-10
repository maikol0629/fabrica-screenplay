package com.fabrica.screenplay.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.rest.interactions.RestInteraction;
import io.restassured.specification.RequestSpecification;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.rest.abilities.CallAnApi.as;

public class DoPost extends RestInteraction {

    private final String resource;
    private final String body;

    public DoPost(String resource, String body) {
        this.resource = resource;
        this.body = body;
    }

    @Step("{0} ejecuta un POST en el recurso #resource")
    @Override
    public <T extends Actor> void performAs(T actor) {
        RequestSpecification req = rest().log().all()
            .contentType("application/json")
            .body(body);
        Object token = actor.recall("token");
        if (token != null) {
            req = req.header("Authorization", "Bearer " + token.toString());
        }
        req.post(as(actor).resolve(resource)).then().log().all();
    }

    public static DoPost to(String resource, String body) {
        return instrumented(DoPost.class, resource, body);
    }
}
