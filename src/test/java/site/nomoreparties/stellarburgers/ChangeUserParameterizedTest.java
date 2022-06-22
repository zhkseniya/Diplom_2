package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.utils.ChangeUserCredentials;
import site.nomoreparties.stellarburgers.utils.UserCredentials;

@RunWith(Parameterized.class)
public class ChangeUserParameterizedTest {
    private static UserClient userClient = new UserClient();
    private static User user = User.getRandom();
    private String userToken;
    private final ChangeUserCredentials changeUserCredentials;
    private final int expectedStatus;
    private final boolean expectedChangeStatus;

    @After
    public void tearDown() {
        userClient.delete(userToken);
    }

    public ChangeUserParameterizedTest(ChangeUserCredentials changeUserCredentials, int expectedStatus, boolean expectedChangeStatus) {
        this.changeUserCredentials = changeUserCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedChangeStatus = expectedChangeStatus;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}. status: {1}. success: {2}")
    public static Object[][] getChangeUserLoginData() {
        return new Object[][] {
                {ChangeUserCredentials.changeUserEmailOnly(user), 200, true},
                {ChangeUserCredentials.changeUserPasswordOnly(user), 200, true},
                {ChangeUserCredentials.changeUserNameOnly(user), 200, true},
                {ChangeUserCredentials.changeUser(user), 200, true},
        };
    }

    @Test
    @DisplayName("Изменение данных пользователя: api/auth/user/")
    @Description("Изменение данных пользователя: изменение email; изменение пароля; изменение имени; изменение всех данных одновременно")
    public void successChangeUserData() {
        userClient.create(user);
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");

        ValidatableResponse response = userClient.change(changeUserCredentials, userToken);

        response.assertThat().statusCode(expectedStatus);
        response.extract().path("success").equals(expectedChangeStatus);
    }
}
