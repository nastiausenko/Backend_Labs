# Лабораторна робота №2

## Запуск проєкту

> NOTE: Для запуску проєкту на комп'ютері повинні бути встановлені [Docker](https://www.docker.com/) та docker-compose

1. Склонуйте репозиторій, використовуючи команду:

```
git clone https://github.com/nastiausenko/Backend_Lab2.git
```

2. Введіть наступну команду для створення образу (image):

```
docker build -t backend .
```
3. Для запуску контейнера Docker введіть команду:

```
docker-compose up
```
Після запуску проєкт буде доступний за посиланням [http://localhost:8080/api/v1](http://localhost:8080/api/v1)

### [Посилання на деплой](https://backend-lab2-0dzb.onrender.com/api/v1)