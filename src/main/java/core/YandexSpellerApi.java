package core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enums.SelectLanguages;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static core.YandexSpellerConstants.*;
import static io.restassured.http.Method.POST;
import static org.hamcrest.Matchers.lessThan;

public class YandexSpellerApi {

   //builder pattern
    private YandexSpellerApi() {
    }

    public Method method = POST;

    private HashMap<String, String> params = new HashMap<>();
    //for checkTexts
    private HashMap<String, List<String>> texts = new HashMap<>();

    public static class ApiBuilder {
        YandexSpellerApi spellerApi;

        private ApiBuilder(YandexSpellerApi gcApi) {
            spellerApi = gcApi;
        }

        public ApiBuilder text(String text) {
            spellerApi.params.put(PARAM_TEXT, text);
            return this;
        }

        //for checkTexts
        public ApiBuilder texts(String... texts) {
            spellerApi.texts.put(PARAM_TEXT, Arrays.asList(texts));
            return this;
        }


        public ApiBuilder options(String options) {
            spellerApi.params.put(PARAM_OPTIONS, options);
            return this;
        }

        public ApiBuilder language(SelectLanguages language) {
            spellerApi.params.put(PARAM_LANG, language.langCode());
            return this;
        }

        public ApiBuilder format(String format) {
            spellerApi.params.put(PARAM_FORMAT, format);
            return this;
        }

        public ApiBuilder httpMethod(Method requestMethod) {
            spellerApi.method = requestMethod;
            return this;
        }

        public Response callApi() {
            return RestAssured.with()
                    .queryParams(spellerApi.params)
                    .log().all()
                    .get(YANDEX_SPELLER_API_URI).prettyPeek();
        }

        public Response callApiTexts() {
            return RestAssured.with()
                    .queryParams(spellerApi.params)
                    .queryParams(spellerApi.texts)
                    .log().all()
                    .get(YANDEX_SPELLER_API_URI_TEXTS).prettyPeek();
        }


    }

    public static ResponseSpecification successResponse() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(20000L))
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

    public static RequestSpecification baseRequestConfiguration() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .setBaseUri(YANDEX_SPELLER_API_URI)
                .build();
    }

    public static ApiBuilder with() {
        YandexSpellerApi api = new YandexSpellerApi();
        return new ApiBuilder(api);
    }

    public static List<List<ServiceResponce>> getYandexSpellerAnswers(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<Collection<List<ServiceResponce>>>() {
        }.getType());
    }
}
