package site.nomoreparties.stellarburgers;

import org.apache.commons.lang3.RandomStringUtils;

public class Order {
    public String ingredients;

    public Order(String ingredients) {
        this.ingredients = ingredients;
    }

    public Order(){}

    public Order setIngredient(String ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public static Order createRandomIdIngredientOrder() {
        return new Order().setIngredient(RandomStringUtils.randomAlphabetic(24));
    }
}
