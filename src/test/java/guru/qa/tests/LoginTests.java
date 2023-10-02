package guru.qa.tests;

import guru.qa.models.LoginBodyModel;
import guru.qa.models.LoginResponseModel;
import guru.qa.models.MissingPasswordResponseModel;
import org.junit.jupiter.api.Test;

import static guru.qa.specs.LoginSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginTests extends BaseTest {

    @Test
    void successfulLoginTest() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseModel response = step("Make a login request", () ->
            given(loginRequestSpec)
                .body(authData)
                .when()
                .post("/login")
                .then()
                .spec(loginResponseSpec)
                .extract().as(LoginResponseModel.class));

        step("Verify response", () ->
            assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4"));
    }


    @Test
    void unsuccessfulLoginTest() {
        LoginBodyModel incompleteAuthData = new LoginBodyModel();
        incompleteAuthData.setEmail("eve.holt@reqres.in");

        MissingPasswordResponseModel response = step("Make a login request without password", () ->
            given(loginRequestSpec)
                .body(incompleteAuthData)
                .when()
                .post("/login")
                .then()
                .spec(missingPasswordLoginSpec)
                .extract().as(MissingPasswordResponseModel.class));

        step("Verify response", () ->
            assertThat(response.getError()).isEqualTo("Missing password"));
    }
}
