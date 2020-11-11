<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <title>Pizza</title>
    <link rel="stylesheet" type="text/css" href="Pizza.css">
    <script src="Pizza.js"></script>
</head>
<body>
<#-- Grid wrapper for whole Page -->
<div class="grid">
    <#-- all idividual values from flex-items (Pizza Selection)-->
    <#assign itemCounts = [32,16,8,4,2,1]>
    <#assign flexItems = ["flex-item1","flex-item2","flex-item3","flex-item4","flex-item5","flex-item6"]>
    <#assign ingredints = ["Schinken, Ruccola, Tomaten, Mozorella","Schinken, Ananas, Tomaten, Mozorella", "Tomaten, Mozorella, Basilikum","Schinken, Champignon, Tomaten, Mozorella","Ton, Zwiebeln, &nbsp Tomaten, Mozorella","Salami, Tomaten, Mozorella"]>

    <#-- Title-->
    <h1>Pizza Shop</h1>

    <#-- Pizza Selection -->
    <div class="flex-container">
        <#list itemCounts as itemCount>
            <button onclick="addAmount('${pizzas[itemCount_index].name}')" class="hvr-sweep-to-top flex-item"
                    id="${flexItems[itemCount_index]}">
                <span class="pizzaName">${pizzas[itemCount_index].name}</span>
                <span class="ingredients">Zutaten</span>
                <p><span class="ingredients" id="pizzaInfoContent">${ingredints[itemCount_index]}</span></p>
            </button>
        </#list>
    </div>

    <#macro loadCart>
        <#list pizzas as pizza>
            <p>
                <label for="${pizza.name}">${pizza.name}</label>
                <input type="number" id="${pizza.name}" name="${pizza.id}-Amount" min="0" max="50">
            </p>
        </#list>
    </#macro>

    <#-- Shopping Cart -->
    <div class="grid-item-shoppingcart">
        <ul class="cart-wrapper">
            <li class="cart-header"> Warenkorb</li>

            <form class="form" method="post">
                <li class="cart-checkout">
                    <@loadCart/>
                    <br>
                    <p>
                        <label for="address">Addresse</label>
                        <input class="orderInput" type="text" id="address" name="address">
                    </p>
                    <p>
                        <label for="tel">Telefon Nummber</label>
                        <input class="orderInput" type="tel" id="tel" name="tel">
                    </p>
                    <button class="button" type="submit"><span class="nextButton">Bestellen</span></button>
            </form>

            </li>
        </ul>
    </div>
</div>
</body>
</html>