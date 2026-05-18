import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.RestAssured.given;


public class SelenoidTests extends TestBase {

    @Test
    @DisplayName("Проверка наличия текста версии Selenoid в ответе")
    public void textTest() {
        given()
                .log().uri()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().body()
                .statusCode(200)
                .body("value.message", containsString("Selenoid 1.11.3 built at 2024-05-25_12:34:40PM"));
    }

    @Test
    @DisplayName("Проверка статуса готовности ready: true")
    public void readyStatusTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .statusCode(200)
                .body("value.ready", is(true));
    }

    @Test
    @DisplayName("Валидация JSON-схемы ответа")
    public void jsonSchemaTest() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("selenoid-status-schema.json"));
    }

    @Test
    @DisplayName("Негативный тест: запрос без авторизации")
    public void notAuthorizedTest() {
        given()
                .log().uri()
                .when()
                .get("/wd/hub/status")
                .then()
                .log().status()
                .statusCode(401);
    }

    @Test
    @DisplayName("Негативный тест: запрос с неверным паролем")
    public void wrongPasswordTest() {
        given()
                .log().uri()
                .auth().basic("user1", "123")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().status()
                .statusCode(401);
    }
}
