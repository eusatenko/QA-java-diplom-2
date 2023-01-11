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
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты получения списка заказов конкретного Пользователя")
public class GetOrdersUserTest {
    private String bearerToken;
    private User user;
    private UserData userData;

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

    @DisplayName("Получение заказов авторизованного пользователя")
    @Test
    public void checkGetOrdersWithAuth() {
        Response response = user.registerUser(userData);
        bearerToken = response.then().extract().path("accessToken");

        Response ordersResponse = user.getOrdersUser(bearerToken);
        ordersResponse
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("orders", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @DisplayName("Получение заказов не авторизованного пользователя")
    @Test
    public void checkGetOrdersWithoutAuth() {
        bearerToken = "no_token";

        Response ordersResponse = user.getOrdersUser(bearerToken);
        ordersResponse
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
