package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LoginUserTest {
    private UserClient userClient;
    private User user;
    private boolean userStatus;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        int statusCode = userClient.login(UserCredentials.from(user)).extract().statusCode();
        if(statusCode == 200) {
            String userToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
            userClient.delete(userToken);
        }
    }

    @Test
    @DisplayName("Авторизация пользователя: api/auth/login/")
    @Description("Авторизация пользователя с валидными данными: email, password")
    public void userCanBeLoginWithValidData() {
        ValidatableResponse userLoginResponse = userClient.login(UserCredentials.from(user));

        userLoginResponse.statusCode(200);
        userStatus = userLoginResponse.extract().path("success");
        assertTrue("Пользователь не был авторизован", userStatus);
    }
}
