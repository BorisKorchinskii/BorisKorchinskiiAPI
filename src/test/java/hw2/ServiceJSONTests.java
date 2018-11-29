package hw2;

import core.ServiceResponce;
import core.YandexSpellerApi;
import enums.SelectLanguages;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static core.YandexSpellerApi.with;
import static core.YandexSpellerConstants.ErrorCodes.ERROR_REPEAT_WORD;
import static core.YandexSpellerConstants.*;
import static core.YandexSpellerConstants.SingleWords.*;
import static enums.Options.*;
import static enums.SelectLanguages.*;
import static enums.TextsData.*;
import static io.restassured.http.Method.GET;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.testng.Assert.*;


public class ServiceJSONTests {

    private static List<List<ServiceResponce>> responces;

    @Test(description = "Ignore digits")
    public void checkIfDigitsIgnoredTest() {
        responces = YandexSpellerApi.getYandexSpellerAnswers(
                with().texts(TEXT_WITH_DIGITS.textsCorrect(), TEXT_WITH_DIGITS.textsIncorrect())
                        .language(EN)
                        .options(IGNORE_DIGITS.option)
                        .callApiTexts());
        assertTrue(responces.get(0).isEmpty());
        assertTrue(responces.get(1).isEmpty());
    }

    @Test(description = "GET request with incorrect Language parameter")
    public void sendIncorrectLanguageParameterTest() {
        with().texts("Тест", "Test", "la prueba")
                .language(INVALID_LANG)
                .httpMethod(GET)
                .callApi()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.equalTo("SpellerService: Invalid parameter 'lang'"));
    }

    @Test(description = "GET request with incorrect incorrect format parameter")
    public void incorrectFormatParameterUsedTest() {
        with().texts(TEXT_WITH_ERRORS.textsIncorrect())
                .format(PARAM_FORMAT)
                .httpMethod(GET)
                .callApi()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.equalTo("SpellerService: Invalid parameter 'format'"));
    }

    @Test(description = "POST request with texts containing errors")
    public void sendTextWithErrorsTest() {
        RestAssured
                .given()
                .queryParam(PARAM_TEXT, TEXT_WITH_ERRORS.textsIncorrect())
                .params(PARAM_LANG, SelectLanguages.EN)
                .accept(ContentType.JSON)
                .and()
                .log().everything()
                .when()
                .get(YANDEX_SPELLER_API_URI_TEXTS)
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(describedAs("Wrong http status code", is(HttpStatus.SC_OK)))
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(TEXT_WITH_ERRORS.textsIncorrect(), TEXT_WITH_ERRORS.textsCorrect())),
                        describedAs("Error code isn't right", containsString("\"code\":1"))))
                .contentType(ContentType.JSON)
                .time(lessThan(20000L));
    }

    @Test(description = "Ignore capital letters")
    public void ignoreCapitalLettersTest() {
        responces = YandexSpellerApi.getYandexSpellerAnswers(
                with().texts(TEXT_WITH_CAPITAL.textsIncorrect(), TEXT_WITH_CAPITAL.textsCorrect())
                        .options(IGNORE_CAPITALIZATION.option)
                        .callApi());
        assertThat("expected number of answers is wrong.", responces.size(), equalTo(0));
    }

    //Fails due empty response
    @Test(description = "Ignore URL setting test")
    public void ignoreURLSettingTest() {
        responces = YandexSpellerApi.getYandexSpellerAnswers(
                with().texts(TEXT_WITH_URL.textsIncorrect())
                        .options(IGNORE_URLS.option)
                        .language(EN)
                        .callApiTexts());
        assertTrue(responces.get(0).get(0).s.contains(TEXT_WITH_URL.textsCorrect()));
    }

    //Fails due empty response
    @Test(description = "Correct repeated words")
    public void correctRepeatedWordsTest() {
        responces = YandexSpellerApi.getYandexSpellerAnswers(
                with().texts("London", "is", "capital", "capital")
                        .options(FIND_REPEAT_WORDS.option)
                        .language(EN)
                        .callApiTexts());
        assertFalse(responces.get(2).isEmpty(), "Responce is empty");
        assertEquals(responces.get(2).get(0).code, ERROR_REPEAT_WORD.code);
        assertEquals(responces.get(2).get(0).word, "capital");
        assertTrue(responces.get(0).get(0).s.contains(TEXT_WITH_REPEATED_WORD.textsCorrect()));
    }

    //Fails due responce size incorrect
    @Test(description = "Check service responce size")
    public void checkServiceResponceSize() {
        responces = YandexSpellerApi.getYandexSpellerAnswers(
                with().texts(TEXT_WITH_CAPITAL.textsIncorrect(),
                        TEXT_WITH_CAPITAL.textsCorrect(),
                        TEXT_WITH_ERRORS.textsCorrect(),
                        TEXT_WITH_ERRORS.textsCorrect())
                        .language(UK)
                        .callApi());
        assertThat(responces, hasSize(4));
        assertThat(responces, not(hasItem(empty())));
    }

    @Test(description = "Check if service responce succeed")
    public void checkIfResponceSuccessTest() {
        RestAssured
                .given(YandexSpellerApi.baseRequestConfiguration())
                .params(PARAM_TEXT, Arrays.asList(YLLOW.value, WHITE.value, GRAY.value))
                .get().prettyPeek()
                .then().specification(YandexSpellerApi.successResponse());
    }
}