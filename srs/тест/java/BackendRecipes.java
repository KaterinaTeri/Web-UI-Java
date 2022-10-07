import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.lessThan;

public class BackendRecipes extends BackendBeforAll {

    @Test
    void getRecipeWithQueryParametersPositiveTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information")
                .then()
                .statusCode(200);
    }

    @Test
    void testGet1() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .pathParams("id", 1408)
                .when()
                .get(getBaseUrl()+"recipes/{id}/information")
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void testGet2() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .pathParams("id", 716437)
                .when()
                .get(getBaseUrl()+"recipes/{id}/information")
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void testGet3() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .expect()
                .body("title", equalTo("Chilled Cucumber Avocado Soup with Yogurt and Kefir"))
                .when()
                .get(getBaseUrl()+"recipes/716437/information")
                .prettyPeek();
    }

    @Test
    void testGet4() {
        JsonPath response = (JsonPath) given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .when()
                .get(getBaseUrl()+"recipes/716437/information")
                .body()
                .jsonPath()
                .prettyPeek();
        assertThat(response.get("vegetarian"), is(true));
    }

    @Test
    void testGet5() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .response()
                .contentType(ContentType.JSON)
                .time(lessThan(2000L))
                .header("Connection", "keep-alive")
                .expect()
                .body("extendedIngredients[0].name", equalTo("avocado"))
                .when()
                .get("https://api.spoonacular.com/recipes/716437/information")
                .prettyPeek();
    }

    @Test
    void testPost1() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Korean Beef Rice Bowl")
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .then()
                .statusCode(200);
    }

    @Test
    void testPost2() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("ingredientList", "chicken")
                .when().post(getBaseUrl()+"recipes/visualizeIngredients")
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void testPost3() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("ingredientList", "apple")
                .when().post(getBaseUrl()+"recipes/visualizeTaste")
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void testPost4() {
        String cuisine = given()
                .queryParam("apiKey", getApiKey())
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .path("cuisine");

        System.out.println("cuisine: " + cuisine);
    }

    @Test
    void testPost5() {
        String id = given()
                .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
                .queryParam("apiKey", getApiKey())
                .body("{\n"
                        + " \"date\": 06102022, \n"
                        + " \"slot\": 1, \n"
                        + " \"position\": 0, \n"
                        + " \"type\": \"INGREDIENTS\", \n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 banana\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/geekbrains/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
        given()
                .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
                .queryParam("apiKey", getApiKey())
                .delete("https://api.spoonacular.com/mealplanner/geekbrains/items/" + id)
                .then()
                .statusCode(200);
    }
}
