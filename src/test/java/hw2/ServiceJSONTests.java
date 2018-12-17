package hw2;

import core.ServiceResponce;
import core.YandexSpellerApi;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static core.YandexSpellerApi.*;
import static core.YandexSpellerConstants.PARAM_FORMAT;
import static core.YandexSpellerConstants.PARAM_TEXT;
import static core.YandexSpellerConstants.SingleWords.*;
import static enums.Options.*;
import static enums.SelectLanguages.*;
import static enums.TextsData.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.Method.GET;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;


public class ServiceJSONTests {

    private static List<List<ServiceResponce>> responces;

    @Test(description = "Ignore digits option set test")
    public void checkIfDigitsIgnoredTest() {
        with().texts(TEXT_WITH_DIGITS.textsCorrect(), TEXT_WITH_DIGITS.textsIncorrect())
                .language(EN)
                .options(IGNORE_DIGITS.option)
                .callApiTexts()
                .then().specification(successResponse())
                .assertThat()
                .body(Matchers.equalTo("[[],[]]"));
    }

    @Test(description = "GET request with incorrect Language parameter")
    public void sendIncorrectLanguageParameterTest() {
        with().texts(TEXT_WITH_ERRORS.textsIncorrect(), TEXT_WITH_CAPITAL.textsCorrect(), TEXT_WITH_DIGITS.textsCorrect())
                .language(INVALID_LANG)
                .httpMethod(GET)
                .callApi()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.equalTo("SpellerService: Invalid parameter 'lang'"));
    }

    @Test(description = "GET request with incorrect format parameter")
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

    @Test(description = "Ignore capital letters option test")
    public void ignoreCapitalLettersTest() {
        responces = YandexSpellerApi.getYandexSpellerAnswers(
                with().texts(TEXT_WITH_CAPITAL.textsIncorrect(), TEXT_WITH_CAPITAL.textsCorrect())
                        .options(IGNORE_CAPITALIZATION.option)
                        .callApi());
        assertThat("expected number of answers is wrong.", responces.size(), equalTo(0));
    }

    @Test(description = "Mixed errors in the text test (Incorrect letters and missing spacing)")
    public void mixedTextErrorsTest() {
        responces = YandexSpellerApi.getYandexSpellerAnswers(
                with().texts(TEXT_WITH_ERRORS.textsIncorrect(), TEXT_WITH_DIGITS.textsIncorrect())
                        .callApiTexts()
                        .then()
                        .specification(successResponse())
                        .extract().response());
        assertThat(responces.get(0).get(0).s, hasItem(TEXT_WITH_ERRORS.textsCorrect()));
        assertThat(responces.get(1).get(0).s, hasItem(TEXT_WITH_DIGITS.textsCorrect()));
    }

    @Test(description = "Test if sum of options set")
    public void checkIfSumOptionsSet() {
        responces = YandexSpellerApi.getYandexSpellerAnswers(
                with().texts(TEXT_WITH_REPEATED_WORD.textsIncorrect(), TEXT_WITH_URL.textsIncorrect())
                        .options(FIND_REPEAT_WORDS.option + IGNORE_URLS.option)
                        .language(EN)
                        .callApiTexts());
        assertThat(responces, anything("options=12"));
    }

    @Test(description = "Check service responce size")
    public void checkServiceResponceSize() {
        responces = YandexSpellerApi.getYandexSpellerAnswers(
                with().texts(TEXT_WITH_CAPITAL.textsIncorrect(),
                        TEXT_WITH_CAPITAL.textsCorrect(),
                        TEXT_WITH_ERRORS.textsCorrect(),
                        TEXT_WITH_ERRORS.textsIncorrect())
                        .language(UK)
                        .language(RU)
                        .callApiTexts());
        assertThat(responces, hasSize(4));
    }

    @Test(description = "Check if service responce succeed")
    public void checkIfResponceSuccessTest() {
        given(YandexSpellerApi.baseRequestConfiguration())
                .params(PARAM_TEXT, Arrays.asList(YLLOW.value, WHITE.value, GRAY.value))
                .log().all()
                .get().prettyPeek()
                .then().specification(successResponse());
    }

    @Test(description = "Check if service POST responce fails due to long text with 414 error")
    public void checkIfPOSTResponceFails() {
        given(YandexSpellerApi.baseRequestConfiguration())
                .params(PARAM_TEXT, TEXT_ARRAY)
                .log().all()
                .post().prettyPeek()
                .then().specification(failedResponse());
    }

    @Test(description = "Check if service GET responce fails due to long text with 414 error")
    public void checkIfGETResponceFails() {
        given(YandexSpellerApi.baseRequestConfiguration())
                .params(PARAM_TEXT, TEXT_ARRAY)
                .log().all()
                .get().prettyPeek()
                .then().specification(failedResponse())
                .assertThat()
                .body(anything("414 Request-URI Too Large"));
    }
}