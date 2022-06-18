package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.Matchers.*;


@RunWith(Parameterized.class)
public class UserParameterizedTest {
    private UserClient userClient = new UserClient();
    private final User user;
    private final int expectedStatus;
    private final String expectedErrorMessage;

    public UserParameterizedTest(User user, int expectedStatus, String expectedErrorMessage) {
        this.user = user;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getCourierData() {
        return new Object[][] {
                {User.createUserWithoutEmail(), 403, "Email, password and name are required fields"},
                {User.createUserWithoutName(), 403, "Email, password and name are required fields"},
                {User.createUserWithoutPassword(), 403, "Email, password and name are required fields"},
        };
    }

    @Test
    @DisplayName("Создание пользователя без обязательных полей: api/auth/register/")
    @Description("Создание пользователя: без email; без имени; без пароля")
    public void cannotCreateCourierWithInvalidParameters() {

        ValidatableResponse response = userClient.create(user);

        response.assertThat().statusCode(expectedStatus);
        response.assertThat().body("message", equalTo(expectedErrorMessage));
    }
}
