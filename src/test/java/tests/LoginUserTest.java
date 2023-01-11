package tests;


import data.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import metods.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Тесты авторизации Пользователя")
public class LoginUserTest {
    private User user;
    private UserData userData;
    private String bearerToken;

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userData = new UserData();
        user = new User();
    }
    @After
    public void deleteUser() {
        user.deleteUser(bearerToken);
    }

    @DisplayName("Логин под существующим пользователем")
    @Test
    public void checkLoginUser() {
        user.registerUser(userData);
        String json = "{\"email\": \"" + userData.getEmail()
                + "\", \"password\": \"" + userData.getPassword()+ "\" }";

        Response response = user.loginUser(json);
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

    @DisplayName("Логин с неверным логином")
    @Test
    public void checkLoginUserWithWrongLogin() {
        Response response = user.registerUser(userData);
        bearerToken = response.then().extract().path("accessToken");

        String json = "{\"email\": \"" + "false" + userData.getEmail()
                + "\", \"password\": \"" + userData.getPassword()+ "\" }";

        user.loginUser(json)
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"))
                .statusCode(SC_UNAUTHORIZED);

    }

    @DisplayName("Логин с неверным паролем")
    @Test
    public void checkLoginUserWithWrongPassword() {
        Response response = user.registerUser(userData);
        bearerToken = response.then().extract().path("accessToken");

        String json = "{\"email\": \"" + userData.getEmail()
                + "\", \"password\": \"" + userData.getPassword()+  "_false" + "\" }";

        user.loginUser(json)
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"))
                .statusCode(SC_UNAUTHORIZED);
    }
}
