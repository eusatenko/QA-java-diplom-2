package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Тесты создания нового Пользователя")
public class CreateUserTest extends BaseTest{
    @DisplayName("Cоздание уникального пользователя")
    @Test
    public void checkCreateNewUser() {
        Response response = user.registerUser(userData);
        response
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(userData.getEmail()))
                .body("user.name", equalTo(userData.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .statusCode(SC_OK);
        bearerToken = response.then().extract().path("accessToken");
    }

    @DisplayName("создать пользователя, который уже зарегистрирован")
    @Test
    public void checkDoubleCreateNewUser() {
        Response response = user.registerUser(userData);
        bearerToken = response.then().extract().path("accessToken");
        user.registerUser(userData)
                .then()
                .statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @DisplayName("Cоздание уникального пользователя без email")
    @Test
    public void checkCreateNewUserWithoutLogin() {
        userData.setEmail("");
        Response response = user.registerUser(userData);
        response
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(SC_FORBIDDEN);
        bearerToken = response.then().extract().path("accessToken");
        if (bearerToken == null) {bearerToken = "noting";}
    }

    @DisplayName("Cоздание уникального пользователя без password")
    @Test
    public void checkCreateNewUserWithoutPassword() {
        userData.setPassword("");
        Response response = user.registerUser(userData);
        response
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(SC_FORBIDDEN);
        bearerToken = response.then().extract().path("accessToken");
        if (bearerToken == null) {bearerToken = "noting";}
    }
    @DisplayName("Cоздание уникального пользователя без name")
    @Test
    public void checkCreateNewUserWithoutName() {
        userData.setName("");
        Response response = user.registerUser(userData);
        response
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(SC_FORBIDDEN);
        bearerToken = response.then().extract().path("accessToken");
        if (bearerToken == null) {bearerToken = "noting";}
    }
}
