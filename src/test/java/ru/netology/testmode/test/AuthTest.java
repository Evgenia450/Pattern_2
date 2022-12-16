package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id=login] input").val(registeredUser.getLogin());
        $("[data-test-id=password] input").val(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        String actual = $(".heading").getText().trim();
        String expected = "Личный кабинет";
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = DataGenerator.Registration.getUser("active");
        $("[data-test-id=login] input").val(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").val(notRegisteredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__title").should(appear);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $("[data-test-id=login] input").val(blockedUser.getLogin());
        $("[data-test-id=password] input").val(blockedUser.getPassword());
        $(withText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__title").should(appear);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  логином, для заполнения поля формы "Логин" используйте переменную wrongLogin,
        //  "Пароль" - пользователя registeredUser
        $("[data-test-id=login] input").val(wrongLogin);
        $("[data-test-id=password] input").val(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__title").should(appear);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  паролем, для заполнения поля формы "Логин" используйте пользователя registeredUser,
        //  "Пароль" - переменную wrongPassword
        $("[data-test-id=login] input").val(registeredUser.getLogin());
        $("[data-test-id=password] input").val(wrongPassword);
        $(withText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__title").should(appear);
    }
}