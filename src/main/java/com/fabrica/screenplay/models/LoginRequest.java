package com.fabrica.screenplay.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    private final String email;

    @SerializedName("contraseña")
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
