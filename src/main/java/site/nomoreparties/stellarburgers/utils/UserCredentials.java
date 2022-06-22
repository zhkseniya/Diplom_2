package site.nomoreparties.stellarburgers.utils;

import org.apache.commons.lang3.RandomStringUtils;
import site.nomoreparties.stellarburgers.model.User;

public class UserCredentials {
    public static final String EMAIL_POSTFIX = "@yandex.ru";
    public String email;
    public String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCredentials() {}

    public static UserCredentials from(User user) {
        return new UserCredentials(user.email, user.password);
    }

    public UserCredentials setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public static UserCredentials getWithRandomEmail(User user) {
        return new UserCredentials().setEmail(RandomStringUtils.randomAlphabetic(5) + EMAIL_POSTFIX).setPassword(user.password);
    }

    public static UserCredentials getWithRandomPassword(User user) {
        return new UserCredentials().setPassword(RandomStringUtils.randomAlphabetic(10)).setEmail(user.email);
    }

    public static UserCredentials getWithEmailOnly(User user) {
        return new UserCredentials().setEmail(user.email);
    }

    public static UserCredentials getWithPasswordOnly(User user) {
        return new UserCredentials().setPassword(user.password);
    }
}
