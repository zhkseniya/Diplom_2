package site.nomoreparties.stellarburgers.utils;

import org.apache.commons.lang3.RandomStringUtils;
import site.nomoreparties.stellarburgers.model.User;

public class ChangeUserCredentials {
    public static final String EMAIL_POSTFIX = "@yandex.ru";
    public String email;
    public String password;
    public String name;

    public ChangeUserCredentials(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public ChangeUserCredentials() {}

    public static ChangeUserCredentials from(User user) {
        return new ChangeUserCredentials(user.email, user.password, user.name);
    }

    public ChangeUserCredentials setEmail(String email) {
        this.email = email;
        return this;
    }

    public ChangeUserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public ChangeUserCredentials setName(String name) {
        this.name = name;
        return this;
    }

    public static ChangeUserCredentials changeUserEmailOnly(User user) {
        return new ChangeUserCredentials().setEmail(RandomStringUtils.randomAlphabetic(5) + EMAIL_POSTFIX).setPassword(user.password).setName(user.name);
    }

    public static ChangeUserCredentials changeUserPasswordOnly(User user) {
        return new ChangeUserCredentials().setPassword(RandomStringUtils.randomAlphabetic(10)).setEmail(user.email).setName(user.name);
    }

    public static ChangeUserCredentials changeUserNameOnly(User user) {
        return new ChangeUserCredentials().setName(RandomStringUtils.randomAlphabetic(10)).setEmail(user.email).setPassword(user.password);
    }

    public static ChangeUserCredentials changeUser(User user) {
        return new ChangeUserCredentials().setEmail(RandomStringUtils.randomAlphabetic(5) + EMAIL_POSTFIX).setPassword(RandomStringUtils.randomAlphabetic(10)).setName(RandomStringUtils.randomAlphabetic(10));
    }
}
