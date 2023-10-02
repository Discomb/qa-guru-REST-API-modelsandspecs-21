package guru.qa.tests;

import guru.qa.models.common.MissingPasswordResponseModel;
import guru.qa.models.registration.RegistrationBodyModel;
import guru.qa.models.registration.RegistrationResponseModel;
import org.junit.jupiter.api.Test;

import static guru.qa.specs.RegistrationSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class RegisterTests extends BaseTest {

    @Test
    void successfulRegistrationTest() {

        RegistrationBodyModel regData = new RegistrationBodyModel();
        regData.setEmail("eve.holt@reqres.in");
        regData.setPassword("pistol");

        RegistrationResponseModel response = step("Make a registration request", () ->
                given(registrationRequestSpec)
                        .body(regData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(registrationResponseSpec)
                        .extract().as(RegistrationResponseModel.class));

        step("Verify response", () -> {
            assertThat(response.getId()).isEqualTo(4);
            assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
        });
    }

    @Test
    void unsuccessfulRegistrationTest() {
        RegistrationBodyModel regData = new RegistrationBodyModel();
        regData.setEmail("eve.holt@reqres.in");

        MissingPasswordResponseModel response = step("Make a registration request without password", () ->
                given(registrationRequestSpec)
                        .body(regData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(missingPasswordRegistrationSpec)
                        .extract().as(MissingPasswordResponseModel.class));

        step("Verify response", () ->
            assertThat(response.getError()).isEqualTo("Missing password"));
    }
}
