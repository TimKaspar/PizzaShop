# Pizza Shop
## SQL Setup
````
--drop existing database
drop database if exists pizzashop;

--create database
create database pizzashop;
use pizzashop;
-- drop existing tables

drop table if exists pizza_ordering;
drop table if exists pizza;
drop table if exists ordering;

-- create tables

create table ordering (
id int not null unique,
date datetime,
phone varchar(20) not null,
address varchar(50) not null,
primary key (id)
);

create table pizza (
id int not null unique,
name varchar(20) not null unique,
primary key (id)
);

create table pizza_ordering (
ordering_id int not null,
pizza_id int not null,
amout int not null,
foreign key(ordering_id) references ordering(id),
foreign key(pizza_id) references pizza(id)
);
````
## PizzaShop Front End

### Setup
Download Node.js:

    https://nodejs.org/en/download/
Install npm

    npm install

Install Node

    npm install node

Install JQuery

    npm install jquery

### Build
To build this project with npm use:

    npm run-script build

### Run Project
The following command will run the Project:

    npm run start

Use `CTRL + C` to stop.

### To Run Locally

    https://localhost:3000

## PizzaShop Back End

### Build the application

To build the application with Gradle:

	gradle

### Run / deploy the application

### Local (project)
The following command will download (`container/download`), extract (`container/extract`) and start a local Tomcat container, and also deploy the application:

    gradle run -i

Use `CTRL + C` to stop the container.

### Remote (existing local or remote Tomcat installation)
Configure the tomcat settings in the `gradle.properties` (for the Tomcat user you configured). <br/>
Example:

    tomcat.host=localhost
    tomcat.port=8081
    tomcat.user=tomcat
    tomcat.password=root

[comment]: <> (To deploy the application WAR on the specified Tomcat, use:)

[comment]: <> (    gradle deploy)

### Testing:
- http://localhost:8080/pizzashop/pizza (Pizza Servlet)
- http://localhost:8080/pizzashop/api/pizza (Pizza API)
- http://localhost:8080/pizzashop/order (Order Servlet)
- http://localhost:8080/pizzashop/api/order (Order API)


