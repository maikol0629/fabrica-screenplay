package com.fabrica.screenplay.stepdefinitions;

import com.fabrica.screenplay.interactions.DoPost;
import com.fabrica.screenplay.models.LoginRequest;
import com.fabrica.screenplay.questions.LastResponseStatusCode;
import com.fabrica.screenplay.questions.ServerResponse;
import com.fabrica.screenplay.tasks.Login;
import com.google.gson.Gson;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.*;

public class LoginStepDefinition {

    private Actor user;
    private String baseUrl = "http://localhost:8080";

    @Before(order = 0)
    public void config() {
        OnStage.setTheStage(new OnlineCast());
        user = OnStage.theActorCalled("admin");
        user.whoCan(CallAnApi.at(baseUrl));
    }

    @Given("que el administrador inicia sesion con email {string} y contrasena {string}")
    public void administradorIniciaSesionConCredenciales(String email, String password) {
        user.attemptsTo(Login.with("/api/v1/auth/login", new LoginRequest(email, password)));
    }

    @Given("que el administrador inicia sesion con sus credenciales de prueba")
    public void administradorIniciaSesionConCredencialesDePrueba() {
        String email = System.getenv().getOrDefault("TEST_EMAIL",
            System.getenv().getOrDefault("TEST_USERNAME", "admin"));
        String password = System.getenv().getOrDefault("TEST_PASSWORD", "admin");
        user.attemptsTo(Login.with("/api/v1/auth/login", new LoginRequest(email, password)));
    }

    @When("el administrador intenta iniciar sesion con email {string} y contrasena {string}")
    public void intentaIniciarSesion(String email, String password) {
        LoginRequest creds = new LoginRequest(email, password);
        String body = new Gson().toJson(creds);
        user.attemptsTo(DoPost.to("/api/v1/auth/login", body));
    }

    @Then("la respuesta debe contener un token de autenticacion")
    public void respuestaContieneToken() {
        user.should(
            seeThat("token", ServerResponse.jsonPathString("data.token"), not(isEmptyOrNullString()))
        );
    }
}
