package ch.ti8m.azubi.kti.pizzashop.service;

import ch.ti8m.azubi.kti.pizzashop.dto.Pizza;
import ch.ti8m.azubi.kti.pizzashop.persistence.PizzaDAO;

import java.util.Comparator;
import java.util.List;

public class PizzaServiceImpl implements PizzaService {

    private final PizzaDAO pizzaDAO = new PizzaDAO(ConnectionFactory.createDBConnection());

    @Override
    public Pizza get(int id) throws Exception {
        Pizza pizza = pizzaDAO.get(id);
        if (pizza == null) {
            System.err.println("No Order with ID: " + pizza.getId() + " exists.");
        }
        return pizza;
    }

    @Override
    public List<Pizza> list() throws Exception {
        List<Pizza> pizzaList = pizzaDAO.list();
        pizzaList.sort(Comparator.comparing(Pizza::getId));
        return pizzaList;
    }

    @Override
    public Pizza create(Pizza pizza) throws Exception {
        return pizzaDAO.create(pizza);
    }

    @Override
    public void update(Pizza pizza) throws Exception {
        pizzaDAO.update(pizza);
    }

    @Override
    public void remove(int id) throws Exception {
        pizzaDAO.delete(id);
    }
}
