import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class OrderGetTest extends BaseTest{

    private User user;
    private String token;
    private String errorString = "You should be authorised";
    private Order order;

    private List<String> ingredients;

    private final String ingredientSauceHash = "61c0c5a71d1f82001bdaaa75";
    private final String ingredientBunHash = "61c0c5a71d1f82001bdaaa6c";
    private final String ingredientFakeHash = "123qwe71d1f82001bdaaa6c";


    @Before
    public void initUser() {
        user = user.getRandomUser();
        token = UserApi.registerUser(user)
                .then()
                .extract().body().path("accessToken");
        ingredients = new ArrayList<>();
        ingredients.add(ingredientSauceHash);
        ingredients.add(ingredientBunHash);
        order = new Order(ingredients);

    }

    @After
    public void finishUser() {
        UserApi.deleteUser(token);
    }

    @DisplayName("Check status code and body when authorized user requests orders")
    @Test
    public void getUserOrdersWithToken() {
        OrderApi.createOrder(order, token)
                .then()
                .statusCode(200);

        String actualResponse = OrderApi.getOrders(token)
                .then()
                .statusCode(200)
                .extract().body().asString();
        assertTrue((actualResponse.contains(ingredientSauceHash)) && (actualResponse.contains(ingredientBunHash)));
    }

    @DisplayName("Check status code and body when non-authorized user requests orders")
    @Test
    public void getUserOrdersWithoutToken() {
        OrderApi.createOrder(order, token)
                .then()
                .statusCode(200);

        OrderApi.getOrders()
                .then()
                .statusCode(401)
                .assertThat().body("message", equalTo(errorString));
    }
}