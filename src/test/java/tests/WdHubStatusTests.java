package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

public class WdHubStatusTests extends TestBase {

    @Test
    @DisplayName("Проверка ответа 401 при запросе без авторизации")
    public void unauthorizedStatusTest() {
        given()
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("Проверка успешного ответа 200 при авторизации")
    public void statusTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка ответа на соответствие JSON Schema")
    public void wdHubStatusSchemaTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .body(matchesJsonSchemaInClasspath("schemas/wd_hub_status_response_schema.json"));
    }

    @Test
    @DisplayName("Проверка значения value.ready = true")
    public void readyValueMustBeTrueTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .body("value.ready", is(true));
    }

    @Test
    @DisplayName("Проверка что поле value.message не путое")
    public void messageValueTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .body("value.message", is(not(is(""))))
                .body("value.message", is(notNullValue()));
    }

    @Test
    @DisplayName("Проверка ответа 404 для несуществующего endpoint")
    public void notFoundStatusTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/as/wd/hub/status")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("Проверка Content-Type ответа")
    public void contentTypeTest() {
        given()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    @DisplayName("Проверка ответа 401 при неверном пароле")
    public void invalidPasswordTest() {
        given()
                .auth().basic("user1", "wrongPassword")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("Проверка ответа 401 при неверном логине")
    public void invalidUsernameTest() {
        given()
                .auth().basic("wrongUser", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401);
    }
}