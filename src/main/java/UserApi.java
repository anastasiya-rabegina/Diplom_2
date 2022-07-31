import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserApi {
    private static final String REGISTER = "/auth/register";
    private static final String LOGIN = "/auth/login";
    private static final String ROOT = "/auth/user";

    @Step("Register user")
    public static Response registerUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(user)
                .when()
                .post(REGISTER);
    }

    @Step("Login user")
    public static Response loginUser(UserCredentials userCredentials) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(userCredentials)
                .when()
                .post(LOGIN);
    }

    @Step("Change user data with auth")
    public static Response updateUser(User user, String token) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .and()
                .body(user)
                .when()
                .patch(ROOT);
    }

    @Step("Change user data without auth")
    public static Response updateUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(ROOT);
    }

    @Step("Delete user")
    public static void deleteUser(String token) {
        given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .when()
                .delete(ROOT);
    }
}