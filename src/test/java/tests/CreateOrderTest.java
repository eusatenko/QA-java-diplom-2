package tests;

import data.Ingredients;
import data.deserializationIngredients.Data;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import metods.Order;
import org.junit.Test;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Тесты создания заказа")
public class CreateOrderTest extends BaseTest{
    @DisplayName("Создание заказа с авторизацией с ингридиентами")
    @Test
    public void checkCreateOrderWithAuthAndIngredients() {
        Response response = user.registerUser(userData);
        bearerToken = response.then().extract().path("accessToken");

        Order order = new Order();
        Ingredients ingredients = new Ingredients().getIngredients();
        // получаем объект ингридиентов
        List<Data> ingredientsData = ingredients.getData();
        // формируем JSON из ID первых двух ингридиентов
        String ingredientsJson = "{" + "\"ingredients\": " + "[\"" +
                ingredientsData.get(0).get_id() + "\"" + ","
                + "\"" + ingredientsData.get(1).get_id() + "\"]}";
        // проверяем ответ создания заказа
        Response responseOrder = order.createOrder(bearerToken, ingredientsJson);
        responseOrder
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @DisplayName("Создание заказа с авторизацией без ингридиентов")
    @Test
    public void checkCreateOrderWithAuthAndWithoutIngredients() {
        Response response = user.registerUser(userData);
        bearerToken = response.then().extract().path("accessToken");

        Order order = new Order();
        // формируем JSON без ингридиентов
        String ingredientsJson = "{" + "\"ingredients\": " + "[]}";
        // проверяем ответ создания заказа
        Response responseOrder = order.createOrder(bearerToken, ingredientsJson);
        responseOrder
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @DisplayName("Создание заказа с авторизацией с невалидным хеш ингридиентов")
    @Test
    public void checkCreateOrderWithAuthAndWrongHashIngredients() {
        Response response = user.registerUser(userData);
        bearerToken = response.then().extract().path("accessToken");

        Order order = new Order();
        Ingredients ingredients = new Ingredients().getIngredients();
        // получаем объект ингридиентов
        List<Data> ingredientsData = ingredients.getData();
        // формируем JSON из ID первых двух ингридиентов
        String ingredientsJson = "{" + "\"ingredients\": " + "[\"" +
                ingredientsData.get(0).get_id() + "_wrongHash" + "\"" + ","
                + "\"" + ingredientsData.get(1).get_id() + "_wrongHash" + "\"]}";
        // проверяем ответ создания заказа
        Response responseOrder = order.createOrder(bearerToken, ingredientsJson);
        responseOrder
                .then()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @DisplayName("Создание заказа без авторизации с ингридиентами")
    @Test
    public void checkCreateOrderWithoutAuthAndWithIngredients() {
        bearerToken = "no_token";

        Order order = new Order();
        Ingredients ingredients = new Ingredients().getIngredients();
        List<Data> ingredientsData = ingredients.getData();
        // формируем JSON из ID первых двух ингридиентов
        String ingredientsJson = "{" + "\"ingredients\": " + "[\"" +
                ingredientsData.get(0).get_id() + "\"" + ","
                + "\"" + ingredientsData.get(1).get_id() + "\"]}";
        // проверяем ответ создания заказа
        Response responseOrder = order.createOrder(bearerToken, ingredientsJson);
        responseOrder
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @DisplayName("Создание заказа без авторизации без ингридиентов")
    @Test
    public void checkCreateOrderWithoutAuthAndWithoutIngredients() {
        bearerToken = "no_token";

        Order order = new Order();
        // формируем JSON без ингридиентов
        String ingredientsJson = "{" + "\"ingredients\": " + "[]}";
        // проверяем ответ создания заказа
        Response responseOrder = order.createOrder(bearerToken, ingredientsJson);
        responseOrder
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @DisplayName("Создание заказа без авторизации с невалидным хеш ингридиентов")
    @Test
    public void checkCreateOrderWithoutAuthAndWrongHashIngredients() {
        bearerToken = "no_token";

        Order order = new Order();
        Ingredients ingredients = new Ingredients().getIngredients();
        // получаем объект ингридиентов
        List<Data> ingredientsData = ingredients.getData();
        // формируем JSON из ID первых двух ингридиентов
        String ingredientsJson = "{" + "\"ingredients\": " + "[\"" +
                ingredientsData.get(0).get_id() + "_wrongHash" + "\"" + ","
                + "\"" + ingredientsData.get(1).get_id() + "_wrongHash" + "\"]}";
        // проверяем ответ создания заказа
        Response responseOrder = order.createOrder(bearerToken, ingredientsJson);
        responseOrder
                .then()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
