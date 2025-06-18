package tests;

import io.restassured.RestAssured;
import models.lombok.BodyLombokModel;
import models.lombok.ResponseLombokModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class SpecedTests {


    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    void patchApiTestSuccessfulTest() {

        BodyLombokModel authData = new BodyLombokModel();
        String patchData = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";
        String currentYear = String.valueOf(LocalDateTime.now().getYear());
        String currentMonth = String.format("%02d", LocalDateTime.now().getMonthValue());

        ResponseLombokModel response = step("Sent request", () -> {
            return given().body(patchData)
                    .contentType("application/json")
                    .header("x-api-key", "reqres-free-v1")
                    .log().uri()
                    .when()
                    .patch("/users/2")
                    .then()
                    .log().status()
                    .log().body()
                    .body("name", is("morpheus"))
                    .body("job", is("zion resident"))
                    .body("updatedAt", notNullValue())
                    .body("updatedAt", containsString(currentYear))
                    .body("updatedAt", containsString(currentMonth))
                    .body("updatedAt", matchesPattern(".*T.*Z"))
                    .extract()
                    .as(ResponseLombokModel.class);
        });
    }
}