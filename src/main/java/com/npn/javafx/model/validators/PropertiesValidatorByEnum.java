package com.npn.javafx.model.validators;

import com.npn.javafx.model.PropertiesEnum;
import com.npn.javafx.model.interfaces.PropertiesValidator;

import java.util.Properties;

/**Проверяет правильность настроек согласно значениям из {@link com.npn.javafx.model.PropertiesEnum}
 *
 */
public class PropertiesValidatorByEnum implements PropertiesValidator {

    /**Проверяет правильность файла настроек
     *
     * @param properties настройки
     * @return true если настроки правильные false если неверные
     */
    @Override
    public boolean isPropertiesValid(Properties properties) {

        for (PropertiesEnum value : PropertiesEnum.values()) {
            if (value.isRequired()) {
                if (properties.getProperty(value.toString())==null) {
                    return false;
                }
            }
        }
        return true;
    }
}
