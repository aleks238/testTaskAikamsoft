
В папке ресурсы находятся:
1. dump-store.sql
2. скрипт sql который использовался для проектирования базы данных
3. тестовые входные файлы
4. тестовые выходные файлы

 Сборка и запуск:
1. Выполнить restore из backup: dump-store.sql
2. В классе DatabaseConnection проверить значения констант: URL,USER,PASSWORD,
  при необходимости прописать свои. В этом случае пересобрать jar используя плагин
  maven-assembly-plugin.
3. Для удобства, чтобы не прописывать полный путь jar файл и .json файлы должны быть на рабочем столе. 
4. Чтобы пользоваться командами ниже нужно переименовать файлы:jar файл в папке target на test.jar, входной файл поиска на searchCriteria.json, входной файл статистики на statCriteria.json:
* Команды:
* Поиск по критериям: java -jar test.jar search outputSearch.json searchCriteria.json
* Получить статистику: java -jar test.jar stat outputStat.json statCriteria.json