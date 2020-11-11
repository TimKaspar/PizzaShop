package ch.ti8m.azubi.kti.pizzashop;

import ch.ti8m.azubi.kti.pizzashop.dto.Order;
import ch.ti8m.azubi.kti.pizzashop.dto.Pizza;
import ch.ti8m.azubi.kti.pizzashop.dto.PizzaOrdering;
import ch.ti8m.azubi.kti.pizzashop.persistence.OrderDAO;
import ch.ti8m.azubi.kti.pizzashop.persistence.PizzaDAO;
import ch.ti8m.azubi.kti.pizzashop.service.ConnectionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class OrderDAOTest {
    @Before
    public void setup() throws SQLException {
        try (Connection connection = ConnectionFactory.testConnection()) {
            try (Statement statement = connection.createStatement()) {

                statement.execute("drop table if exists pizzaOrdering");
                statement.execute("drop table if exists pizza");
                statement.execute("drop table if exists ordering");

                statement.execute("create table ordering (" +
                        "id int not null auto_increment," +
                        "date datetime," +
                        "phone varchar(255) not null," +
                        "address varchar(255) not null," +
                        "primary key (id));");
                statement.execute("create table pizza (" +
                        "id int not null auto_increment," +
                        "name varchar(255) not null unique," +
                        "price double not null," +
                        "primary key (id));");
                statement.execute("create table pizzaOrdering (" +
                        " ordering_id int not null," +
                        " pizza_id int not null," +
                        " amount int not null," +
                        " foreign key(ordering_id) references ordering(id)," +
                        " foreign key(pizza_id) references pizza(id));");
                statement.execute("insert into pizza (id, name, price) values (1, 'Prosciutto', 15.50);");
                statement.execute("insert into pizza (id, name, price) values (2, 'Hawaii', 16.50);");
                statement.execute("insert into pizza (id, name, price) values (3, 'Mergherita', 14.00);");
                statement.execute("insert into pizza (id, name, price) values (4, 'Prosciutto et Funghi', 16.50);");
                statement.execute("insert into pizza (id, name, price) values (5, 'Tonno', 15.50);");
                statement.execute("insert into pizza (id, name, price) values (6, 'Salami', 15.00);");
                statement.execute("insert into ordering (id, date, phone, address) values (1, '2016-10-01 18:45'," +
                        "    '+41 77 111 1111', 'Teststrasse 123, 8000 Zürich');");
                statement.execute("insert into pizzaOrdering (ordering_id, pizza_id, amount) values (1,2,1);");
                statement.execute("insert into ordering (id, date, phone, address) values (2, '2016-10-01 19:07'," +
                        "    '+41 77 222 2222', 'Anderestrasse 45, 8400 Winterthur');");
                statement.execute("insert into pizzaOrdering (ordering_id, pizza_id, amount) values (2,1,1);");
                statement.execute("insert into pizzaOrdering (ordering_id, pizza_id, amount) values (2,3,3);");
            }
        }
    }

    @Test
    public void testCreate() throws Exception {
        OrderDAO orderDAO = new OrderDAO(ConnectionFactory.testConnection());
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());


        Pizza pizza1 = new Pizza("Prosciutto et Tonno", 15.50);
        Pizza pizza2 = new Pizza("Cinque", 16.50);
        Pizza pizza3 = new Pizza("Capricciosa", 20.50);

        pizzaDAO.create(pizza1);
        pizzaDAO.create(pizza2);
        pizzaDAO.create(pizza3);

        PizzaOrdering pizzaOrdering = new PizzaOrdering(pizza1, 1);
        PizzaOrdering pizzaOrdering2 = new PizzaOrdering(pizza3, 3);

        List<PizzaOrdering> list = new LinkedList<>();
        list.add(pizzaOrdering);
        list.add(pizzaOrdering2);

        Order order = new Order(orderDAO.get(1).getId(), list, orderDAO.get(1).getDate(), orderDAO.get(1).getPhone(), orderDAO.get(1).getAddress());

        orderDAO.create(order);

        Order loaded = orderDAO.get(order.getId());

        Assert.assertNotNull(order.getId());
        Assert.assertEquals(loaded.getId(), order.getId());
    }

    @Test
    public void testGet() throws Exception {
        OrderDAO orderDAO = new OrderDAO(ConnectionFactory.testConnection());
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());


        Pizza pizza1 = new Pizza(pizzaDAO.get(1).getId(), pizzaDAO.get(1).getName(), pizzaDAO.get(1).getPrice());
        Pizza pizza2 = new Pizza(pizzaDAO.get(3).getId(), pizzaDAO.get(3).getName(), pizzaDAO.get(3).getPrice());

        PizzaOrdering pizzaOrdering = new PizzaOrdering(pizza1, 1);
        PizzaOrdering pizzaOrdering2 = new PizzaOrdering(pizza2, 3);

        List<PizzaOrdering> list = new LinkedList<>();
        list.add(pizzaOrdering);
        list.add(pizzaOrdering2);

        Order order = new Order(orderDAO.get(1).getId(), list, orderDAO.get(1).getDate(), orderDAO.get(1).getPhone(), orderDAO.get(1).getAddress());

        orderDAO.create(order);

        Order loaded = orderDAO.get(order.getId());

        Assert.assertNotNull(loaded);
        Assert.assertEquals(order, loaded);

    }

    @Test
    public void testGetPizzaOrdering() throws Exception {
        OrderDAO orderDAO = new OrderDAO(ConnectionFactory.testConnection());
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());


        Pizza pizza1 = new Pizza(pizzaDAO.get(1).getId(), pizzaDAO.get(1).getName(), pizzaDAO.get(1).getPrice());
        Pizza pizza2 = new Pizza(pizzaDAO.get(3).getId(), pizzaDAO.get(3).getName(), pizzaDAO.get(3).getPrice());

        PizzaOrdering pizzaOrdering = new PizzaOrdering(pizza1, 1);
        PizzaOrdering pizzaOrdering2 = new PizzaOrdering(pizza2, 3);

        List<PizzaOrdering> list = new LinkedList<>();
        list.add(pizzaOrdering);
        list.add(pizzaOrdering2);

        Order order = new Order(orderDAO.get(1).getId(), list, orderDAO.get(1).getDate(), orderDAO.get(1).getPhone(), orderDAO.get(1).getAddress());

        orderDAO.create(order);

        List<PizzaOrdering> loaded = orderDAO.getPizzaOrderings(order);

        Assert.assertNotNull(loaded);
        Assert.assertEquals(list, loaded);

    }

    @Test
    public void testGetNotFound() throws Exception {
        OrderDAO orderDAO = new OrderDAO(ConnectionFactory.testConnection());

        Order loaded = orderDAO.get(999999999);

        Assert.assertNull(loaded);

    }

    @Test
    public void testUpdate() throws Exception {
        OrderDAO orderDAO = new OrderDAO(ConnectionFactory.testConnection());
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());


        Pizza pizza2 = new Pizza(pizzaDAO.get(2).getId(), pizzaDAO.get(2).getName(), pizzaDAO.get(2).getPrice());
        Pizza pizza1 = new Pizza(pizzaDAO.get(3).getId(), pizzaDAO.get(3).getName(), pizzaDAO.get(3).getPrice());

        PizzaOrdering pizzaOrdering2 = new PizzaOrdering(pizza1, 3);
        PizzaOrdering pizzaOrdering3 = new PizzaOrdering(pizza2, 6);


        Order order = new Order(orderDAO.get(1).getId(), orderDAO.getPizzaOrders(1), orderDAO.get(1).getDate(), orderDAO.get(1).getPhone(), orderDAO.get(1).getAddress());

        List<PizzaOrdering> list2 = new LinkedList<>();
        list2.add(pizzaOrdering3);
        list2.add(pizzaOrdering2);

        order.setPizzaOrder(list2);

        order.setDate(order.getCurrentDate());
        order.setAddress("Rütistrasse 9");
        order.setPhone("079 757 17 15");

        orderDAO.update(order);

        Order loaded = orderDAO.get(order.getId());

        Assert.assertNotNull(order);
        Assert.assertEquals(order, loaded);
    }

    @Test
    public void testDelete() throws Exception {
        OrderDAO orderDAO = new OrderDAO(ConnectionFactory.testConnection());
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());


        Pizza pizza1 = new Pizza(pizzaDAO.get(1).getId(), pizzaDAO.get(1).getName(), pizzaDAO.get(1).getPrice());
        Pizza pizza2 = new Pizza(pizzaDAO.get(3).getId(), pizzaDAO.get(3).getName(), pizzaDAO.get(3).getPrice());

        List<PizzaOrdering> list = new LinkedList<>();
        Order order = new Order(orderDAO.get(1).getId(), list, orderDAO.get(1).getDate(), orderDAO.get(1).getPhone(), orderDAO.get(1).getAddress());

        PizzaOrdering pizzaOrdering = new PizzaOrdering(pizza1, 4);
        PizzaOrdering pizzaOrdering2 = new PizzaOrdering(pizza2, 3);

        list.add(pizzaOrdering);
        list.add(pizzaOrdering2);

        order.setPizzaOrder(list);
        orderDAO.create(order);

        orderDAO.delete(order.getId());

        Assert.assertNull(orderDAO.get(order.getId()));

    }

    @Test
    public void testList() throws Exception {
        OrderDAO orderDAO = new OrderDAO(ConnectionFactory.testConnection());
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());


        Pizza pizza1 = new Pizza(pizzaDAO.get(1).getId(), pizzaDAO.get(1).getName(), pizzaDAO.get(1).getPrice());
        Pizza pizza2 = new Pizza(pizzaDAO.get(3).getId(), pizzaDAO.get(3).getName(), pizzaDAO.get(3).getPrice());

        List<PizzaOrdering> pizzOrderingList = new LinkedList<>();
        Order order = new Order(orderDAO.get(1).getId(), pizzOrderingList, orderDAO.get(1).getDate(), orderDAO.get(1).getPhone(), orderDAO.get(1).getAddress());

        PizzaOrdering pizzaOrdering = new PizzaOrdering(pizza1, 5);
        PizzaOrdering pizzaOrdering2 = new PizzaOrdering(pizza2, 2);

        pizzOrderingList.add(pizzaOrdering);
        pizzOrderingList.add(pizzaOrdering2);

        order.setPizzaOrder(pizzOrderingList);
        orderDAO.create(order);

        Order loaded = orderDAO.list().get(orderDAO.list().size() - 1);

        Assert.assertNotNull(order);
        Assert.assertEquals(order, loaded);

    }
}
