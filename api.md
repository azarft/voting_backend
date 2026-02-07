# Voting System API Documentation

Base URL: `http://localhost:8080` (API Gateway)

## Общие нюансы для Фронтенда
- **CORS**: Разрешен со всех источников (`*`).
- **Авторизация**: Используется заголовок `Authorization: Bearer <token>`.
- **JWT**: Токен содержит `userId` и список `roles` (например, `ROLE_USER`, `ROLE_ADMIN`). Срок жизни — 24 часа.
- **Ошибки**:
    - `401 Unauthorized`: Токен отсутствует, истек или неверен.
    - `403 Forbidden`: Недостаточно прав (нужна роль ADMIN).
    - `204 No Content`: Запрос успешен, но данных нет (например, нет активной сессии).
    - `400 Bad Request`: Ошибка валидации данных.

---

## 1. Authentication Service (Публичный)

### 1.1 Запрос кода входа
Отправляет код подтверждения на указанный email.
- **URL:** `/auth/login`
- **Method:** `POST`
- **Body:**
```json
{
  "email": "user@example.com"
}
```
- **Responses:**
    - `200 OK`: Код отправлен.
    - `400 Bad Request`: Некорректный email.

### 1.2 Верификация кода и получение JWT
Обменивает код из письма на токен доступа.
- **URL:** `/auth/verify`
- **Method:** `POST`
- **Body:**
```json
{
  "email": "user@example.com",
  "code": "123456"
}
```
- **Responses:**
    - `200 OK`: Успех.
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
    ```
    - `401 Unauthorized`: Код неверный или истек.

---

## 2. Voting Service (Для пользователей)

### 2.1 Отправить голос
Голосование за вариант в сессии.
- **URL:** `/votes`
- **Method:** `POST`
- **Auth:** `ROLE_USER`
- **Body:**
```json
{
  "sessionId": 1,
  "optionId": 2
}
```
- **Responses:**
    - `200 OK`: Голос принят в обработку.
    - `401 Unauthorized`: Требуется авторизация.

---

## 3. Result Service (Публичный)

### 3.1 Получить активную сессию
Возвращает текущее голосование, которое доступно для участия.
- **URL:** `/session/active`
- **Method:** `GET`
- **Responses:**
    - `200 OK`: Сессия найдена.
    ```json
    {
      "id": 1,
      "title": "Лучший язык программирования",
      "status": "ACTIVE",
      "options": [
        { "id": 1, "text": "Java" },
        { "id": 2, "text": "Kotlin" },
        { "id": 3, "text": "Python" }
      ]
    }
    ```
    - `204 No Content`: В данный момент нет активного голосования.

### 3.2 Живые результаты
Текущая статистика активного голосования (обновляется раз в 2 секунды на бэкенде).
- **URL:** `/results/live`
- **Method:** `GET`
- **Responses:**
    - `200 OK`:
    ```json
    {
      "Java": 150,
      "Kotlin": 120,
      "Python": 80
    }
    ```

### 3.3 Итоговые результаты последнего голосования
- **URL:** `/results/final`
- **Method:** `GET`
- **Responses:**
    - `200 OK`: Результаты последней закрытой (CLOSED) сессии.

### 3.4 Итоговые результаты конкретной сессии
- **URL:** `/results/final/{sessionId}`
- **Method:** `GET`

---

## 4. Admin Panel (Только ROLE_ADMIN)

### 4.1 Создать сессию
- **URL:** `/admin/session`
- **Method:** `POST`
- **Body:**
```json
{
  "title": "Тема голосования",
  "options": ["Вариант 1", "Вариант 2"]
}
```

### 4.2 Список всех сессий
- **URL:** `/admin/session`
- **Method:** `GET`
- **Response:** `Array<VotingSession>`

### 4.3 Активировать сессию
Делает сессию доступной для голосования. Автоматически закрывает предыдущую активную сессию.
- **URL:** `/admin/session/activate/{id}`
- **Method:** `POST`

### 4.4 Закрыть сессию
Останавливает прием голосов.
- **URL:** `/admin/session/close/{id}`
- **Method:** `POST`

### 4.5 Удалить сессию
Удаление возможно только если сессия не активна.
- **URL:** `/admin/session/{id}`
- **Method:** `DELETE`
