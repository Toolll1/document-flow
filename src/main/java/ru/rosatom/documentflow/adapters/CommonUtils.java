package ru.rosatom.documentflow.adapters;

import org.springframework.stereotype.Component;

@Component
public class CommonUtils {
    public static final String PAGINATION_DEFAULT_FROM = "0";
    public static final String PAGINATION_DEFAULT_SIZE = "10";
    public static final String CHANGE_TITLE =
            "Изменен заголовок документа пользователем id-%d. Старая версия: '%s'. Новая версия: '%s'. ";
    public static final String CHANGE_DATE =
            "Изменена дата документа. пользователем id-%d. Старая версия: '%tD'. Новая версия: '%tD'. ";
    public static final String CHANGE_DOCUMENT_PATH =
            "Добавлена новая версия документа пользователем id-%d. Старая версия: '%s'. Новая версия: '%s'. ";
    public static final String CHANGE_DOCUMENT_TYPE =
            "Изменен тип документа пользователем id-%d. Старая версия: тип id-'%d'. Новая версия: тип id-'%d'. ";
    public static final String CHANGE_DOCUMENT_ATTRIBUTES =
            "Изменены значения атрибутов пользователем id-%d. Старая версия: '%s'. Новая версия: '%s'. ";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


}
