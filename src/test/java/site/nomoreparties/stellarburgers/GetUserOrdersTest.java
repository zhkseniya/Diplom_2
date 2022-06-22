package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.OrdersClient;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.utils.UserCredentials;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GetUserOrdersTest {
    private OrdersClient ordersClient;
    private boolean ordersStatus;
    private UserClient userClient;
    private User user;
    private String userToken;

    @Before
    public void setUp() {
        ordersClient = new OrdersClient();
        user = User.getRandom();
        userClient = new UserClient();
        userClient.create(user);
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
    @DisplayName("Получение заказов без авторизации пользователя: api/orders/")
    @Description("Получение заказов без авторизации пользователя не возможно")
    public void getUserOrdersWithoutAuthorizationToken() {
        ValidatableResponse response = ordersClient.getUserOrders(" ");

        response.statusCode(401);
        ordersStatus = response.extract().path("success");
        assertFalse("Заказы были получены без авторизации", ordersStatus);
    }

    @Test
    @DisplayName("Получение заказов пользователя: api/orders/")
    @Description("Получение заказов авторизованного пользователя")
    public void getUserOrdersWithAuthorizationToken() {
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");

        ValidatableResponse response = ordersClient.getUserOrders(userToken);

        response.statusCode(200);
        ordersStatus = response.extract().path("success");
        assertTrue("Заказы не были получены", ordersStatus);
    }
}
