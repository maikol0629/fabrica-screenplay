Feature: Autenticacion y seguridad
  Como administrador del sistema
  Quiero iniciar sesion de forma segura
  Para poder acceder a las funcionalidades protegidas

  @Login_Exitoso
  Scenario: Inicio de sesion con credenciales validas
    Given que el administrador inicia sesion con sus credenciales de prueba
    Then deberia obtener un codigo de respuesta 200
    And la respuesta debe contener un token de autenticacion

  @Login_Exitoso
  Scenario: Inicio de sesion con credenciales explicitas
    Given que el administrador inicia sesion con email "admin@barberia.com" y contrasena "password123"
    Then deberia obtener un codigo de respuesta 200
    And la respuesta debe contener un token de autenticacion

  @Negativo
  Scenario: Inicio de sesion con credenciales invalidas
    When el administrador intenta iniciar sesion con email "invalido@test.com" y contrasena "wrongpass"
    Then deberia obtener un codigo de respuesta 401

  @Negativo
  Scenario: Inicio de sesion con body vacio
    When envia una solicitud POST a "/api/v1/auth/login" con body "{}"
    Then deberia obtener un codigo de respuesta 401
