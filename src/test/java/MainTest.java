import data.NewPet;
import data.NewUserWithList;
import helper.Specification;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static data.Data.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class MainTest {

    //Pet. Add a new pet to project
    @Test
    @Order(1)
    public void testPetPost() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec200());
        NewPet newPet = new NewPet(PET_ID, PET_NAME, PHOTO_URLS);
        JsonPath jsonPath = given()
                .body(newPet)
                .when()
                .post(ENDPOINT_PET)
                .then()
                .log().body()
                .and()
                .extract().jsonPath();
        assertEquals(PET_ID, jsonPath.getInt("id"));
        assertEquals(PET_NAME, jsonPath.get("name"));

    }

    //Pet. Update an existing pet
    @Test
    @Order(2)
    public void testPetPut() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec200());
        NewPet newPet = new NewPet(PET_ID, PET_NAME_RU, PHOTO_URLS);
        JsonPath jsonPath = given()
                .body(newPet)
                .when()
                .put(ENDPOINT_PET)
                .then()
                .log().body()
                .and()
                .extract().jsonPath();
        assertEquals(PET_ID, jsonPath.getInt("id"));
        assertEquals(PET_NAME_RU, jsonPath.get("name"));

    }

    //Pet. Find pet by ID
    @Test
    @Order(3)
    public void testPetGet() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec200());
        JsonPath jsonPath = given()
                .when()
                .get(ENDPOINT_PET + PET_ID)
                .then()
                .log().body()
                .and()
                .extract().jsonPath();
        assertEquals(PET_ID, jsonPath.getInt("id"));
        assertEquals(PET_NAME_RU, jsonPath.get("name"));

    }

    //Pet. Deletes a pet
    @Test
    @Order(4)
    public void testPetDelete() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec200());
        String message = given()
                .when()
                .delete(ENDPOINT_PET + PET_ID)
                .then()
                .log().body()
                .and()
                .extract().jsonPath().getString("message");
        assertEquals(PET_ID, Integer.parseInt(message));

    }

    //User. Creates list of users with given input array
    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
    @Order(5)
    public void testUserPost(int id,
                             String username,
                             String firstName,
                             String email,
                             String password,
                             String phone,
                             int userStatus) {

        NewUserWithList newUserWithList =
                new NewUserWithList(id, username, firstName, email, password, phone, userStatus);
        List<NewUserWithList> newUserWithListArrayList = new ArrayList<>();
        newUserWithListArrayList.add(newUserWithList); //Создает объект и кладет в ArrayList

        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec200());
        String message = given()
                .body(newUserWithListArrayList)
                .when()
                .post(ENDPOINT_USER + "createWithArray")
                .then()
                .log().body()
                .and()
                .extract().jsonPath().getString("message");
        assertEquals(OK, message);
    }

    //User. Get user by user name
    @Test
    @Order(6)
    public void testUserGet() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec200());
        Map<String, String> response = given()
                .when()
                .get(ENDPOINT_USER + USER_NAME)
                .then()
                .log().body()
                .and()
                .extract().jsonPath().getMap("");
        assertEquals(USER_NAME, response.get("username"));

    }

    //Pet. Delete user. 404 User not found
    @Test
    @Order(7)
    public void testUserDelete() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec404());
        given()
                .when()
                .delete(ENDPOINT_USER + USER_NAME_NO)
                .then()
                .log().body();

    }

}
