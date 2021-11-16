### Заметки по реализации

Файлы считываются построчно, причем к каждой строке
применяется `IssueChecker`, проверяющий наличие 
конкретной проблемы в файле. 
Такая реализация показалась мне лучшей, чем сразу
вычитать весь файл и скармливать его целиком в `IssueChecker`

### How to run JAR file

`java -jar <jar name>.jar <threads amount> <timeout seconds> <temp directory path> <pathCheckers.json path> <report output path>`
