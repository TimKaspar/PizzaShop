package ch.ti8m.azubi.kti.pizzashop.service;

import ch.ti8m.azubi.kti.pizzashop.dto.Order;
import ch.ti8m.azubi.kti.pizzashop.dto.PizzaOrdering;
import ch.ti8m.azubi.kti.pizzashop.persistence.OrderDAO;

import java.util.Comparator;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO = new OrderDAO(ConnectionFactory.createDBConnection());

    @Override
    public Order get(int id) throws Exception {
        Order order = orderDAO.get(id);
        if (order == null) {
            System.err.println("No Order with ID: " + order.getId() + " exists.");
        }
        return order;
    }

    @Override
    public List<PizzaOrdering> getPizzaOrderings(Order order) throws Exception {
        return orderDAO.getPizzaOrderings(order);
    }

    @Override
    public List<Order> list() throws Exception {
        List<Order> orderList = orderDAO.list();
        orderList.sort(Comparator.comparing(Order::getId));
        return orderList;
    }

    @Override
    public Order create(Order order) throws Exception {
        return orderDAO.create(order);
    }

    @Override
    public void update(Order order) throws Exception {
        orderDAO.update(order);
    }

    @Override
    public void remove(int id) throws Exception {
        orderDAO.delete(id);
    }
}
