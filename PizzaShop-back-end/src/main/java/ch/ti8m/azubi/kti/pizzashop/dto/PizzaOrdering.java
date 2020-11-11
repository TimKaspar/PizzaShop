package ch.ti8m.azubi.kti.pizzashop.dto;

import java.util.Objects;

public class PizzaOrdering{


    private Pizza pizza;
    private Integer amount;

    public PizzaOrdering(){

    }

    public PizzaOrdering(Pizza pizza,Integer amount){
        this.pizza = pizza;
        this.amount = amount;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PizzaOrdering{" +
                "pizza=" + pizza +
                ", amount=" + amount +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (pizza.getId() == null) {
            return false;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PizzaOrdering pizzaOrdering = (PizzaOrdering) o;
        return Objects.equals(amount, pizzaOrdering.amount) && Objects.equals(pizza, pizzaOrdering.pizza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), amount, pizza);
    }

}

