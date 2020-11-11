function addAmount(id) {
    var element = document.getElementById(id);
    if (!element) {
        throw "No such element: " + id;
    }
    if (!element.value) { // falls leer
        element.value = 1;
    } else {
        var count = parseInt(element.value);
        element.value = count + 1;
    }
}

function loadPizzas() {
    var xhttp = new XMLHttpRequest();


    // HTML builder to render HTML more easily
    class HTMLBuilder {

        constructor(node) {
            this.node = typeof node == "string" ? document.createElement(node) : node;
        }

        element(elementName) {
            var element = document.createElement(elementName);
            this.node.append(element);
            return new HTMLBuilder(element);
        }

        text(text) {
            this.node.append(document.createTextNode(text));
            return this;
        }

        attribute(key, value) {
            this.node.setAttribute(key,value);
            return this;
        }

        clear() {
            this.node.innerHTML="";
            return this;
        }
    }

    //render Pizza page as html
    function showPizzas(pizzas) {

    }




    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var obj = JSON.parse(xhttp.responseText);
            console.log("All Pizzas:");
            for (var i in obj) {
                console.log("id: " + obj[i].id + " name: " + obj[i].name + " price: " + obj[i].price);
            }
        }
    };
    xhttp.open("GET", "http://localhost:8080/pizzashop/api/pizza", true);
    xhttp.send();
}


loadPizzas();