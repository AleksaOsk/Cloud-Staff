# Cloud Staff

Cloud Staff - это микросервисное приложение для управления компаниями и их сотрудниками с использованием Spring Cloud
Gateway в качестве API Gateway.

---

## Архитектура проекта

**Проект состоит из трех основных модулей:**

1. **user-server**
	* Отвечает за обработку данных пользователей
	* Предоставляет REST API для управления пользователями
	* Взаимодействует с `company-server` через HTTP-запросы
2. **company-server**
	* Обрабатывает данные компаний
	* Содержит бизнес-логику работы с организациями
	* Обменивается данными с `user-server`
3. **gateway**
	* Реализован на Spring Cloud Gateway
	* Выполняет маршрутизацию запросов между сервисами
	* Обеспечивает единую точку входа в систему

**Ключевые особенности взаимодействия:**

* Межсервисная коммуникация реализована через `RestTemplate` с JSON-сериализацией Jackson
* `CompanyManager` (Company Server) выполняет:
	* Получение списка пользователей компании
	* Удаление пользователей при удалении компании
* `UserManager` (User Server) предоставляет:
	* Получение информации о компании с привязанными пользователями

Сервисы обмениваются DTO-объектами (`UserDto`, `CompanyDto`), что обеспечивает:

* Слабое связывание компонентов
* Гибкость при изменении моделей данных
* Простоту интеграции через REST API

Конфигурация подключения (URL серверов) задаётся через properties-файлы (`@Value("${server.url}")`), что позволяет легко
менять параметры для разных окружений.

Проект запускается с помощью Docker Compose. Приложение будет доступно по адресу: http://localhost:8080.

---

## Технологический стек

- **Язык программирования**: Java 21
- **Фреймворки**: Spring Boot, Spring Data JPA, Spring Cloud Gateway
- **База данных**: PostgreSQL
- **Тестирование**: JUnit 5, Mockito, Slice Tests, Postman
- **Инфраструктура**: Docker, Docker Compose
- **Дополнительно**: Lombok, Validation API, RestTemplate

---

## Функциональность

### Модуль User Service

#### Сущность User

```java

@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String phoneNumber;
    private Long companyId;
}
```

#### API Endpoints:

* `POST /users` - Добавление нового пользователя (проверяем существование компании по `companyId`)
* `PATCH /users/{userId}` - Обновление данных пользователя (при изменении `companyId` проверяем существование компании)
* `GET /users/{userId}` - Получение пользователя по ID
* `GET /users` - Получение списка всех пользователей
* `DELETE /users/{userId}` - Удаление пользователя
* `GET /users/company/{companyId}` - Получение пользователей по ID компании (метод для `company-service`)
* `DELETE /users/company/{companyId}` - Удаление всех пользователей компании (метод для `company-service`)

#### Пример:

`POST /users`\
request

```json
{
  "name": "user1",
  "lastName": "user1",
  "phoneNumber": "9999990000",
  "companyId": 1
}
```

response (201 code)

```json
{
  "id": 1,
  "name": "user1",
  "lastName": "user1",
  "phoneNumber": "79999990000",
  "company": {
    "id": 1,
    "name": "company",
    "budget": 1000000
  }
}
```

#### База данных users-bd

```dbn-sql
CREATE TABLE IF NOT EXISTS USERS(
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    company_id bigint NOT NULL
);
```

#### Особенности

* Можно добавить нового пользователя только если компания существует (проверяем по `companyId`)
* При обновлении данных пользователя, если изменяется `companyId`, проверяем существование новой компании
* Один пользователь может работать только в одной компании

---

### Модуль Company Service

#### Сущность Company

```java

@Entity
@Table(name = "companies", schema = "public")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal budget;
}
```

#### API Endpoints:

* `POST /companies` - Добавление новой компании
* `PATCH /companies/{companyId}` - Обновление данных компании
* `GET /companies/{companyId}` - Получение компании по ID
* `GET /companies` - Получение списка всех компаний
* `DELETE /companies/{companyId}` - Удаление компании (отправляется запрос в `user-service` на каскадное удаление всех
  сотрудников с этим `companyId`)
* `GET /companies/{companyId}/users` - Получение компании с краткой информацией (метод для `user-service`)

#### Пример:

**1**. `POST /companies`\
request

```json
{
  "name": "company1",
  "budget": 1000000
}
```

response (201 code)

```json
{
  "id": 1,
  "name": "company1",
  "budget": 1000000,
  "users": null
}
```

**2**. `GET /companies/{companyId}`\
response (200 code)

```json
{
  "id": 1,
  "name": "company1",
  "budget": 1000000,
  "users": [
    {
      "id": 1,
      "name": "user1",
      "lastName": "user1",
      "phoneNumber": "79999990002"
    },
    {
      "id": 2,
      "name": "user2",
      "lastName": "user2",
      "phoneNumber": "79999990004"
    }
  ]
}
```

#### База данных companies-bd

```dbn-sql
CREATE TABLE IF NOT EXISTS COMPANIES(
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    budget NUMERIC(15, 2) NOT NULL
);
```

#### Особенности

* Могут существовать компании без сотрудников
* При удалении компании каскадно удаляются все ее сотрудники
* В одной компании может быть много сотрудников

---

## Взаимодействие между сервисами

Для взаимодействия между сервисами используется RestTemplate:

#### UserRestClient

```java

@Slf4j
@Service
public class UserRestClient {
    // Реализация методов для работы с пользователями
    public List<UserShortResponseDto> getUsersShortDtoList(long companyId) {
        // Логика получения пользователей компании
    }

    public void deleteUsersByCompanyId(long companyId) {
        // Логика удаления пользователей компании
    }
}
```

#### CompanyRestClient

```java

@Slf4j
@Service
public class CompanyRestClient {
    // Реализация метода для работы с компаниями
    public CompanyShortResponseDto getCompanyDto(long compId) {
        // Логика получения информации о компании
    }
}
```

---

## Тестирование

Проект включает следующие виды тестов:

* Юнит-тесты (JUnit 5 + Mockito)
* Слайс-тесты для контроллеров и сервисов
* Postman-коллекция для тестирования API (postman directory в проекте)

---

## Логирование

В проекте активно используется логирование через SLF4J с Lombok:

```java

@Slf4j
@Service
public class UserService {
    public UserResponseDto addNewUser(UserRequestDto requestDto) {
        log.info("Добавление нового пользователя: {}", requestDto);
        // Логика метода
    }
}
```