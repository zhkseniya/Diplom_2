package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class LoginParameterizedTest {
    private static UserClient userClient = new UserClient();
    private static User user = User.getRandom();
    private final UserCredentials userCredentials;
    private final int expectedStatus;
    private final String expectedErrorMessage;

    public LoginParameterizedTest(UserCredentials userCredentials, int expectedStatus, String expectedErrorMessage) {
        this.userCredentials = userCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getCourierLoginData() {
        return new Object[][] {
                {UserCredentials.getWithRandomEmail(user), 401, "email or password are incorrect"},
                {UserCredentials.getWithRandomPassword(user), 401, "email or password are incorrect"},
                {UserCredentials.getWithEmailOnly(user), 401, "email or password are incorrect"},
                {UserCredentials.getWithPasswordOnly(user), 401, "email or password are incorrect"},
        };
    }

    @Test
    @DisplayName("Авторизация пользователя с невалидными данными / без обязательных полей: api/auth/login/")
    @Description("Авторизация пользователя с невалидными данными / без обязательных полей: невалидный email; невалидный пароль; без почты; без пароля")
    public void invalidRequestInNotAllowed() {
        userClient.create(user);

        ValidatableResponse response = userClient.login(userCredentials);

        response.assertThat().statusCode(expectedStatus);
        response.assertThat().body("message", equalTo(expectedErrorMessage));
    }
}
