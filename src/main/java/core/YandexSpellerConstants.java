package core;

public class YandexSpellerConstants {
    public static final String YANDEX_SPELLER_API_URI_TEXTS = "https://speller.yandex.net/services/spellservice.json/checkTexts";
    public static final String YANDEX_SPELLER_API_URI = "https://speller.yandex.net/services/spellservice.json/checkText";
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_OPTIONS = "options";
    public static final String PARAM_LANG = "lang";
    public static final String PARAM_FORMAT = "format";

    public enum ErrorCodes {

        ERROR_UNKNOWN_WORD(1),
        ERROR_REPEAT_WORD(2),
        ERROR_CAPITALIZATION(3),
        ERROR_TOO_MANY_ERRORS(4);

        public Integer code;

        ErrorCodes(Integer code) {
            this.code = code;
        }
    }

    public enum SingleWords {

        YLLOW("Yellow"),
        WHITE("White"),
        GRAY("Gray");

        public String value;

        SingleWords(String value) {
            this.value = value;
        }
    }
}
