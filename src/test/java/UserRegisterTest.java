import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserRegisterTest extends BaseTest{

    private User user;

    private final String expectedError = "Email, password and name are required fields";

    @Before
    public void initUser() {
        user = user.getRandomUser();
    }

    @DisplayName("Check status code and body when user registers successfully")
    @Test
    public void userRegisterWithCorrectDataIsSuccessful() {
        String token = UserApi.registerUser(user)
                .then()
                .statusCode(200)
                .assertThat().body("user.email", equalTo(user.getEmail()))
                .extract().body().path("accessToken");
        UserApi.deleteUser(token);
    }

    @DisplayName("Check status code and body when user registers again")
    @Test
    public void userCannotRegisterTwice() {
        UserApi.registerUser(user);
        UserApi.registerUser(user)
                .then()
                .statusCode(403)
                .assertThat().body("message", equalTo("User already exists"));
    }

    @DisplayName("Check status code and body when user registers without email")
    @Test
    public void userCannotRegisterWithoutEmail() {
        user.setEmail("");
        UserApi.registerUser(user)
                .then()
                .statusCode(403)
                .assertThat().body("message", equalTo(expectedError));
    }

    @DisplayName("Check status code and body when user registers without password")
    @Test
    public void userCannotRegisterWithoutPassword() {
        user.setPassword("");
        UserApi.registerUser(user)
                .then()
                .statusCode(403)
                .assertThat().body("message", equalTo(expectedError));
    }

    @DisplayName("Check status code and body when user registers without name")
    @Test
    public void userCannotRegisterWithoutName() {
        user.setName("");
        UserApi.registerUser(user)
                .then()
                .statusCode(403)
                .assertThat().body("message", equalTo(expectedError));
    }
}