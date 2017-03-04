/**
 * Created by David on 04/03/2017.
 */
public class Ingredient {

    private final String ingredient;
    private final String image;

    public Ingredient(String ingredient, String image){

        this.ingredient = ingredient;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getIngredient() {
        return ingredient;
    }
}
