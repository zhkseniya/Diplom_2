package site.nomoreparties.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.model.Order;
import static io.restassured.RestAssured.given;

public class OrdersClient extends RestAssuredClient {
    private static final String ORDERS_PATH = "api/orders";

    @Step("Send POST request to api/orders")
    public ValidatableResponse createOrders(Order ingredients, String userToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", userToken)
                .body(ingredients)
                .when()
                .post(ORDERS_PATH)
                .then();
    }

    @Step("Send GET request to api/orders")
    public ValidatableResponse getUserOrders(String userToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", userToken)
                .when()
                .get(ORDERS_PATH)
                .then();
    }
}
