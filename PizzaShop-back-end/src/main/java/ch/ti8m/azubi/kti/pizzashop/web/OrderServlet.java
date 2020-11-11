package ch.ti8m.azubi.kti.pizzashop.web;

import ch.ti8m.azubi.kti.pizzashop.dto.Order;
import ch.ti8m.azubi.kti.pizzashop.dto.Pizza;
import ch.ti8m.azubi.kti.pizzashop.dto.PizzaOrdering;
import ch.ti8m.azubi.kti.pizzashop.service.OrderService;
import ch.ti8m.azubi.kti.pizzashop.service.PizzaService;
import ch.ti8m.azubi.kti.pizzashop.service.ServiceRegistry;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * OrderID servlet
 */
@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private OrderService orderService;
    private PizzaService pizzaService;
    private Template template;

    @Override
    public void init() throws ServletException {
        orderService = ServiceRegistry.getInstance().get(OrderService.class);
        pizzaService = ServiceRegistry.getInstance().get(PizzaService.class);
        template = new FreemarkerConfig().loadTemplate("order.ftl");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // set Encoding to UTF-8
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        Map<String, Object> model = new HashMap<>();

        List<BigDecimal> price = new LinkedList<>();
        BigDecimal total= BigDecimal.valueOf(0.0);
        int id = 0;
        try {
            id = Integer.parseInt(req.getParameter("id"));
            Order order = orderService.get(id);
            total = BigDecimal.valueOf(order.getTotal()).setScale(2, BigDecimal.ROUND_HALF_UP);

            List<PizzaOrdering> pizzaOrderings = orderService.getPizzaOrderings(order);
            List<Pizza> pizzas = pizzaService.list();

            price = new LinkedList<>();
            for (Pizza pizza : pizzas) {
                price.add(BigDecimal.valueOf(pizza.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
            }

            model.put("total", total);
            model.put("price", price);
            model.put("pizzaOrderings", pizzaOrderings);
            model.put("id", id);
            model.put("pizzas", pizzas);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage() + "total: "+total+" price"+price, e);
        }

        try {
            template.process(model, writer);
        } catch (TemplateException ex) {
            throw new IOException("Could not process template: " + ex.getMessage() + " id is: " + id );
        }
    }
}
