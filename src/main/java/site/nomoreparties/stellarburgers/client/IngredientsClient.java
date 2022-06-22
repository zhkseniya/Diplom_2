package site.nomoreparties.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class IngredientsClient extends RestAssuredClient {
    private static final String INGREDIENTS_PATH = "api/ingredients";

    @Step("Send GET request to api/ingredients")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH)
                .then();
    }
}
