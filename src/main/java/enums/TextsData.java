package enums;

public enum TextsData {

    TEXT_WITH_REPEATED_WORD ("Paris is capital", "Paris Paris is capital"),
    TEXT_WITH_URL  ("Ignore this", "Ignore this http://google.ru"),
    TEXT_WITH_DIGITS ( "in 1452 of April",  "in1452ofApril"),
    TEXT_WITH_ERRORS ("Test", "Teest" ),
    TEXT_WITH_CAPITAL ("london", "paris");

    private String textsIncorrect;
    private String textsCorrect;


    public String textsCorrect(){return textsCorrect;}
    public String textsIncorrect(){return textsIncorrect;}

    TextsData(String textsCorrect, String textsIncorrect) {
        this.textsCorrect = textsCorrect;
        this.textsIncorrect = textsIncorrect;
    }
}
