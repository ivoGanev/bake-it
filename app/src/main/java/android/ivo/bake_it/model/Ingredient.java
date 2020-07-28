package android.ivo.bake_it.model;

public class Ingredient
{
    private int quantity;

    private String measure;

    private String ingredient;

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public void setMeasure(String measure){
        this.measure = measure;
    }
    public String getMeasure(){
        return this.measure;
    }
    public void setIngredient(String ingredient){
        this.ingredient = ingredient;
    }
    public String getIngredient(){
        return this.ingredient;
    }
}

