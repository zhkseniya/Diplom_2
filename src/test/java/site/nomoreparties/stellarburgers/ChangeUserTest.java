package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import static org.junit.Assert.assertFalse;

public class ChangeUserTest {
    private UserClient userClient;
    private User user;
    private String userToken;
    private boolean userStatus;


    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        userClient.delete(userToken);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации пользователя: api/auth/user/")
    @Description("Изменение данных пользователя без авторизации не возможно")
    public void changeUserWithoutAuthorizationToken() {
        userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");

        ValidatableResponse response = userClient.change(ChangeUserCredentials.changeUser(user), " ");

        response.statusCode(401);
        userStatus = response.extract().path("success");
        assertFalse("Данные были изменены без авторизации", userStatus);
    }
}
