package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

public class StatusTests extends TestBase {

    @Test
    @DisplayName("Проверка общего количества сессий")
    public void totalAmountTest_withResponseLogs() {
        given()
                .when()
                .get("/status")
                .then()
                .log().all()
                .body("total", is(25));
    }

    @Test
    @DisplayName("Проверка успешного ответа 200")
    public void totalAmountTest_withAllLogs() {
        given()
                .log().all()
                .when()
                .get("/status")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка наличия обязательных ключей в ответе")
    public void requiredKeysTest() {
        given()
                .log().all()
                .when()
                .get("/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("", hasKey("total"))
                .body("", hasKey("used"))
                .body("", hasKey("queued"))
                .body("", hasKey("pending"))
                .body("", hasKey("browsers"));
    }

    @Test
    @DisplayName("Проверка доступных версий Chrome")
    public void chromeVersionsTest() {
        given()
                .log().all()
                .when()
                .get("/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("browsers.chrome", hasKey("151.0"))
                .body("browsers.chrome", hasKey("151.0-min"))
                .body("browsers.chrome", hasKey("152.0"))
                .body("browsers.chrome", hasKey("152.0-min"));
    }

    @Test
    @DisplayName("Проверка ответа на соответствие JSON Schema")
    public void statusSchemaTest() {
        given()
                .log().all()
                .when()
                .get("/status")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/status_response_schema.json"));
    }
}
