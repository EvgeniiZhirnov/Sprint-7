import ForLogin.Create;
import ForLogin.Login;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void loginCourier() {// тест на логин курьера
        Create create  = new Create("Jack", "100500", "Ballbes");
        given()
                .header("Content-type", "application/json")
                .body(create)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201)
                .and()
                .body("ok", equalTo(true));

        Login login  = new Login("Ballbes", "100500");
        Response response = given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post("/api/v1/courier/login");
        response.then().statusCode(200)
                .and()
                .body("id", notNullValue());
        int idCourier = response.jsonPath().getInt("id");

        Response response1 = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + idCourier);
        response1.then().statusCode(200)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    public void loginWrongCourier() {//тест на логин курьера с незаполненными обязательными полями
        Login login  = new Login(" ", "");
        Response response = given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post("/api/v1/courier/login");
        response.then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginNonexistentCourier() {//тест на логин за несуществующего курьера
        Login login = new Login("Afonia", "222333");
        given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
