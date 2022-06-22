package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.IngredientsClient;
import site.nomoreparties.stellarburgers.client.OrdersClient;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.model.Order;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.utils.UserCredentials;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateOrdersTest {
    private OrdersClient ordersClient;
    private Order orderWithOneIngredient;
    private IngredientsClient ingredientsClient;
    private boolean ordersStatus;
    private UserClient userClient;
    private User user;
    private String userToken;
    public List<String> ingredients = new ArrayList<>();

    @Before
    public void setUp() {
        ordersClient = new OrdersClient();
        ingredientsClient = new IngredientsClient();
        user = User.getRandom();
        userClient = new UserClient();
        userClient.create(user);
        orderWithOneIngredient  = new Order();
    }

   @After
    public void tearDown() {
        int statusCode = userClient.login(UserCredentials.from(user)).extract().statusCode();
        if(statusCode == 200) {
            userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
            userClient.delete(userToken);
        }
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя: api/orders/")
    @Description("Создание заказа авторизованного пользователя")
    public void createOrdersWithAuthorizationToken() {
        ValidatableResponse ingredientsResponse = ingredientsClient.getIngredients();
        ingredients = ingredientsResponse.extract().body().path("data._id");
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        ValidatableResponse response = ordersClient.createOrders(orderWithOneIngredient.setIngredient(ingredients.get(0)), userToken);

        response.statusCode(200);
        ordersStatus = response.extract().path("success");
        assertTrue("Заказы не был создан", ordersStatus);
    }

    @Test
    @DisplayName("Создание заказа пользователя без авторизации: api/orders/")
    @Description("Создание заказа пользователя без авторизации не возможно")
    public void getUserOrdersWithoutAuthorizationToken() {
        ValidatableResponse ingredientsResponse = ingredientsClient.getIngredients();
        ingredients = ingredientsResponse.extract().body().path("data._id");
        userToken = "";
        ValidatableResponse response = ordersClient.createOrders(orderWithOneIngredient.setIngredient(ingredients.get(0)), userToken);

        response.statusCode(401);
        ordersStatus = response.extract().path("success");
        assertFalse("Заказ был создан без авторизации", ordersStatus);
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя без ингредиентов: api/orders/")
    @Description("Создание заказа авторизованного пользователя без ингредиентов не возможно")
    public void createOrdersWithAuthorizationTokenWithoutIngredients() {
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        ValidatableResponse response = ordersClient.createOrders(orderWithOneIngredient.setIngredient(null), userToken);

        response.statusCode(400);
        ordersStatus = response.extract().path("success");
        assertFalse("Заказ был создан", ordersStatus);
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя с некорректным id ингредиента: api/orders/")
    @Description("Создание заказа авторизованного пользователя с некорректным id ингредиента не возможно")
    public void createOrdersWithAuthorizationTokenWithIncorrectIngredients() {
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        ValidatableResponse response = ordersClient.createOrders(orderWithOneIngredient.createRandomIdIngredientOrder(), userToken);

        response.statusCode(400);
        ordersStatus = response.extract().path("success");
        assertFalse("Заказ был создан", ordersStatus);
    }
}
