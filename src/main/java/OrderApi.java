import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderApi {

    private static final String INGREDIENTS = "/ingredients";
    private static final String ROOT = "/orders";

    @Step("Create order")
    public static Response createOrder(Order order, String token) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(ROOT);
    }

    @Step("Get user orders with a token")
    public static Response getOrders(String token) {
        return given()
                .header("Authorization", token)
                .and()
                .when()
                .get(ROOT);
    }

    @Step("Get user orders without auth")
    public static Response getOrders() {
        return given()
                .and()
                .when()
                .get(ROOT);
    }
}