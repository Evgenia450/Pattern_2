package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Должен успешно войти в систему с активным зарегистрированным пользователем")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id=login] input").val(registeredUser.getLogin());
        $("[data-test-id=password] input").val(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $(".heading").shouldHave(text("Личный кабинет"));
    }

    @Test
    @DisplayName("Должно появиться сообщение об ошибке, если войти в систему с незарегистрированным пользователем")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = DataGenerator.Registration.getUser("active");
        $("[data-test-id=login] input").val(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").val(notRegisteredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка!"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Должно появиться сообщение об ошибке, если вход в систему с заблокированным зарегистрированным пользователем")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $("[data-test-id=login] input").val(blockedUser.getLogin());
        $("[data-test-id=password] input").val(blockedUser.getPassword());
        $(withText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка!"))
                .shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Должно появиться сообщение об ошибке, если войти с неправильным логином")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        $("[data-test-id=login] input").val(wrongLogin);
        $("[data-test-id=password] input").val(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка!"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Должно появиться сообщение об ошибке при входе с неправильным паролем")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        $("[data-test-id=login] input").val(registeredUser.getLogin());
        $("[data-test-id=password] input").val(wrongPassword);
        $(withText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Ошибка!"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}