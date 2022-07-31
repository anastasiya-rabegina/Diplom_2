import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OrderCreateTest extends BaseTest{

    private User user;
    private String token;
    private List<String> ingredients;

    private final String ingredientSauceHash = "61c0c5a71d1f82001bdaaa75";
    private final String ingredientBunHash = "61c0c5a71d1f82001bdaaa6c";

    private final String error404 = "Ingredient ids must be provided";
    private final String tokenEmpty = "";

    @Before
    public void initUser() {
        user = user.getRandomUser();
        token = UserApi.registerUser(user)
                .then()
                .extract().body().path("accessToken");
        ingredients = new ArrayList<>();
        ingredients.add(ingredientSauceHash);
        ingredients.add(ingredientBunHash);
    }

    @After
    public void finishUser() {
        UserApi.deleteUser(token);
    }

    @DisplayName("Check status code and body when authorized user creates an order")
    @Test
    public void createOrderWithAuthAndIngredients() {
        Order order = new Order(ingredients);
        String actualResponse = OrderApi.createOrder(order, token)
                .then()
                .statusCode(200)
                .extract().body().asString();
        assertTrue((actualResponse.contains(ingredientSauceHash)) && (actualResponse.contains(ingredientBunHash)));
    }

    @DisplayName("Check status code and body when non-authorized user creates an order")
    @Test
    public void createOrderWithoutAuthAndWithIngredients() {
        Order order = new Order(ingredients);
        String actualResponse = OrderApi.createOrder(order, tokenEmpty)
                .then()
                .statusCode(200)
                .extract().body().path("order").toString();
        assertFalse((actualResponse.isEmpty()));
    }

    @DisplayName("Check status code and body when authorized user creates an order without ingredients")
    @Test
    public void createOrderWithAuthAndNoIngredients() {
        List<String> ingredients = new ArrayList<>();

        Order order = new Order(ingredients);
        OrderApi.createOrder(order, token)
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo(error404));
    }

    @DisplayName("Check status code and body when non-authorized user creates an order without ingredients")
    @Test
    public void createOrderWithoutAuthAndNoIngredients() {
        List<String> ingredients = new ArrayList<>();

        Order order = new Order(ingredients);
        OrderApi.createOrder(order, tokenEmpty)
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo(error404));
    }
}