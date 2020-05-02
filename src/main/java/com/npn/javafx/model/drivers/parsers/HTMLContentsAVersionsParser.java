package com.npn.javafx.model.drivers.parsers;


import com.npn.javafx.model.interfaces.VersionsParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;

/**Парсер HTML  ищет объект с id "contents" и проходит по всем его подобъектам типа <a href="относительная ссылка"></>
 * например см. адрес https://repo1.maven.org/maven2/io/micronaut/micronaut-runtime/
 */
public class HTMLContentsAVersionsParser implements VersionsParser {

    /**Возвращает список адресов
     *
     * @param path адрес (URL) где располагается список версий
     * @return List со списком версий
     */
    @Override
    public List<String> getVersion(String path) throws Exception {
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36 OPR/67.0.3575.137";
        Document document = Jsoup.connect(path).userAgent(userAgent).referrer("").get();

        ///TODO закончил тут






        return null;
    }
}
