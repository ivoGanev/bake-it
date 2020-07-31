package android.ivo.bake_it.model;

import java.util.List;

public class Recipe {
    private final int id;

    private final String name;

    private final List<Ingredient> ingredients;

    private final List<Step> steps;

    private final int servings;


    public Recipe(Builder builder)
    {
        this.id = builder.id;
        this.name = builder.name;
        this.ingredients = builder.ingredients;
        this.steps = builder.steps;
        this.servings = builder.servings;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public List<Step> getSteps() {
        return this.steps;
    }

    public int getServings() {
        return this.servings;
    }

    public static class Builder {
        private int id;

        private String name;

        private List<Ingredient> ingredients;

        private List<Step> steps;

        private int servings;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name)
        {
            this.name = name;
            return this;
        }

        public Builder ingredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public Builder steps(List<Step> steps) {
            this.steps = steps;
            return this;
        }

        public Builder servings(int servings) {
            this.servings = servings;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", servings=" + servings +
                '}';
    }
}
