package android.ivo.bake_it.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {
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

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        servings = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        Ingredient[] ingredientsArray = new Ingredient[ingredients.size()];
        dest.writeParcelableArray(ingredients.toArray(ingredientsArray), 0);
        Step[] stepsArray = new Step[steps.size()];
        dest.writeParcelableArray(steps.toArray(stepsArray), 0);
        dest.writeInt(servings);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
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
