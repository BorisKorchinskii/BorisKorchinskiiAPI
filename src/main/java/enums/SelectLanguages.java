package enums;

public enum SelectLanguages {

    RU("ru"),
    EN("en"),
    UK("uk"),
    INVALID_LANG("lu");

    private String languageCode;
    public  String langCode(){return languageCode;}

    SelectLanguages(String lang) {
        this.languageCode  = lang;
    }
}
