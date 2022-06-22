package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.utils.UserCredentials;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.hamcrest.Matchers.*;

public class UserCreateTest {
    private UserClient userClient;
    private User user;
    private boolean userStatus;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandom();
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
    @DisplayName("Регистрация пользователя: api/auth/register/")
    @Description("Регистрация уникального пользователя с валидными данными: email, name, password")
    public void userCanBeCreatedWithValidData() {
        ValidatableResponse userCreateResponse = userClient.create(user);

        userCreateResponse.statusCode(200);
        userStatus = userCreateResponse.extract().path("success");
        assertTrue("Пользователь не был создан", userStatus);
    }

    @Test
    @DisplayName("Регистрация пользователя, который уже был зарегистрирован ранее: api/auth/register/")
    @Description("Нет возможности зарегистрировать одного и того же пользователя два раза")
    public void cannotCreateTwoIdenticalUsers() {
        userClient.create(user);

        ValidatableResponse secondUserCreateResponse = userClient.create(user);

        secondUserCreateResponse.statusCode(403);
        userStatus = secondUserCreateResponse.extract().path("success");
        assertFalse("Пользователь создан повторно", userStatus);
        secondUserCreateResponse.assertThat().body("message", equalTo("User already exists"));
    }
}
