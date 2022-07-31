import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest extends BaseTest{

    private User user;
    private UserCredentials userCredentials;
    private String token;

    private final String expectedError = "email or password are incorrect";

    @Before
    public void initUser() {
        user = user.getRandomUser();
        token = UserApi.registerUser(user)
                .then()
                .extract().body().path("accessToken");;
        userCredentials = UserCredentials.from(user);
    }

    @After
    public void finishUser() {
        UserApi.deleteUser(token);
    }

    @DisplayName("Check status code and body when user logins successfully")
    @Test
    public void userLoginWithCorrectDataIsSuccessful() {
        UserApi.loginUser(userCredentials)
                .then()
                .statusCode(200)
                .assertThat().body("user.email", equalTo(userCredentials.getEmail()))
                .assertThat().body("accessToken", notNullValue());
    }

    @DisplayName("Check status code and body when user logins without email")
    @Test
    public void userCannotLoginWithoutEmail() {
        userCredentials.setEmail(null);
        UserApi.loginUser(userCredentials)
                .then()
                .statusCode(401)
                .assertThat().body("message", equalTo(expectedError));
    }

    @DisplayName("Check status code and body when user logins without password")
    @Test
    public void userCannotLoginWithoutPassword() {
        userCredentials.setPassword(null);
        UserApi.loginUser(userCredentials)
                .then()
                .statusCode(401)
                .assertThat().body("message", equalTo(expectedError));
    }

    @DisplayName("Check status code and body when user logins with invalid credentials")
    @Test
    public void userCannotLoginWithIncorrectCredentials() {
        userCredentials.setPassword(User.faker.internet().safeEmailAddress());
        UserApi.loginUser(userCredentials)
                .then()
                .statusCode(401)
                .assertThat().body("message", equalTo(expectedError));
    }
}