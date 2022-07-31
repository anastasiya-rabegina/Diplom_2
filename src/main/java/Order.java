import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Order {
    private List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}