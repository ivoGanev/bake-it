package android.ivo.bake_it.model;

public class Ingredient {
    private int quantity;

    private String measure;

    private String ingredient;

    public Ingredient(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getMeasure() {
        return this.measure;
    }

    public String getIngredient() {
        return this.ingredient;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }
}

