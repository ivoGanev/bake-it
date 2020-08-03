package android.ivo.bake_it.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private int quantity;

    private String measure;

    private String ingredient;

    public Ingredient(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    protected Ingredient(Parcel in) {
        quantity = in.readInt();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getQuantityToString() {
        return Integer.toString(this.quantity);
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
                "\nquantity=" + quantity +
                ",\n measure='" + measure + '\'' +
                ",\n ingredient='" + ingredient + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

