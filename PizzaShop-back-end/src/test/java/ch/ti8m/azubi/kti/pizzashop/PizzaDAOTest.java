package ch.ti8m.azubi.kti.pizzashop;

import ch.ti8m.azubi.kti.pizzashop.dto.Pizza;
import ch.ti8m.azubi.kti.pizzashop.persistence.OrderDAO;
import ch.ti8m.azubi.kti.pizzashop.persistence.PizzaDAO;
import ch.ti8m.azubi.kti.pizzashop.service.ConnectionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PizzaDAOTest {
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
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());

        Pizza pizza = new Pizza(1, "Margarita", 19.50);

        Pizza loaded = pizzaDAO.get(pizza.getId());

        Assert.assertNotNull(pizza.getId());
        Assert.assertEquals(loaded.getId(), pizza.getId());
    }

    @Test
    public void testGet() throws Exception {
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());

        Pizza pizza = new Pizza(pizzaDAO.get(1).getId(), pizzaDAO.get(1).getName(), pizzaDAO.get(1).getPrice());

        Pizza loaded = pizzaDAO.get(pizza.getId());

        Assert.assertNotNull(loaded);
        Assert.assertEquals(pizza, loaded);

    }

    @Test
    public void testGetNotFound() throws Exception {
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());

        Pizza loaded = pizzaDAO.get(999999999);

        Assert.assertNull(loaded);

    }

    @Test
    public void testUpdate() throws Exception {
        OrderDAO orderDAO = new OrderDAO(ConnectionFactory.testConnection());
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());


        Pizza pizza = new Pizza(pizzaDAO.get(1).getId(), pizzaDAO.get(1).getName(), pizzaDAO.get(1).getPrice());


        pizza.setName("Capricciosa");
        pizza.setPrice(23.35);

        pizzaDAO.update(pizza);

        Pizza loaded = pizzaDAO.get(pizza.getId());

        Assert.assertNotNull(pizza);
        Assert.assertEquals(pizza, loaded);
    }

    @Test
    public void testDelete() throws Exception {
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());

        Pizza pizza = new Pizza("Margarita", 19.50);
        pizzaDAO.create(pizza);

        pizzaDAO.delete(pizza.getId());

        Assert.assertNull(pizzaDAO.get(pizza.getId()));

    }

    @Test
    public void testList() throws Exception {
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());

        Pizza pizza = new Pizza(pizzaDAO.get(1).getId(), pizzaDAO.get(1).getName(), pizzaDAO.get(1).getPrice());


        Pizza loaded = pizzaDAO.list().get(0);

        Assert.assertNotNull(pizza);
        Assert.assertEquals(pizza, loaded);

    }

    @Test
    public void testSave() throws Exception {
        PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.testConnection());

        Pizza pizza1 = new Pizza("Capricciosa", 19.50);
        Pizza pizza2 = new Pizza("Futji", 19.50);
        pizzaDAO.create(pizza1);
        pizza1.setName("Cinque");
        pizza2.setName("Prosciutto et Tonno");

        pizzaDAO.save(pizza1);
        pizzaDAO.save(pizza2);
    }
}


