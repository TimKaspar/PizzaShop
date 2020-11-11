package ch.ti8m.azubi.kti.pizzashop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Order {

    private Integer id;
    private final List<PizzaOrdering> pizzaOrder;
    private Date date;
    private String phone;
    private String address;
    private double total;

    public double getTotal() {
        double total = 0;
        for (PizzaOrdering ordering : pizzaOrder) {
            total+= ordering.getAmount()*ordering.getPizza().getPrice();
        }
        return total;
    }

    public Order() {
        this.pizzaOrder = new LinkedList<>();
    }

    public Order(Integer id, List<PizzaOrdering> pizzaOrder, Date date, String phone, String address) {
        this.date = date;
        this.phone = phone;
        this.address = address;
        this.id = id;
        if (pizzaOrder == null){
            pizzaOrder = new LinkedList<>();
        }
        this.pizzaOrder = pizzaOrder;
    }

    public Order(List<PizzaOrdering> pizzaOrder, Date date, String phone, String address) {
        this(null, pizzaOrder, date, phone, address);
    }


    public Order(List<PizzaOrdering> pizzaOrder, String phone, String address) {
        this(null,pizzaOrder,null,phone,address);
    }

    @JsonProperty(value = "date", access = JsonProperty.Access.READ_ONLY)
    public Date getCurrentDate() {
        //calculates currrent time but in String
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String stringDate = dtf.format(now);

        //converts String to Date
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(stringDate);
            dtf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(stringDate + "\t" + date1);
        return date1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = getCurrentDate();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PizzaOrdering> getPizzaOrder() {
        return pizzaOrder;
    }

    public void setPizzaOrder(List<PizzaOrdering> order) {
        this.pizzaOrder.clear();
        if (order!=null){
            this.pizzaOrder.addAll(order);
        }
    }


    @Override
    public String toString() {
        return "Order{" + "\n" +
                "id=" + id + "\n" +
                "pizzaOrder=" + pizzaOrder + "\n" +
                "date=" + date + "\n" +
                "phone='" + phone + "\n" +
                "address='" + address + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (id == null) {
            return false;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }
}
