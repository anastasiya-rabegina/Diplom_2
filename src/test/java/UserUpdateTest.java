import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserUpdateTest extends BaseTest{

    private User user;
    private String token;

    private final String errorNoAuth = "You should be authorised";

    @Before
    public void initUser() {
        user = user.getRandomUser();
        token = UserApi.registerUser(user)
                .then()
                .extract().body().path("accessToken");
    }

    @After
    public void finishUser() {
        UserApi.deleteUser(token);
    }

    @DisplayName("Check status code and body when user updates name successfully")
    @Test
    public void userUpdateNameWithCorrectDataAndAuthIsSuccessful() {
        user.setName(User.faker.name().firstName());
        UserApi.updateUser(user, token)
                .then()
                .statusCode(200)
                .assertThat().body("user.name", equalTo(user.getName()));
    }

    @DisplayName("Check status code and body when user updates email successfully")
    @Test
    public void userUpdateEmailWithCorrectDataAndAuthIsSuccessful() {
        user.setEmail(User.faker.internet().safeEmailAddress());
        UserApi.updateUser(user, token)
                .then()
                .statusCode(200)
                .assertThat().body("user.email", equalTo(user.getEmail()));
    }

    @DisplayName("Check status code when user updates password successfully")
    @Test
    public void userUpdatePassWithCorrectDataAndAuthIsSuccessful() {
        user.setPassword(User.faker.internet().password(6,7));
        UserApi.updateUser(user, token)
                .then()
                .statusCode(200);
        UserApi.loginUser(UserCredentials.from(user))
                .then()
                .statusCode(200);
    }

@DisplayName("Check status code and body when non-authorized user updates name")
@Test
    public void userUpdateNameWithCorrectDataAndWithoutAuthIsNotSuccessful() {
        user.setName(User.faker.name().firstName());
        UserApi.updateUser(user)
                .then()
                .statusCode(401)
                .assertThat().body("message", equalTo(errorNoAuth));
    }

    @DisplayName("Check status code and body when non-authorized user updates email")
    @Test
    public void userUpdateEmailWithCorrectDataAndWithoutAuthIsNotSuccessful() {
        user.setEmail(User.faker.internet().safeEmailAddress());
        UserApi.updateUser(user)
                .then()
                .statusCode(401)
                .assertThat().body("message", equalTo(errorNoAuth));
    }

    @DisplayName("Check status code and body when non-authorized user updates password")
    @Test
    public void userUpdatePassWithCorrectDataAndWithoutAuthIsNotSuccessful() {
        user.setPassword(User.faker.internet().password(6, 7));
        UserApi.updateUser(user)
                .then()
                .statusCode(401)
                .assertThat().body("message", equalTo(errorNoAuth));
    }
}