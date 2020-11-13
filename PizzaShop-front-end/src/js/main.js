let $ = require('jquery');

function addAmount(id) {
    let element = document.getElementById(id);
    if (!element) {
        throw "No such element: " + id;
    }
    if (!element.value) { // falls leer
        element.value = 1;
        console.log("added 1 to " + id);
    } else {
        var count = parseInt(element.value);
        element.value = count + 1;
        console.log("added 1 to " + id);
    }
}


// HTML builder to render HTML more easily
class HTMLBuilder {

    constructor(node) {
        this.node = typeof node == "string" ? document.createElement(node) : node;
    }

    element(elementName, prepend = false, insertAfterId = null,) {
        var element = document.createElement(elementName);
        if (prepend) {
            this.node.prepend(element);
        } else if (insertAfterId) {
            this.node.insertBefore(element, document.getElementById(insertAfterId));
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
function showPizzas(pizzas) {
    const ingredints = ["Schinken, Ruccola, Tomaten, Mozorella", "Schinken, Ananas, Tomaten, Mozorella", "Tomaten, Mozorella, Basilikum", "Schinken, Champignon, Tomaten, Mozorella", "Ton, Zwiebeln, \n Tomaten, Mozorella", "Salami, Tomaten, Mozorella"];

    //shopping cart
    let placeholder = document.getElementById("cart-checkout");
    let builder = new HTMLBuilder(placeholder);

    if (pizzas) {
        pizzas.reverse();
        for (let pizza of pizzas) {
            let p_cart = builder.element("p", true).attribute("id", "p." + pizza.name);
            p_cart.element("label").text(pizza.name).attribute("for", pizza.name);
            p_cart.element("input").attribute("type", "number").attribute("id", pizza.name).attribute("name", pizza.id + "-Amount").attribute("min", "0").attribute("max", "50");

        }
    } else {
        div.text("no Pizzas found");
    }

    // Pizza Selection
    placeholder = document.getElementById("grid");
    builder = new HTMLBuilder(placeholder);

    let div = builder.element("div").attribute("id", "flex-container");
    if (pizzas) {
        pizzas.reverse();
        for (let pizza of pizzas) {
            let button = div.element("button").attribute("class", "hvr-sweep-to-top flex-item").attribute("id", "flex-item" + pizza.id);
            button.node.addEventListener("click", addAmount.bind(null, pizza.name));
            button.element("span").text(pizza.name).attribute("class", "pizzaName");
            button.element("span").text("Zutaten").attribute("class", "ingredients");
            let p_selection = button.element("p");
            p_selection.element("span").text(ingredints[pizza.id - 1]).attribute("id", "pizzaInfoContent").attribute("class", "ingredients");
        }
    } else {
        div.text("no Pizzas found");
    }
}

//add onclick="post()" to "Bestellen" button
document.getElementById("form").addEventListener("submit", post)

xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function () {


    if (this.readyState == 4 && this.status == 200) {
        let pizzas = JSON.parse(xhttp.responseText);
        showPizzas(pizzas);
        //show Json in console
        console.log("Pizza Json received");
        console.log(pizzas)
    } else if (this.status >= 400) {
        console.log('Error ' + this.status + ': ' + xhttp.responseText);
    }
};
xhttp.open("GET", "http://localhost:8080/pizzashop/api/pizza", true);
xhttp.send();


function post(e) {
    //removes all existing error messages (readded later if still no input)
    let errors = document.getElementsByClassName("error");
    while (errors[0]) {
        errors[0].parentNode.removeChild(errors[0]);
    }


    e.preventDefault();
    $.getJSON('http://localhost:8080/pizzashop/api/pizza', function (json) {
        console.log("%s", "Pizza API Json", json[json.length - 1].name)
        let jsonObj;
        //execute will be set to false if any of the conditions are not met (input in address,tel and at least 1 pizza)
        let execute = true;

        let pizzaOrders = [];
        for (let i = 0; i < json.length; i++) {
            let amount = parseInt(document.getElementById(json[i].name).value);
            if (!isNaN(amount) && amount !== 0) {
                let pizzaOrder = {
                    pizza: {
                        id: json[i].id,
                        name: json[i].name,
                        price: json[i].price
                    },
                    amount: amount
                }
                pizzaOrders.push(pizzaOrder)
            }
        }
        jsonObj = {
            "pizzaOrder": pizzaOrders,
            "phone": document.getElementById("tel").value,
            "address": document.getElementById("address").value
        }
        console.log("%s", "jsonObj", jsonObj);

        //check if fields are empty
        if (pizzaOrders.length < 1) {
            console.log("lastPizza " + "p." + json);
            let builder = new HTMLBuilder(document.getElementById("p." + json[json.length - 1].name));

            builder.element("div").text("Bitte wählen Sie mindestens eine Pizza aus").attribute("class", "error");

            execute = false;
        }
        if (document.getElementById("address").value == "") {
            let placeholder = document.getElementById("p.address");
            let builder = new HTMLBuilder(placeholder);

            builder.element("div").text("Bitte geben Sie eine Addresse an").attribute("class", "error");
            execute = false;
        }
        if (document.getElementById("tel").value == "") {
            let placeholder = document.getElementById("p.tel");
            let builder = new HTMLBuilder(placeholder);

            builder.element("div").text("Bitte geben Sie eine Telefonnummer an").attribute("class", "error");
            execute = false;
        }
        if (execute) {
            let xhr = new XMLHttpRequest();
            xhr.open("POST", 'http://localhost:8080/pizzashop/api/order', true);

            //Send the proper header information along with the request
            xhr.setRequestHeader("Content-Type", "application/json");

            xhr.onreadystatechange = function () { // Call a function when the state changes.
                if (this.readyState === 4 && this.status === 200) {
                    // Request finished. Do processing here.
                    let json2 = JSON.parse(xhr.responseText);
                    console.log({json2})
                    window.location.href = "../index2.html?id=" + json2.id;
                }
            }
            xhr.send(JSON.stringify(jsonObj));
        }
    });
}