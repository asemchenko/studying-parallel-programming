## Idea description

Суть - приложение, которое на основе текстового файла со списком 
git-репозиториев скачивает их и анализирует их содержимое на предмет
наличия разного рода уязвимостей.

В текущей реализации уязвимости обнаруживаются, путем поиска в файлах
проекта потенциально опасных паттернов (например, инициализация переменной `TOKEN` текстовым выражением`)
Наличие паттернов обнаруживается с помощью регулярных выражений.

