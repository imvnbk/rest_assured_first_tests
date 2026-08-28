package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

public class StatusTests {
    @Test
    public void totalAmountTest_withResponseLogs() {
        given()
                .when()
                .get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .body("state.total", is(25));
    }

    @Test
    public void totalAmountTest_withAllLogs() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void requiredKeysTest() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("state", hasKey("total"))
                .body("state", hasKey("used"))
                .body("state", hasKey("queued"))
                .body("state", hasKey("pending"))
                .body("", hasKey("browsers"));
    }
    @Test
    public void chromeVersionsTest() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("state.browsers.chrome", hasKey("151.0"))
                .body("state.browsers.chrome", hasKey("151.0-min"))
                .body("state.browsers.chrome", hasKey("152.0"))
                .body("state.browsers.chrome", hasKey("152.0-min"));
    }

    @Test
    public void statusSchemaTest() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.qa.guru/ui/status")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/status_response_schema.json"));
    }
}