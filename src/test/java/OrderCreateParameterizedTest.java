import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreateParameterizedTest extends BaseTest {

    private final int statusCode;
    private final List<String> ingredients;

    private static final String ingredientSauceHash = "61c0c5a71d1f82001bdaaa75";
    private static final String ingredientBunHash = "61c0c5a71d1f82001bdaaa6c";
    private static final String ingredientFakeHash = "123qwe71d1f82001bdaaa6c";
    private final String tokenEmpty = "";


    private User user;
    private String token;
    private Order order;

    public OrderCreateParameterizedTest(int statusCode, List<String> ingredients) {
        this.statusCode = statusCode;
        this.ingredients = ingredients;
    }

    @Parameterized.Parameters(name = "Test data - status code: {0}, ingredients: {1}")
    public static Object[][] getData() {
        return new Object[][] {
                {400, null},
                {200, Arrays.asList(ingredientBunHash, ingredientSauceHash)},
                {500, Arrays.asList(ingredientFakeHash)},
        };
    }

    @Before
    public void initUser() {
        user = user.getRandomUser();
        token = UserApi.registerUser(user)
                .then()
                .extract().body().path("accessToken");
        order = new Order(ingredients);
    }

    @After
    public void finishUser() {
        UserApi.deleteUser(token);
    }

//    @DisplayName("Check status code when authorized user creates an order")
    @Test
    public void createOrderWithAuth() {
        OrderApi.createOrder(order, token)
                .then()
                .statusCode(statusCode);
    }

    //    @DisplayName("Check status code when non-authorized user creates an order")
    @Test
    public void createOrderWithoutAuth() {
        OrderApi.createOrder(order, tokenEmpty)
                .then()
                .statusCode(statusCode);
    }
}