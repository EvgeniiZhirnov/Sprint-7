import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

import ForLogin.*;
public class CreateCourierTest {
    private static Integer idCourier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourier() {// тест на создание курьера
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
        idCourier = response.jsonPath().getInt("id");
    }

    @Test
    public void createRepeatCourier() {//тест на дублирование курьера
        Create create  = new Create("Jack", "100500", "Ballbes");
        given()
                .header("Content-type", "application/json")
                .body(create)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201);

        Response response = given()
                .header("Content-type", "application/json")
                .body(create) // заполни body
                .when()
                .post("/api/v1/courier");
        response.then().statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        Login login  = new Login("Ballbes", "100500");
        Response response1 = given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post("/api/v1/courier/login");
        response1.then().statusCode(200)
                .and()
                .body("id", notNullValue());
        idCourier = response1.jsonPath().getInt("id");
    }
    @Test
    public void createWrongCourier() {//тест на создание курьера с незаполненными обязательными полями
        Create create  = new Create("Jack", "", " ");
        given()
                .header("Content-type", "application/json")
                .body(create)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

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


    @After
    public void DeleteCourier() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + idCourier);
    }
}
