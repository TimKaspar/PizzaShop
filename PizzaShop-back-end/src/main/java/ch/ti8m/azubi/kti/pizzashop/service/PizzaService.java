package ch.ti8m.azubi.kti.pizzashop.service;

import ch.ti8m.azubi.kti.pizzashop.dto.Pizza;

import java.util.List;

public interface PizzaService {

    Pizza get(int id) throws Exception;

    List<Pizza> list() throws Exception;

    void update(Pizza pizza) throws Exception;

    Pizza create (Pizza pizza) throws Exception;

    void remove (int id) throws Exception;
}
