package enums;

public enum TextsData {

    TEXT_WITH_REPEATED_WORD ("Paris is capital", "Paris Paris is capital"),
    TEXT_WITH_URL  ("mail@mail.ru", "www.mail.ru"),
    TEXT_WITH_DIGITS ( "В Апреле 1452",  "ВАпреле1452"),
    TEXT_WITH_ERRORS ("Игнорировать", "Игнариравать" ),
    TEXT_WITH_CAPITAL ("london", "paris");

    public static String[] TEXT_ARRAY = new String[3000];

    private String textsIncorrect;
    private String textsCorrect;

    public String textsCorrect(){return textsCorrect;}
    public String textsIncorrect(){return textsIncorrect;}

    TextsData(String textsCorrect, String textsIncorrect) {
        this.textsCorrect = textsCorrect;
        this.textsIncorrect = textsIncorrect;
    }
}
