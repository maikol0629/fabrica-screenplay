package com.fabrica.screenplay.utils;

import com.fabrica.screenplay.models.ModelCreateServicio;
import com.google.gson.Gson;

public class Utils {

    private Utils() {}

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ModelCreateServicio createUniqueServicio() {
        long ts = System.currentTimeMillis();
        return new ModelCreateServicio(
            "Servicio Test " + ts,
            "Descripcion generada " + ts,
            10000.0 + (ts % 50000),
            (int) (30 + (ts % 60))
        );
    }
}
