# Credit Card Processing Application 
Author - Ritika Sao

Email - saoritika@gmail.com

Submission Date - 6th Dec 2021

<h1> Project </h1>
<p> This app exposes two REST end points : 
<p> POST /api/v1/credit/cards - creates a new credit card with given card no against the given customer name
<p> GET /api/v1/credit/cards/{cardID} - get information of a credit card
<p> GET /api/v1/credit/cards/ - get pageable information of all credit cards

<h1> Tech / Framework used </h1>
<p> Spring Boot
<p> Spring REST  
<p> JDK 8
<p> Lombok Processors
<p> Springfox Swagger2 / Swagger UI
<p> Junit-4 / Mockito  

<h1> How to run this application </h1>
<p> 1. Clone this repo into your local system and import in an IDE (IntelliJ/Eclipse)
<p> 2. Run the CreditCardApplication.java class which should start the embedded tomcat server in localhost:8080
<p> 3. Open the Swagger UI - <b>http://localhost:8080/swagger-ui.html#/ </b>
<p> 4. Create credit cards using the POST end point and get one/all credit card information using GET end points

<h1> Security Considerations </h1>
As there is no UI involved in this application and no user/ admin credentials in the picture, there's no way to authorize the users. But in ideal scenario, this APIs should
be consumed by only admin bearer token with required scopes/ entitlements to create credit card for customers.

