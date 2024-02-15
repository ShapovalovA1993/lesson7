package reqres;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class GetSingleUserTest {
    @Test
    public void getStatusCode() {
        Response response = RestAssured
                .get("https://reqres.in/api/users/2")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);

        Assertions.assertEquals(200, statusCode, "Unexpected status code");
    }

    @Test
    public void getErrorStatusCode() {
        Response response = RestAssured
                .get("https://reqres.in/api/users/23")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);

        Assertions.assertEquals(404, statusCode, "Unexpected status code");
    }

    @Test
    public void getUserId() {
        JsonPath response = RestAssured
                .given()
                .get("https://reqres.in/api/users/2")
                .jsonPath();

        int userId = response.get("data.id");
        System.out.println("User ID: " + userId);

        Assertions.assertEquals(2, userId, "Unexpected user ID");
    }

    @Test
    public void getCacheMaxAge() {
        ValidatableResponse response = RestAssured
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200);

        String cacheControl = response.extract().header("Cache-Control");
        String[] array = cacheControl.split("=");
        int cacheMaxAge = Integer.parseInt(array[1]);
        System.out.println("Max age of cache: " + cacheMaxAge);

        Assertions.assertEquals(14400, cacheMaxAge, "Unexpected max age of cache");
    }

    @Test
    public void getResponseAllFields() {
        JsonPath response = RestAssured
                .given()
                .get("https://reqres.in/api/users/2")
                .jsonPath();

        Map<String, String> responseFields = new HashMap<>();
        responseFields.put("id", response.get("data.id").toString());
        responseFields.put("email", response.get("data.email"));
        responseFields.put("first_name", response.get("data.first_name"));
        responseFields.put("last_name", response.get("data.last_name"));
        responseFields.put("avatar", response.get("data.avatar"));
        responseFields.put("url", response.get("support.url"));
        responseFields.put("text", response.get("support.text"));
        System.out.println(responseFields);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(Integer.parseInt(responseFields.get("id"))).isEqualTo(2);
        softAssertions.assertThat(responseFields.get("email")).isEqualTo("janet.weaver@reqres.in");
        softAssertions.assertThat(responseFields.get("first_name")).isEqualTo("Janet");
        softAssertions.assertThat(responseFields.get("last_name")).isEqualTo("Weaver");
        softAssertions.assertThat(responseFields.get("avatar")).isEqualTo("https://reqres.in/img/faces/2-image.jpg");
        softAssertions.assertThat(responseFields.get("url")).isEqualTo("https://reqres.in/#support-heading");
        softAssertions.assertThat(responseFields.get("text")).isEqualTo("To keep ReqRes free, contributions towards server costs are appreciated!");

        softAssertions.assertAll();
    }
}
