# PizzaShop Back End

## Build the application

To build the application with Gradle:

	gradle

## Run / deploy the application

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

##Testing:
- http://localhost:8080/pizzashop/pizza (Pizza Servlet)
- http://localhost:8080/pizzashop/api/pizza (Pizza API)
- http://localhost:8080/pizzashop/order (Order Servlet)
- http://localhost:8080/pizzashop/api/order (Order API)
