package com.fabrica.screenplay.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.rest.interactions.RestInteraction;
import io.restassured.specification.RequestSpecification;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.rest.abilities.CallAnApi.as;

public class DoGet extends RestInteraction {

    private final String resource;

    public DoGet(String resource) {
        this.resource = resource;
    }

    @Step("{0} ejecuta un GET en el recurso #resource")
    @Override
    public <T extends Actor> void performAs(T actor) {
        RequestSpecification req = rest().log().all();
        Object token = actor.recall("token");
        if (token != null) {
            req = req.header("Authorization", "Bearer " + token.toString());
        }
        req.get(as(actor).resolve(resource)).then().log().all();
    }

    public static DoGet resource(String resource) {
        return instrumented(DoGet.class, resource);
    }
}
