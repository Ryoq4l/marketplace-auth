# marketplace-auth

Сервис аутентификации и профиля пользователей для маркетплейса скинов CS.

## Назначение

Отвечает за:
- Вход через Steam (OpenID 2.0 через OIDC-прокси)
- Выдачу JWT access + refresh токенов
- Хранение профиля пользователя (steamId, persona, avatar, trade link)
- Регистрацию в Eureka

## Стек

- Java 21, Spring Boot 4.1
- Spring Security + OAuth2 Client
- JJWT 0.12
- MySQL 8.4, Spring Data JPA, Hibernate
- Spring Cloud Netflix Eureka Client

## Эндпоинты

| Метод | Путь | Описание |
|---|---|---|
| GET | /oauth2/authorization/steam | Старт OAuth2-флоу через Steam |
| GET | /login/oauth2/code/steam | Callback от OIDC-прокси |
| POST | /api/v1/auth/login | Логин по steamId (заглушка для отладки) |
| POST | /api/v1/auth/refresh | Обновление access-токена |
| POST | /api/v1/auth/logout | Отзыв refresh-токена |
| PATCH | /api/v1/auth/me | Обновление профиля (trade link) |

## Переменные окружения

| Переменная | Назначение | Дефолт |
|---|---|---|
| DB_HOST | Хост MySQL | localhost |
| DB_PORT | Порт MySQL | 3306 |
| DB_NAME | Имя базы | auth_db |
| DB_USER | Пользователь БД | market_user |
| DB_PASSWORD | Пароль БД | market_pass |
| EUREKA_HOST | Хост Eureka | localhost |
| EUREKA_PORT | Порт Eureka | 8761 |
| JWT_SECRET | Секрет для подписи JWT | — |
| DDL_AUTO | Hibernate ddl-auto | update |

## Запуск локально

Требуется запущенный MySQL с базой `auth_db` и OIDC-прокси на `localhost:19000`.

\`\`\`bash
mvn spring-boot:run
\`\`\`

## Запуск в Docker

Из корня `marketplace-infra`:

\`\`\`bash
docker compose up --build auth
\`\`\`

## Аутентификация

Все защищённые эндпоинты ожидают заголовок:

\`\`\`
Authorization: Bearer <access_token>
\`\`\`

Access-токен — JWT с claims: `sub` (steamId), `userId`, `roles`. Время жизни — 15 минут.
Refresh-токен — UUID, хранится в БД, время жизни — 7 дней.
