package ch.ti8m.azubi.kti.pizzashop.persistence;

import ch.ti8m.azubi.kti.pizzashop.dto.Order;
import ch.ti8m.azubi.kti.pizzashop.dto.Pizza;
import ch.ti8m.azubi.kti.pizzashop.dto.PizzaOrdering;
import ch.ti8m.azubi.kti.pizzashop.service.PizzaService;
import ch.ti8m.azubi.kti.pizzashop.service.PizzaServiceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class OrderDAO {

    private Connection connection;
    private PizzaDAO pizzaDAO;

    public OrderDAO(Connection connection) {
        this.connection = connection;
        pizzaDAO = new PizzaDAO(connection);
    }


    public List<Order> list() throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from ordering");
        ResultSet resultSet = statement.executeQuery();
        List<Order> list = new LinkedList<>();
        while (resultSet.next()) {
            list.add(new Order(resultSet.getInt("id"), getPizzaOrders(resultSet.getInt("id")), resultSet.getDate("date"), resultSet.getString("phone"), resultSet.getString("address")));
        }
        return list;
    }

    public Order get(int id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select id, date, phone, address from ordering where id =" + id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            // Order found
            return new Order(resultSet.getInt("id"), getPizzaOrders(resultSet.getInt("id")), resultSet.getDate("date"), resultSet.getString("phone"), resultSet.getString("address"));
        }
        //no order found
        return null;
    }

    public List<PizzaOrdering> getPizzaOrderings(Order order) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select pizza_id, amount from pizzaordering where ordering_id =" + order.getId());
        ResultSet resultSet = statement.executeQuery();
        List<PizzaOrdering> pizzaOrderings = new LinkedList<>();
        PizzaService pizzaService = new PizzaServiceImpl();
        PizzaDAO pizzaDAO = new PizzaDAO(connection);
        while (resultSet.next()) {
            // pizzaOrdering found
            Pizza pizza = pizzaDAO.get(resultSet.getInt("pizza_id"));
            PizzaOrdering pizzaOrdering = new PizzaOrdering(pizza, resultSet.getInt("amount"));
            pizzaOrderings.add(pizzaOrdering);
        }
        return pizzaOrderings;
    }

    public Order create(Order order) throws Exception {
        validateOrder(order);
        PreparedStatement statement = connection.prepareStatement("insert into ordering (date, phone, address) values (?,?,?)", Statement.RETURN_GENERATED_KEYS);

        Date date = order.getCurrentDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = format.format(date);

        statement.setString(1, currentDateTime);
        statement.setString(2, order.getPhone());
        statement.setString(3, order.getAddress());
        statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        generatedKeys.next();
        order.setId(generatedKeys.getInt(1));

        PreparedStatement statement2 = connection.prepareStatement("insert into pizzaOrdering (ordering_id, pizza_id,amount) values (?,?,?)");
        for (PizzaOrdering pizzaOrdering : order.getPizzaOrder()) {
            statement2.setInt(1, order.getId());
            statement2.setInt(2, pizzaOrdering.getPizza().getId());
            statement2.setInt(3, pizzaOrdering.getAmount());
            statement2.executeUpdate();
        }
        return order;
    }

    public void update(Order order) throws Exception {
        validateOrder(order);
        doesOrderExist(order);
        PreparedStatement statement = connection.prepareStatement("update ordering set date=(?),phone=(?), address=(?) where id =" + order.getId());

        Date date = order.getCurrentDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = format.format(date);

        statement.setString(1, currentDateTime);
        statement.setString(2, order.getPhone());
        statement.setString(3, order.getAddress());
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated == 0) {
            throw new Exception("Updated failed, no entity with that id exists");
        }

        PreparedStatement statement2 = connection.prepareStatement("delete from pizzaOrdering where ordering_id =" + order.getId());
        statement2.executeUpdate();

        PreparedStatement statement3 = connection.prepareStatement("insert into pizzaOrdering (ordering_id, pizza_id,amount) values (?,?,?)");
        for (PizzaOrdering pizzaOrdering : order.getPizzaOrder()) {
            statement3.setInt(1, order.getId());
            statement3.setInt(2, pizzaOrdering.getPizza().getId());
            statement3.setInt(3, pizzaOrdering.getAmount());
            statement3.execute();
        }
    }

    public boolean delete(int id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("delete from pizzaOrdering where ordering_id=" + id);
        statement.executeUpdate();

        PreparedStatement statement2 = connection.prepareStatement("delete from ordering where id=" + id);
        int numberOfDeletedRecords = statement2.executeUpdate();
        return numberOfDeletedRecords > 0;
    }

    public List<PizzaOrdering> getPizzaOrders(int id) throws Exception {

        PreparedStatement preparedStatement = connection.prepareStatement("select * from pizzaOrdering where ordering_id=" + id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<PizzaOrdering> pizzaOrderList = new ArrayList<>();
        while (resultSet.next()) {
            pizzaOrderList.add(new PizzaOrdering(pizzaDAO.get(resultSet.getInt("pizza_id")), resultSet.getInt("amount")));
        }
        return pizzaOrderList;
    }

    public void validateOrder(Order order) {
        if (order.getPhone() == null) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (order.getAddress() == null) {
            throw new IllegalArgumentException("Address is required");
        }
        if (order.getPizzaOrder() == null) {
            throw new IllegalArgumentException("Pizza Orderings are required");
        }
    }

    public boolean doesOrderExist(Order order) throws Exception {
        if (order.getId() == null || get(order.getId()) == null) {
            throw new IllegalArgumentException("Order doesnt exist");
        }
        return true;
    }
}
