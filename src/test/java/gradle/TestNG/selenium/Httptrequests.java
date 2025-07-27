package gradle.TestNG.selenium;

import static  io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import org.json.JSONObject;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Httptrequests  {

    private static String createdEmail;
    private static Customer customer;
    private static String LoginResponsetoken;
    private static String BaseUrl = "http://localhost:3000";

    @BeforeTest
    public void setup() {
        customer = new Customer.Builder().build();
    }

    @Test(priority = 3, groups = {"httpRequest", "api", "customer"})
    public void createCustomer() {
        //Customer customer = new Customer.Builder().build();
        JSONObject registrationUser = new JSONObject()
                .put("email", customer.getEmail())
                .put("password", customer.getPassword())
                .put("securityQuestion", new JSONObject().put("id", 1))
                .put("securityAnswer", customer.getSecurityAnswer());
        String Response = given()
                .header("Content-Type", "application/json")
                .body(registrationUser.toString())
                .when()
                .post(BaseUrl+"/api/Users")
                .then()
                .body("status", equalTo("success"))
                .body("data.email", notNullValue())
                .header("X-Content-Type-Options", "nosniff")
                .header("X-Frame-Options", "SAMEORIGIN")
                .header("Access-Control-Allow-Origin", "*")
                .statusCode(201)
                .extract().response().asString();
        System.out.println("Response: " + Response);
        JsonPath jsonPath = new JsonPath(Response);
        createdEmail = jsonPath.getString("data.email");
        System.out.println("Created Email: " + createdEmail);
    }
    @Test(priority = 4, dependsOnMethods = {"createCustomer"}, groups = {"httpRequest", "api", "customer"})
    public void loginCustomer() {
        //Assert.assertNotNull(createdEmail, "Email from createCustomerTest was null. State was not shared correctly between tests.");
        JSONObject LoginUser = new JSONObject()
                .put("email", createdEmail)
                .put("password", customer.getPassword());
        System.out.println("Login User: " + LoginUser.toString());
        String Loginresponse = given()
                .header("Content-Type", "application/json")
                .body(LoginUser.toString())
                .when()
                .post(BaseUrl+"/rest/user/login")
                .then()
                .body("authentication.umail", equalTo(createdEmail))
                .body("authentication.token", notNullValue())
                .statusCode(200)
                .extract().response().asString();
        LoginResponsetoken = new JsonPath(Loginresponse).getString("authentication.token");
        System.out.println("Login Response token: " + LoginResponsetoken);
    }
    @Test(priority = 5, dependsOnMethods = {"loginCustomer"}, groups = {"httpRequest", "api", "customer"})
    public void getProductReview() {
           String ProductReviewresponse = given()
                .header("Content-Type", "application/json")
                .header("Authorization", LoginResponsetoken)
                .when()
                .get(BaseUrl+"/rest/products/24/reviews")
                .then()
                .body("status", equalTo("success"))
                .statusCode(200)
                .extract().response().asString();
        System.out.println("Get Product Review Details: " + ProductReviewresponse);
    }
    @Test(priority = 6, dependsOnMethods = {"getProductReview"}, groups = {"httpRequest", "api", "customer"})
    public void PutProductReviews() {
        JSONObject productDetails = new JSONObject()
                .put("message", "This is a test review")
                .put("author", createdEmail);
        String PutProductReviewsresponse = given()
                .header("Content-Type", "application/json")
                .header("Authorization", LoginResponsetoken)
                .body(productDetails.toString())
                .when()
                .put(BaseUrl+"/rest/products/24/reviews")
                .then()
                .body("staus", equalTo("success"))
                .statusCode(201)
                .extract().response().asString();
        System.out.println("Get Product Details: " + PutProductReviewsresponse);
        JsonPath jsonPath = new JsonPath(PutProductReviewsresponse);
    }
    @Test(priority = 7, groups = {"httpRequest", "api", "customer"})
    public void Getallproducts() {
        String GetallResponse = given()
                .header("Content-Type", "application/json")
                .when()
                .get(BaseUrl+"/rest/products/search?q=")
                .then()
                .body("status", equalTo("success"))
                .statusCode(200)
                .extract().response().asString();
        JsonPath Prdjsonresponse = new JsonPath(GetallResponse);
        int totalProducts = Prdjsonresponse.getInt("data.size()");
        System.out.println("Products found in the response."+ totalProducts);
        for(int i = 0; i < totalProducts; i++) {
            String productName = Prdjsonresponse.getString("data[" + i + "].name");
            String productprice = Prdjsonresponse.getString("data[" + i + "].price");
            System.out.println("Product Name: " + productName + ", Product Price: " + productprice);
            if(productName.equalsIgnoreCase("Lemon Juice (500ml)")) {
                String productId = Prdjsonresponse.getString("data[" + i + "].id");
                System.out.println("Product ID: " + productId);
                String productDescription = Prdjsonresponse.getString("data[" + i + "].description");
                System.out.println("Product Description: Lemon Juice (500ml) " + productDescription);
            }
        }
    }
}
