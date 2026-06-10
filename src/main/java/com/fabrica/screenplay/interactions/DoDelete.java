package com.fabrica.screenplay.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.rest.interactions.RestInteraction;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.rest.abilities.CallAnApi.as;

public class DoDelete extends RestInteraction {

    private final String resource;

    public DoDelete(String resource) {
        this.resource = resource;
    }

    @Step("{0} ejecuta un DELETE en el recurso #resource")
    @Override
    public <T extends Actor> void performAs(T actor) {
        io.restassured.specification.RequestSpecification req = rest().log().all();
        Object token = actor.recall("token");
        if (token != null) {
            req = req.header("Authorization", "Bearer " + token.toString());
        }
        req.delete(as(actor).resolve(resource)).then().log().all();
    }

    public static DoDelete from(String resource) {
        return instrumented(DoDelete.class, resource);
    }
}
