package ch.ti8m.azubi.kti.pizzashop.ws;

import ch.ti8m.azubi.kti.pizzashop.dto.Order;
import ch.ti8m.azubi.kti.pizzashop.dto.Pizza;
import ch.ti8m.azubi.kti.pizzashop.dto.PizzaOrdering;
import ch.ti8m.azubi.kti.pizzashop.service.OrderService;
import ch.ti8m.azubi.kti.pizzashop.service.OrderServiceImpl;
import ch.ti8m.azubi.kti.pizzashop.service.PizzaService;
import ch.ti8m.azubi.kti.pizzashop.service.PizzaServiceImpl;
import ch.ti8m.azubi.kti.pizzashop.ws.config.ObjectMapperFactory;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
//        PizzaService pizzaService = new PizzaServiceImpl();
//        OrderService orderService = new OrderServiceImpl();
//
//
//        Pizza pizza = pizzaService.get(1);
//        pizza.setPrice(12.5);
//        pizza.setName("test14543d3");
//        Pizza pizza2 = pizzaService.get(1);
//        pizza.setPrice(12.5);
//        pizza.setName("test1445d4");
//
//        PizzaOrdering pizzaOrdering = new PizzaOrdering(pizza,4);
//        PizzaOrdering pizzaOrdering2 = new PizzaOrdering(pizza2,2);
//
//
//
//        List<PizzaOrdering> pizzaOrderingList = new LinkedList<>();
//        pizzaOrderingList.add(pizzaOrdering);
//        pizzaOrderingList.add(pizzaOrdering2);
//
//        Order order = new Order(pizzaOrderingList,"087 4444 44 44","Ruetelerstrasse 23");
//        orderService.create(order);
//
//        for (Order savedOrder : orderService.list()) {
//            System.out.println(savedOrder.getPhone() + " lives at: " + savedOrder.getAddress());
//        }
//
//        String json = ObjectMapperFactory.objectMapper().writeValueAsString(order);
//        System.out.println(json);
//        Pizza restoredPizza = ObjectMapperFactory.objectMapper().readValue(json, Pizza.class);
    }
}
