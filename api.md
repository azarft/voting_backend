# Voting System API Documentation

Base URL: `http://localhost:8080` (API Gateway)

## Общие нюансы для Фронтенда
- **CORS**: Разрешен со всех источников (`*`).
- **Авторизация**: Используется заголовок `Authorization: Bearer <token>` (только для админ-панели).
- **JWT**: Токен содержит `userId` и список `roles` (например, `ROLE_ADMIN`). Срок жизни — 24 часа.
- **Анонимное голосование**: Для голосования **не требуется** авторизация. Идентификация пользователя происходит через Cookie `deviceId`.
- **Ошибки**:
    - `401 Unauthorized`: Токен отсутствует, истек или неверен (для защищенных эндпоинтов).
    - `403 Forbidden`: Недостаточно прав (нужна роль ADMIN).
    - `429 Too Many Requests`: Превышен лимит запросов (1 голос в 10 секунд с одного IP).
    - `204 No Content`: Запрос успешен, но данных нет (например, нет активной сессии).
    - `400 Bad Request`: Ошибка валидации данных.

---

## 1. Authentication Service (Публичный)

### 1.1 Вход (Админ)
Аутентификация администратора по email и паролю.
- **URL:** `/auth/login`
- **Method:** `POST`
- **Body:**
```json
{
  "email": "argen.azanov@alatoo.edu.kg",
  "password": "password121234"
}
```
- **Responses:**
    - `200 OK`: Успех.
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
    ```
    - `401 Unauthorized`: Неверные учетные данные.

---

## 2. Voting Service (Публичный)

### 2.1 Отправить голос
Голосование за вариант в сессии. Не требует JWT.
При первом голосовании бэкенд установит Cookie `deviceId`. Фронтенд должен поддерживать отправку кук (`withCredentials: true`).
- **URL:** `/polls/{id}/vote`
- **Method:** `POST`
- **Body:**
```json
{
  "optionId": 2
}
```
- **Responses:**
    - `200 OK`: Голос принят в обработку.
    - `429 Too Many Requests`: Лимит 1 голос в 10 секунд с одного IP.
    - `400 Bad Request`: Некорректные данные или сессия неактивна.

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
Текущая статистика активного голосования. **Доступно для всех пользователей.**
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
Все эндпоинты требуют заголовок `Authorization: Bearer <token>`.

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

### 4.3 Получить сессию по ID
- **URL:** `/admin/session/{id}`
- **Method:** `GET`

### 4.4 Активировать сессию
Делает сессию доступной для голосования. Автоматически закрывает предыдущую активную сессию.
- **URL:** `/admin/session/activate/{id}`
- **Method:** `POST`

### 4.5 Закрыть сессию
Останавливает прием голосов.
- **URL:** `/admin/session/close/{id}`
- **Method:** `POST`

### 4.6 Удалить сессию
- **URL:** `/admin/session/{id}`
- **Method:** `DELETE`
