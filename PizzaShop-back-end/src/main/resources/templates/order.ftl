<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <title>Confirmation</title>
    <link rel="stylesheet" type="text/css" href="confirmation.css">
</head>
<body>
<#--Titel-->
<div class="header">
    <h1>Pizza Shop</h1>
    <h2>Vielen Dank für die Bestellung!</h2>
</div>
<#--Confirmation--><
<#assign amountCounter = 0>

<#setting number_format="currency">
<#list pizzaOrderings as pizzaOrder>
    <#if 0 != id>
        <div class="box">
            <div class="grid">
                <#assign currentPizza = pizzas[pizzaOrder.pizza.id - 1]>
                <img src="Img/${currentPizza.name}.jpg" alt="Pizza ${currentPizza}" class="grid-item"/>
                <div class="grid-item">Pizza ${currentPizza.name}</div>
                <div class="grid-item">${pizzaOrder.amount?string.computer}x</div>
                <#assign amountCounter = amountCounter + pizzaOrder.amount>
                <div class="grid-item"> ${price[currentPizza.id -1]}</div>
            </div>
        </div>
    </#if>
</#list>

<#--Confirmation Total-->
<div class="box">
    <div class="grid">
        <div></div>
        <div class="grid-item">Total</div>
        <div class="grid-item">${amountCounter?string.computer}x</div>
        <div class="grid-item"> ${total}</div>
    </div>
</div>
<#--Button -> zurück auf /pizza-->
<div class="box">
    <form action="http://localhost:8080/pizzashop/pizza">
        <button class="button" type="submit"><span class="nextButton">Nochmals Bestellen</span></button>
    </form>
</div>

</body>
</html>