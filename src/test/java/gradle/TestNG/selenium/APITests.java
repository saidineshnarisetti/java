package gradle.TestNG.selenium;

import io.restassured.path.json.JsonPath;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.lang.reflect.Array;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class APITests{
    private static Customer customer = new Customer.Builder().build();
    private static String BaseUrl = "http://localhost:3000";
    UIAutomation ui =new UIAutomation();
    private static String createdEmail;
    private static String prdId;
    public static String LoginResponsetoken;
    public static int intailreviews;
    @Test(priority = 10, groups = {"httpRequest", "api", "customer"})
    public void CreatUser() {
        JSONObject registrationUser = new JSONObject();
        registrationUser.put("email", customer.getEmail())
                .put("password", customer.getPassword())
                .put("securityQuestion", new JSONObject().put("id", 1))
                .put("securityAnswer", customer.getSecurityAnswer());

               String CreateUserresponse= given()
                    .header("Content-Type", "application/json")
                    .body(registrationUser.toString())
                .when()
                        .post(BaseUrl+"/api/Users")
                .then()
                    .statusCode(201)
                        .body("status", equalTo("success"))
                       .extract().response().asString();
        JsonPath jsonPath = new JsonPath(CreateUserresponse);
        createdEmail = jsonPath.getString("data.email");
        System.out.println("Response: " + jsonPath.getString("data.email"));
    }
    @Test(priority = 11, dependsOnMethods = {"CreatUser"}, groups = {"httpRequest", "api", "customer"})
    public void LoginUser(){
        JSONObject LoginUser = new JSONObject()
                .put("email", createdEmail)
                .put("password", customer.getPassword());
        System.out.println("Login User: " + LoginUser.toString());

        String Loginresponse = given()
                .header("Content-Type", "application/json")
                .header("X-Content-Type-Options", "nosniff")
                .header("Access-Control-Allow-Origin", "*")
                .body(LoginUser.toString())
                .when()
                .post(BaseUrl+"/rest/user/login")
                .then()
                .extract().response().asString();
        JsonPath jsonPath = new JsonPath(Loginresponse);
        LoginResponsetoken = jsonPath.getString("authentication.token");
        System.out.println("Login Token: " + LoginResponsetoken);
    }
    @Test(priority = 12, dependsOnMethods = {"LoginUser"}, groups = {"httpRequest", "api", "customer"})
    public void GetAllProducts(){
       String GetAllProductResponse= given()
                .header("Content-Type", "application/json")
                .header("Authorization", LoginResponsetoken)
                .when()
                .get(BaseUrl+"/rest/products/search")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.size()", greaterThan(0))
               .extract().response().asString();
        JsonPath jsonPath = new JsonPath(GetAllProductResponse);
        System.out.println(GetData.GetProductDetails(GetAllProductResponse));
        String product ="Banana Juice (1000ml)";
        System.out.println(GetData.GetProductID(product, GetAllProductResponse));
        int products = jsonPath.getInt("data.size()");
        System.out.println("Number of Products: " + products);
//        for(int i=0; i < products; i++) {
//            System.out.println("Product ID: " + jsonPath.getString("data[" + i + "].name"));
//            if((jsonPath.getString("data[" + i + "].name")).equals("Banana Juice (1000ml)")){
//                System.out.println("Product ID" + (jsonPath.getString("data[" + i + "].id")));
//                prdId =(jsonPath.getString("data[" + i + "].id"));
//                break;
//            }
//        }
        System.out.println("Get All Products Response: " + jsonPath.getString("data"));
    }
    @Test(priority = 13, dependsOnMethods = {"GetAllProducts"}, groups = {"httpRequest", "api", "customer"})
    public void GetPrdReview(){
        String GetPrdReviewResponse = given()
                .header("Content-Type", "application/json")
                .header("Authorization", LoginResponsetoken)
                .when()
                .get(BaseUrl+"/rest/products/"+prdId+"/reviews")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("data.size()", greaterThanOrEqualTo(0))
                .extract().response().asString();
        JsonPath jsonPath = new JsonPath(GetPrdReviewResponse);
        System.out.println("Get Product Cont Review Response: " + GetData.GetReviewDetails(GetPrdReviewResponse));
    }
    @Test(priority = 14, dependsOnMethods = {"GetPrdReview"}, groups = {"httpRequest", "api", "customer"})
    public void AddPrdReview(){
        JSONObject AddPrdReview = new JSONObject()
                .put("message", "This is a test review")
                .put("author", createdEmail);
        System.out.println("Add Product Review: " + AddPrdReview.toString());

        String AddPrdReviewResponse = given()
                .header("Content-Type", "application/json")
                .header("Authorization", LoginResponsetoken)
                .body(AddPrdReview.toString())
                .when()
                .put(BaseUrl+"/rest/products/"+prdId+"/reviews")
                .then()
                .statusCode(201)
                .body("staus", equalTo("success"))
                .extract().response().asString();
        JsonPath jsonPath = new JsonPath(AddPrdReviewResponse);
        System.out.println("Add Product Review Response: " + jsonPath.getString("data"));
    }


}
