package ch.ti8m.azubi.kti.pizzashop.web;

import ch.ti8m.azubi.kti.pizzashop.dto.Order;
import ch.ti8m.azubi.kti.pizzashop.dto.Pizza;
import ch.ti8m.azubi.kti.pizzashop.dto.PizzaOrdering;
import ch.ti8m.azubi.kti.pizzashop.service.*;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Pizza servlet
 */
@WebServlet("/pizza")
public class PizzaServlet extends HttpServlet {
    private PizzaService pizzaService;
    private Template template;

    @Override
    public void init() throws ServletException {
        pizzaService = ServiceRegistry.getInstance().get(PizzaService.class);
        template = new FreemarkerConfig().loadTemplate("pizza.ftl");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//        set Encoding to UTF-8
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");


        List<Pizza> pizzas = null;
        try {
            pizzas = pizzaService.list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter writer = resp.getWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("pizzas", pizzas);


        try {
            template.process(model, writer);
        } catch (TemplateException ex) {
            throw new IOException("Could not process template: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        set Encoding to UTF-8
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PizzaService pizzaService = new PizzaServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        Order order = new Order();
        List<PizzaOrdering> pizzaOrderingList = new LinkedList<>();
        try {
            //orderService.create(order1);
            for (Pizza pizza : pizzaService.list()) {
                String pizzaNameAmount = pizza.getId() + "-Amount";
                int amount = 0;
                if (req.getParameter(pizzaNameAmount) != "") {
                    amount = Integer.parseInt(req.getParameter(pizzaNameAmount));
                }
                if (amount != 0) {

                    PizzaOrdering pizzaOrdering = new PizzaOrdering(pizza, amount);
                    pizzaOrderingList.add(pizzaOrdering);
                }
            }
            order.setAddress(req.getParameter("address"));
            order.setPizzaOrder(pizzaOrderingList);
            order.setPhone(req.getParameter("tel"));
            order.setDate(order.getCurrentDate());

            orderService.create(order);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }
        // send a redirect to refresh the page
        resp.sendRedirect("order?id=" +order.getId());
    }
}
