// HTML builder to render HTML more easily
class HTMLBuilder {

    constructor(node) {
        this.node = typeof node == "string" ? document.createElement(node) : node;
    }

    element(elementName, prepend = false) {
        var element = document.createElement(elementName);
        if (prepend) {
            this.node.prepend(element)
        } else {
            this.node.append(element);
        }
        return new HTMLBuilder(element);
    }

    text(text) {
        this.node.append(document.createTextNode(text));
        return this;
    }

    attribute(key, value) {
        this.node.setAttribute(key, value);
        return this;
    }

    clear() {
        this.node.innerHTML = "";
        return this;
    }
}

//render Pizza page as html
function showOrder(order) {
    //confirmation
    builder = new HTMLBuilder(document.getElementById("listGrid"));

    let totalPrice = 0;
    let totalAmount = 0;
    if (order) {
        for (var pizzaOrdering of order.pizzaOrder) {
            let pizza = pizzaOrdering.pizza;
            builder.element("img").attribute("class", "grid-item").attribute("src", "../../Img/" + pizzaOrdering.pizza.name + ".jpg").attribute("alt", "Pizza" + pizza.name);
            builder.element("div").attribute("class", "grid-item").text("Pizza " + pizza.name);
            builder.element("div").attribute("class", "grid-item").text(pizzaOrdering.amount +"x");
            builder.element("div").attribute("class", "grid-item").text(pizza.price.toFixed(2) + " CHF")
            totalPrice += pizza.price * pizzaOrdering.amount;
            totalAmount += pizzaOrdering.amount;
        }

        builder = new HTMLBuilder(document.getElementById("totalGrid"));

        builder.element("div");
        builder.element("div").attribute("class", "grid-item").text("Total");
        builder.element("div").attribute("class", "grid-item").text(+totalAmount + "x");
        builder.element("div").attribute("class", "grid-item").text(totalPrice.toFixed(2) + " CHF");
    } else {
        builder.text("no Orders found");
    }
}

var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function () {


    if (this.readyState == 4 && this.status == 200) {
        var order = JSON.parse(xhttp.responseText);
        console.log(order.id);
        console.log(order);
        showOrder(order);
        //show Json in console
        console.log("Pizza Json received");
    } else if (this.status >= 400) {
        console.log('Error ' + this.status + ': ' + xhttp.responseText);
    }
};
let id = location.search;
id = id.slice(4);
console.log(id);
xhttp.open("GET", "http://localhost:8080/pizzashop/api/order/" + id, true);
xhttp.send();