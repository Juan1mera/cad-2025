
# Вопросы по сервлетам и Spring

## 1. Что такое Servlet и зачем он нужен?

**Servlet** — это Java-класс, который используется для обработки HTTP-запросов (например, GET, POST) и генерации ответов в веб-приложении. Он работает на сервере (например, Tomcat) и является частью спецификации Jakarta EE (ранее Java EE).

**Зачем нужен:**
- Обрабатывает запросы от клиентов (браузеров, приложений) и возвращает ответы (HTML, JSON, файлы и т.д.).
- Позволяет динамически генерировать контент, взаимодействовать с базой данных, вызывать бизнес-логику.
- Является основой для построения веб-приложений на Java (например, `OrderServlet` и `OrderFormServlet`).

**Как работает:**
- Клиент отправляет запрос → сервер (Tomcat) передаёт его сервлету → сервлет обрабатывает запрос (например, через `doGet` или `doPost`) → возвращает ответ.

**Пример:** В твоём `OrderServlet` метод `doGet` генерирует HTML со списком заказов.

---

## 2. Что делает web.xml и зачем он нужен в веб-приложении?

**web.xml** — это файл конфигурации (дескриптор развёртывания) для веб-приложения, который находится в папке `WEB-INF`. Он определяет, как сервер (Tomcat) должен обрабатывать запросы и какие компоненты использовать.

**Что делает:**
- **Маппинг сервлетов:** Указывает, какой сервлет обрабатывает определённый URL (например, `/orders` → `OrderServlet`).
- **Конфигурация слушателей:** Регистрирует классы, реагирующие на события (например, запуск приложения).
- **Параметры контекста:** Определяет глобальные настройки (например, путь к базе данных).

**Зачем нужен:**
- Централизованно управляет конфигурацией приложения.
- Используется в старых приложениях или когда требуется совместимость с серверами без аннотаций.

**Пример (из твоего проекта):**
```xml
<servlet>
    <servlet-name>OrderServlet</servlet-name>
    <servlet-class>ru.bsuedu.cad.app.servlet.OrderServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>OrderServlet</servlet-name>
    <url-pattern>/orders</url-pattern>
</servlet-mapping>
```
- Это говорит Tomcat: "Когда приходит запрос на `/orders`, вызывай `OrderServlet`".

**Современная альтернатива:** Аннотации вроде `@WebServlet` (см. вопрос 9).

---

## 3. Что такое WAR-файл и чем он отличается от JAR?

- **WAR (Web Application Archive):**
  - Это архив для веб-приложений, содержащий сервлеты, классы, ресурсы (HTML, CSS), `web.xml`.
  - Разворачивается на веб-сервере (Tomcat, Jetty).
  - Структура: `WEB-INF/classes`, `WEB-INF/lib`, `WEB-INF/web.xml`, статические файлы.

- **JAR (Java Archive):**
  - Это общий архив для Java-приложений (не обязательно веб).
  - Содержит классы и библиотеки, запускается через `java -jar`.
  - Нет структуры `WEB-INF`, используется для библиотек или standalone-приложений.

**Отличия:**
- **Назначение:** WAR — для веб-приложений, JAR — для библиотек или консольных приложений.
- **Структура:** WAR имеет специфичную структуру (`WEB-INF`), JAR — просто классы и ресурсы.
- **Использование:** WAR развёртывается на сервере, JAR запускается JVM.

**Пример:** Твой `university.war` — это WAR, который Tomcat разворачивает в `/university`.

---

## 4. Что такое ServletContext и как его использовать?

**ServletContext** — это объект, представляющий контекст всего веб-приложения. Он общий для всех сервлетов и предоставляет доступ к глобальным данным и ресурсам.

**Зачем нужен:**
- Хранит параметры конфигурации (из `web.xml`).
- Доступ к ресурсам приложения (например, файлам в `WEB-INF`).
- Обмен данными между сервлетами.

**Как использовать:**
- Получаешь через `getServletContext()` в сервлете.
- Методы:
  - `getInitParameter("param")`: Читает параметры из `web.xml`.
  - `getResourceAsStream("/WEB-INF/file.txt")`: Читает файлы.
  - `setAttribute("key", value)`: Хранит данные.

**Пример (из твоего проекта):**
```java
@Override
public void init() throws ServletException {
    WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    this.orderService = context.getBean(OrderService.class);
}
```
- `getServletContext()` даёт доступ к контексту, чтобы получить Spring `ApplicationContext`.

---

## 5. Чем отличается HttpServletRequest от HttpServletResponse?

- **HttpServletRequest:**
  - Представляет запрос от клиента к серверу.
  - Содержит: параметры (`getParameter`), заголовки (`getHeader`), метод (`GET`, `POST`), URL.
  - Используется для чтения данных от клиента.
  - Пример: `req.getParameter("description")` в `OrderFormServlet`.

