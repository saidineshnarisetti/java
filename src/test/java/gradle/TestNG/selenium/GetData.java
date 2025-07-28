package gradle.TestNG.selenium;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import io.restassured.path.json.JsonPath;

public class GetData {

    public static String GetReviews(String getPrdReviewResponse) {
        JsonPath jsonPath = new JsonPath(getPrdReviewResponse);
        int initialReviewsCount = jsonPath.getInt("data.size()");
        StringBuilder reviews = new StringBuilder();
        reviews.append("Total Reviews: ").append(initialReviewsCount).append("\n");
        for (int i = 0; i < initialReviewsCount; i++) {
            reviews.append("Review ID: Chiled ").append(jsonPath.getString("data[" + i + "]._id")).append("\n");
            reviews.append("Review Text:Chiled ").append(jsonPath.getString("data[" + i + "].message")).append("\n");
        }
        return reviews.toString();
    }
    public static String GetReviewDetails(String getPrdReviewResponse) {
        JsonPath jsonPath = new JsonPath(getPrdReviewResponse);
        int intailreviewscount = jsonPath.getInt("data.size()");
        StringBuilder reviewDetails = new StringBuilder();
        reviewDetails.append("Total Reviews: ").append(intailreviewscount).append("\n");
        for (int i = 0; i < intailreviewscount; i++) {
            reviewDetails.append("Review ID: ").append(jsonPath.getString("data[" + i + "]._id")).append("\n");
            reviewDetails.append("Review Text: ").append(jsonPath.getString("data[" + i + "].message")).append("\n");
        }
        return reviewDetails.toString();
    }

    public static String GetProductDetails(String getAllProductResponse) {
        JsonPath jsonPath = new JsonPath(getAllProductResponse);
        int productsCount = jsonPath.getInt("data.size()");
        StringBuilder productDetails = new StringBuilder();
        productDetails.append("Total Products: ").append(productsCount).append("\n");
        for (int i = 0; i < productsCount; i++) {
            productDetails.append("Product ID: ").append(jsonPath.getString("data[" + i + "].id")).append("\n");
            productDetails.append("Product Name: ").append(jsonPath.getString("data[" + i + "].name")).append("\n");
            productDetails.append("Product Price: ").append(jsonPath.getString("data[" + i + "].price")).append("\n");
        }
        return productDetails.toString();
    }

    public static String GetProductID(String product, String getAllProductResponse) {
        JsonPath jsonPath = new JsonPath(getAllProductResponse);
        int productsCount = jsonPath.getInt("data.size()");
        String prdId="";
        for (int i = 0; i < productsCount; i++) {
            if ((jsonPath.getString("data[" + i+"].name")).equals(product)) {
                System.out.println("Product ID" + (jsonPath.getString("data[" + i + "].id")));
                prdId = (jsonPath.getString("data[" + i + "].id"));
                break;
            }
        }
        return prdId;
    }
}
