# updater
<h3>v.1.0.0<h3>
<h4>Учебный проект</h4>

Выполняет поиск новой версии по файлу настроек, скачивание и обновление программы
Запускается в трех режимах:
<ol>
<li>
Режим с указанием в командной строке файла с настройками см. fileUpdateSetting.xml
Будет произведен поиск более новой версии, чем указанная в файле.
Номер версии будет выведен в стандартный поток вывода
</li>
<li>
Режим с указанием в командной строке номера версии или 'auto' и файла с настройками см. fileUpdateSetting.xml
Будет произведено скачивание файла требуемой версии и распаковка с проверкой CRC
</li>
<li>
Режим без указания переменных командной строки.
Будет открыт GUI на JavaFX для создания архива указанной версии и соответсвующего ini файла см. iniFile.xml.
</li>
</ol>

Использованы: JAXAB , Slf4j, JavaFX, JSOUP.