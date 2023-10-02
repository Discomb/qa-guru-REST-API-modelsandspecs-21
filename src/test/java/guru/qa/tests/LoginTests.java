package guru.qa.tests;

import guru.qa.models.LoginBodyModel;
import guru.qa.models.LoginResponseModel;
import guru.qa.models.MissingPasswordResponseModel;
import org.junit.jupiter.api.Test;

import static guru.qa.specs.LoginSpec.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginTests extends BaseTest {

    @Test
    void successfulLoginTest() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseModel response = given(loginRequestSpec)
                .body(authData)
                .when()
                .post("/login")
                .then()
                .spec(loginResponseSpec)
                .extract().as(LoginResponseModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }


    @Test
    void unsuccessfulLoginTest() {
        LoginBodyModel incompleteAuthData = new LoginBodyModel();
        incompleteAuthData.setEmail("eve.holt@reqres.in");

        MissingPasswordResponseModel response = given(loginRequestSpec)
                .body(incompleteAuthData)
                .when()
                .post("/login")
                .then()
                .spec(missingPasswordSpec)
                .extract().as(MissingPasswordResponseModel.class);

        assertThat(response.getError()).isEqualTo("Missing password");
    }
}
