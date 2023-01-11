package tests;

import data.UserData;
import io.qameta.allure.Step;
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

@DisplayName("Тесты изменения данных пользователя")
public class UpdateUserTest {
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

    @DisplayName("Изменение данных пользователя с авторизацией")
    @Test
    public void checkUpdateUserWithAuth() {
        Response response = user.registerUser(userData);
        bearerToken = response.then().extract().path("accessToken");

        userData.setEmail("different_mail_" + userData.getEmail());
        userData.setName("different_name_" + userData.getName());
        userData.setPassword("different_password_" + userData.getPassword());
        Response updateUser = user.updateUser(bearerToken, userData);
        updateUser
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(userData.getEmail()))
                .body("user.name", equalTo(userData.getName()))
                .statusCode(SC_OK);
    }

    @DisplayName("Изменение данных пользователя без авторизации")
    @Test
    public void checkUpdateUserWithoutAuth() {
        Response response = user.registerUser(userData);
        bearerToken = response.then().extract().path("accessToken");

        userData.setEmail("different_mail" + userData.getEmail());
        userData.setName("different_name" + userData.getName());
        userData.setPassword("different_password" + userData.getPassword());

        user.updateUser("no_token", userData)
                .then()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(SC_UNAUTHORIZED);
    }
}
