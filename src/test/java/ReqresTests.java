import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTests extends TestBase {

    @Test
    void loginSuccessful() {

        String data = "{ \"email\": \"eve.holt@reqres.in\", " +
                "\"password\": \"cityslicka\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    void loginUnsuccessful() {

        String data = "{ \"email\": \"eve.holt@reqres.in\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void listUsers() {

        given()
                .when()
                .get("api/users?page=1")
                .then()
                .statusCode(200)
                .body("total", is(12))
                .body("data.first_name[0]", is("George"))
                .body("data.last_name[0]", is("Bluth"))
                .body("data.email[0]", is("george.bluth@reqres.in"));
    }

    @Test
    void singleUser() {

        given()
                .when()
                .get("api/users/5")
                .then()
                .statusCode(200)
                .body("data.email", is("charles.morris@reqres.in"))
                .body("data.first_name", is("Charles"))
                .body("data.last_name", is("Morris"));
    }

    @Test
    void singleUserNotFound() {

        given()
                .when()
                .get("api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void listResource() {

        given()
                .when()
                .get("api/unknown")
                .then()
                .statusCode(200)
                .body("total", is(12))
                .body("data.name[0]", is("cerulean"))
                .body("data.year[0]", is(2000))
                .body("data.pantone_value[0]", is("15-4020"));
    }

    @Test
    void singleResource() {

        given()
                .when()
                .get("api/unknown/5")
                .then()
                .statusCode(200)
                .body("data.id", is(5))
                .body("data.name", is("tigerlily"))
                .body("data.year", is(2004))
                .body("data.color", is("#E2583E"))
                .body("data.pantone_value", is("17-1456"));
    }

    @Test
    void singleResourceNotFound() {

        given()
                .when()
                .get("api/unknown/23")
                .then()
                .statusCode(404);
    }

    @Test
    void create() {

        String data = "{ \"name\": \"NewUser\", " +
                "\"job\": \"qa\" }";
        //String response =
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is("NewUser"))
                .body("job", is("qa"))
                .body("createdAt", notNullValue())
        //          .extract().response().asString()
        ;

        //System.out.println("Response: " + response);;
    }

    @Test
    void putUpdate() {

        String data = "{ \"name\": \"NewUser\", " +
                "\"job\": \"qa_automated\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("NewUser"))
                .body("job", is("qa_automated"))
                .body("updatedAt", notNullValue());
    }

    @Test
    void patchUpdate() {

        String data = "{ \"name\": \"NewUser\", " +
                "\"job\": \"qa_automated\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .patch("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("NewUser"))
                .body("job", is("qa_automated"))
                .body("updatedAt", notNullValue());
    }

    @Test
    void delete() {

        given()
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void registerSuccessful() {

        String data = "{ \"email\": \"eve.holt@reqres.in\", " +
                "\"password\": \"pistol\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test
    void registerUnsuccessful() {

        String data = "{ \"email\": \"sydney@fife\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void delayedResponse() {

        given()
                .when()
                .get("/api/users?delay=1")
                .then()
                .statusCode(200)
                .body("total", is(12))
                .body("data.id[0]", is(1))
                .body("data.email[0]", is("george.bluth@reqres.in"))
                .body("data.first_name[0]", is("George"))
                .body("data.last_name[0]", is("Bluth"))
                .body("data.avatar[0]", is("https://reqres.in/img/faces/1-image.jpg"));
    }
}
