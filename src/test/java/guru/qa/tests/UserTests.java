package guru.qa.tests;

import guru.qa.models.user.UserListResponseModel;
import guru.qa.models.user.UserResponseModel;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static guru.qa.specs.UserSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTests extends BaseTest {

    @Test
    void getUserListTest() {
        UserListResponseModel response = step("Make a user list request", () ->
                given(userRequestSpec)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .spec(userResponseSpec)
                        .extract().as(UserListResponseModel.class));

        step("Verify response", () -> {
            assertThat(response.getTotal()).isEqualTo(12);
            assertThat(response.getData()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(user -> user.getId() == 10)
                    .findFirst()
                    .get()
                    .getEmail()).isEqualTo("byron.fields@reqres.in");
        });
    }

    @Test
    void getSingleUserTest() {
        UserResponseModel response = step("Make a single user request", () ->
                given(userRequestSpec)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(userResponseSpec)
                        .extract().as(UserResponseModel.class));

        step("Verify response", () -> {
            assertThat(response.getData().getId()).isEqualTo(2);
            assertThat(response.getData().getEmail()).isEqualTo("janet.weaver@reqres.in");
            assertThat(response.getData().getFirst_name()).isEqualTo("Janet");
            assertThat(response.getData().getLast_name()).isEqualTo("Weaver");
            assertThat(response.getData().getAvatar()).isEqualTo("https://reqres.in/img/faces/2-image.jpg");
            assertThat(response.getSupport().getUrl()).isEqualTo("https://reqres.in/#support-heading");
        });
    }

    @Test
    void userNotFoundTest() {
        step("Send and verify 'user not found' request", () ->
                given(userRequestSpec)
                        .when()
                        .get("/users/42")
                        .then()
                        .spec(userNotFoundResponseSpec));
    }
}
