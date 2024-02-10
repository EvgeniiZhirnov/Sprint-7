import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
public class GetOrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    public void getOrderListTest(){
        given()
                .header("Content-type", "application/json")
                .when()
                .get("api/v1/orders?limit=10&page=0")
                .then().statusCode(200)
                .and()
                .body("orders.id", hasSize(10));

    }
}