- **HttpServletResponse:**
  - Представляет ответ от сервера клиенту.
  - Позволяет: установить тип контента (`setContentType`), писать данные (`getWriter`), отправлять статус (`setStatus`).
  - Используется для отправки ответа.
  - Пример: `resp.getWriter().println("<html>...")` в `OrderServlet`.

**Разница:**
- **Направление:** `Request` — входящие данные, `Response` — исходящие.
- **Роль:** `Request` читает, `Response` пишет.

---

## 6. Какой интерфейс нужно реализовать, чтобы создать Listener, реагирующий на запуск приложения?

Чтобы создать слушатель (Listener), реагирующий на запуск приложения, нужно реализовать интерфейс **`ServletContextListener`** из пакета `jakarta.servlet`.

**Методы:**
- `contextInitialized(ServletContextEvent sce)`: Вызывается при запуске приложения.
- `contextDestroyed(ServletContextEvent sce)`: Вызывается при остановке.

**Пример:**
```java
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletContextEvent;

public class AppStartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Приложение запущено!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Приложение остановлено!");
    }
}
```

**Регистрация в `web.xml`:**
```xml
<listener>
    <listener-class>ru.bsuedu.cad.app.AppStartupListener</listener-class>
</listener>
```

---

## 7. Как получить доступ к Spring ApplicationContext внутри обычного сервлета?

Внутри обычного сервлета (не управляемого Spring) ты можешь получить `ApplicationContext` через `ServletContext` с помощью утилиты `WebApplicationContextUtils`.

**Пример (из твоего `OrderServlet`):**
```java
@Override
public void init() throws ServletException {
    WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    this.orderService = context.getBean(OrderService.class);
}
```

- **`WebApplicationContextUtils.getRequiredWebApplicationContext`:**
  - Берет `ServletContext` и возвращает Spring-контекст.
- **`context.getBean`:**
  - Извлекает бин (например, `OrderService`) для использования в сервлете.

**Зачем:** Чтобы интегрировать Spring (с `@Service`, `@Repository`) с классическими сервлетами.

---

## 8. Что делает ContextLoaderListener в Spring-приложении?

**ContextLoaderListener** — это слушатель из Spring, реализующий `ServletContextListener`. Он инициализирует корневой `ApplicationContext` при запуске приложения.

**Что делает:**
- Создаёт Spring-контекст из конфигурации (например, `ConfigJpa.java` или XML-файла).
- Помещает его в `ServletContext` для доступа другими компонентами.
- Уничтожает контекст при остановке приложения.

**Регистрация (в `web.xml`):**
```xml
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
<context-param>
    <param-name>contextClass</param-name>
    <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
</context-param>
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>ru.bsuedu.cad.app.ConfigJpa</param-value>
</context-param>
```

**Зачем нужен:** Обеспечивает глобальный контекст Spring для всего приложения (например, для сервисов, репозиториев).

---

## 9. Зачем нужно использовать @WebServlet и чем он лучше/хуже конфигурации в web.xml?

**@WebServlet** — это аннотация из Jakarta Servlet API для регистрации сервлета без `web.xml`.

**Пример:**
```java
@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    // ...
}
```

**Зачем нужен:**
- Упрощает конфигурацию: не нужно писать `web.xml`.
- Код и маппинг в одном месте.

**Преимущества:**
- Меньше конфигурации, быстрее писать.
- Подходит для небольших проектов или современных приложений (Servlet 3.0+).

**Недостатки:**
- Меньше гибкости: сложнее управлять централизованно (всё в коде).
- Нет глобального обзора маппингов (в `web.xml` всё видно сразу).
- Не работает в старых контейнерах (до Servlet 3.0).

**Сравнение:**
- **`web.xml`:** Лучше для больших проектов, где нужна централизация и совместимость.
- **`@WebServlet`:** Удобнее для простых приложений или прототипов.

**Твой проект:** Использует `web.xml`, что нормально для структуры с Spring.

---

## 10. Как можно использовать один Spring Bean в нескольких сервлетах?

Чтобы один Spring-бин (например, `OrderService`) использовался в нескольких сервлетах:

1. **Определи бин в Spring-контексте:**
   ```java
   @Service
   public class OrderService {
       // ...
   }
   ```
   - `@Service` делает его бином, управляемым Spring.

2. **Получи бин в сервлетах через ApplicationContext:**
   ```java
   public class OrderServlet extends HttpServlet {
       private OrderService orderService;

       @Override
       public void init() throws ServletException {
           WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
           this.orderService = context.getBean(OrderService.class);
       }
   }

   public class OrderFormServlet extends HttpServlet {
       private OrderService orderService;

       @Override
       public void init() throws ServletException {
           WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
           this.orderService = context.getBean(OrderService.class);
       }
   }
   ```

3. **Почему это работает:**
   - Spring создаёт **один экземпляр** бина (по умолчанию singleton).
   - Каждый сервлет получает ссылку на тот же объект через `getBean`.

**Альтернатива:** Использовать `@Autowired` в сервлетах, если они управляются Spring (с `@WebServlet` и Spring MVC), но в твоём случае с классическими сервлетами используется `init()`.

---
