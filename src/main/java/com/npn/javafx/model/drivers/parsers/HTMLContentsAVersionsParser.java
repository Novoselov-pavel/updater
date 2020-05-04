package com.npn.javafx.model.drivers.parsers;


import com.npn.javafx.model.interfaces.FilesParser;
import com.npn.javafx.model.interfaces.VersionsParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**Парсер HTML  ищет объект с id "contents" и проходит по всем его подобъектам типа <a href="относительная ссылка"></>
 * например см. адрес https://repo1.maven.org/maven2/io/micronaut/micronaut-runtime/
 */
public class HTMLContentsAVersionsParser implements VersionsParser, FilesParser {

    /**Возвращает список адресов
     *
     * @param path адрес (URL) где располагается список версий
     * @return List со списком версий
     */
    @Override
    public List<String> getVersion(String path) throws Exception {
        List<String> list = new ArrayList<>();

        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36 OPR/67.0.3575.137";
        Document document = Jsoup.connect(path).userAgent(userAgent).referrer("").get();
        Element element = document.getElementById("contents");
        Elements elements = element.select("a");

        elements.forEach(x-> {
            if (x.text().endsWith("/")) {
                list.add(x.text());
            }
        });

        removeExceptionFromList(list);
        return normalization(list);
    }

    /**
     * Получает адрес где размещается список версий
     *
     * @param version номер версии
     * @param path    адрес размещения всех версий
     * @return адрес расположения файлов с версиями
     * @throws Exception при ошибке
     */
    @Override
    public String getAddress(String version, String path) throws Exception {
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36 OPR/67.0.3575.137";
        Document document = Jsoup.connect(path).userAgent(userAgent).referrer("").get();
        Element element = document.getElementById("contents");
        Elements elements = element.select("a");
        Element result = elements.
                                stream().
                                filter(x->x.text().substring(0,x.text().length()-1).equals(version)).
                                findFirst().
                                orElse(null);
        if (result!=null) {
            return getFullAddress(path, result.attr("href"));
        }
        return null;
    }

    /**
     * Получает список адресов файлов  которые требуется скачать из папки с версией
     *
     * @param path фдрес папки с версией
     * @return список адресов
     * @throws Exception
     */
    @Override
    public List<String> getFilesAddress(String path) throws Exception {
        List<String> files = new ArrayList<>();
        List<String> fullPathList = new ArrayList<>();

        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36 OPR/67.0.3575.137";
        Document document = Jsoup.connect(path).userAgent(userAgent).referrer("").get();
        Element element = document.getElementById("contents");
        Elements elements = element.select("a");

        elements.forEach(x-> {
            files.add(x.attr("href"));
        });

        removeExceptionFromList(files);
        files.forEach(x->fullPathList.add(getFullAddress(path,x)));
        return fullPathList;
    }

    /**Создает адрес из двух частей вставляя между ними "/" если в конце первой части этого символа нет.
     * Если вторая чать null вернет null
     * @param firstPart первая часть адреса например "https://openjfx.io/javadoc/11/javafx.graphics"
     * @param secondPart вторая часть адреса например "javafx/scene/Node.html"
     * @return сумму строк "https://openjfx.io/javadoc/11/javafx.graphics/javafx/scene/Node.html".
     * Если вторая чать null вернет null.
     *
     */
    private String getFullAddress(String firstPart, String secondPart) {
        if (secondPart==null) return null;
        String retVal = firstPart.endsWith("/")? firstPart : firstPart + "/";
        retVal += secondPart;
        return retVal;
    }

    /**Удаляет исключения из переданного списка
     *исключения адерса перехода "../"
     * @param strings
     */
    private void removeExceptionFromList(List<String> strings) {
        strings.remove("../");
    }

    /**Нормализация значений (1.2.1/ => 1.2.1)
     *
     * @param strings
     */
    private List<String> normalization(List<String> strings) {
        return strings.stream().map(x-> x.substring(0,x.length()-1)).collect(Collectors.toList());
    }


}
