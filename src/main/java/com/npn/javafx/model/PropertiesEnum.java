package com.npn.javafx.model;

public enum PropertiesEnum {
    VERSION("версия программы",true),
    DIR_PARSER_NAME("наименование парсера для поиска директории версии", true),
    FILE_PARSER_NAME("наименование парсера для поиска файлов", true),
    UPDATE_LOCATION("расположение директории с версиями", true),
    INI_FILE_NAME("имя INI файла обновления", true),
    EXE_FILE_NAME("имя файла программы которую надо запустить после обновления", false);


    /**конструктор
     *
     * @param description описание назначения свойства (справочное)
     * @param isRequired true если свойство обязательно должно быть в Properties false если свойство опционально
     */
    PropertiesEnum(String description, boolean isRequired) {
        this.description = description;
        this.isRequired = isRequired;
    }

    /**Возвращает обязательность свойства
     *
     * @return true если свойство обязательно должно быть в Properties false если свойство опционально
     */
    public boolean isRequired() {
        return isRequired;
    }

    @Override
    public String toString() {
        return this.name();
    }

    private final String description;
    private final boolean isRequired;
}
