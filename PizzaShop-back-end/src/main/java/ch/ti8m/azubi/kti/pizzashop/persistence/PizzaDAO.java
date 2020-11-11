package ch.ti8m.azubi.kti.pizzashop.persistence;

import ch.ti8m.azubi.kti.pizzashop.dto.Pizza;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PizzaDAO {

    private Connection connection;

    public PizzaDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Pizza> list() throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from pizza");
        ResultSet resultSet = statement.executeQuery();
        List<Pizza> pizzaList = new LinkedList<>();
        while (resultSet.next()) {
            pizzaList.add(new Pizza(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("price")));
        }
        return pizzaList;
    }

    public Pizza get(int id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from pizza where id =" + id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Pizza(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("price"));
        }
        return null;
    }

    public Pizza create(Pizza pizza) throws Exception {
        validatePizza(pizza);
        PreparedStatement statement = connection.prepareStatement("insert into pizza (name, price) values (?,?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, pizza.getName());
        statement.setDouble(2, pizza.getPrice());
        statement.executeUpdate();

        ResultSet generatedKeys = statement.getGeneratedKeys();
        generatedKeys.next();
        pizza.setId(generatedKeys.getInt(1));
        return pizza;
    }

    public void update(Pizza pizza) throws Exception {
        validatePizza(pizza);
        doesPizzaExist(pizza);
        PreparedStatement statement = connection.prepareStatement("update pizza set name=(?), price=(?) where id =" + pizza.getId());
        statement.setString(1, pizza.getName());
        statement.setDouble(2, pizza.getPrice());
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated == 0) {
            throw new Exception("Updated failed, no entity with that id exists");
        }
    }

    public boolean delete(int id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("delete from pizza where id=" + id);
        int numberOfDeletedRecords = statement.executeUpdate();
        return numberOfDeletedRecords > 0;
    }

    public void save(Pizza pizza) throws Exception {
        if (pizza.getId() == null) {
            create(pizza);
        } else {
            update(pizza);
        }
    }

    public void validatePizza(Pizza pizza) {
        if (pizza.getName() == null) {
            throw new IllegalArgumentException("Name is required");
        }
        if (pizza.getPrice() == null) {
            throw new IllegalArgumentException("Price is required");
        }
    }

    public boolean doesPizzaExist(Pizza pizza) throws Exception {
        if (pizza.getId() == null || get(pizza.getId()) == null) {
            throw new IllegalArgumentException("Pizza doesnt exist");
        }
        return true;
    }
}
