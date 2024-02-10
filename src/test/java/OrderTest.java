import ForOrder.Data;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;

@RunWith(Parameterized.class)
public class OrderTest {
    private static int trackId;
    private final List<String> color;

    public OrderTest(List<String> color){
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getExpected() {
        return new Object[][]{
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of()}
        };
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    public void createOrderTest(){
        Data data = new Data("Jack", "Black", "ulica Pushkina, dom 5", 4, "+7 925 329 44 66", 5, "2024-02-16", "pozvonite za 30 minyt", color);
        Response response = given()
                .header("Content-type", "application/json")
                .body(data)
                .when()
                .post("/api/v1/orders");
                response.then().statusCode(201)
                .and()
                .body("track", notNullValue());
       trackId = response.jsonPath().getInt("track");
        System.out.println(trackId);

            Response response1 = given()
                    .header("Content-type", "application/json")
                    .when()
                    .get("/api/v1/orders/track?t=" + trackId);
            response1.then().statusCode(200)
                    .and()
                    .body("order.color", containsInAnyOrder(color.toArray()));

    }

    @After
    public void cancelOrder(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        given()
                .header("Content-type", "application/json")
                .body(trackId)
                .when()
                .put("/api/v1/orders/cancel");
    }
}
