# Объяснение Java-кода с использованием Spring

## Структура проекта

Проект следует архитектуре, основанной на фреймворке **Spring**, с конфигурацией на **Java** и использованием **Gradle** для управления зависимостями.

```
app/
├── src/
│   ├── main/java/
│   │   ├── model/
│   │   │   ├── Product.java
│   │   ├── parser/
│   │   │   ├── CSVParser.java
│   │   ├── provider/
│   │   │   ├── ProductProvider.java
│   │   │   ├── ConcreteProductProvider.java
│   │   ├── reader/
│   │   │   ├── Reader.java
│   │   │   ├── ResourceFileReader.java
│   │   ├── renderer/
│   │   │   ├── ConsoleTableRenderer.java
│   │   │   ├── Renderer.java
│   │   ├── ru/bsuedu/cad/lab/
│   │   │   ├── App.java
│   │   │   ├── AppConfig.java
│   ├── main/resources/
│   │   ├── products.csv
│   ├── test/java/
│   │   ├── AppTest.java
```

## Описание класса

### 1. `Product.java` (Модель данных)
Расположение: `model/Product.java`

Этот класс представляет собой продукт бизнеса с его основными атрибутами.

```java
public class Product {
    private int productId;
    private String name;
    private String description;
    private int categoryId;
    private BigDecimal price;
    private int stockQuantity;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

**Обязанности:**
- Представляет товары с такими атрибутами, как название, цена, запас и т.д.
- Включает конструктор и, возможно, методы getter/setter.

---

### 2. `CSVParser.java` (Разбор CSV)
Расположение: `parser/CSVParser.java`

```java
public class CSVParser implements Parser {
    public List<Product> parse(String data) {
        return Arrays.stream(data.split("\n"))
                .map(line -> line.split(","))
                .map(values -> new Product(
                    Integer.parseInt(values[0]),
                    values[1],
                    values[2],
                    Integer.parseInt(values[3]),
                    new BigDecimal(values[4]),
                    Integer.parseInt(values[5]),
                    values[6],
                    LocalDate.parse(values[7], DateTimeFormatter.ISO_DATE),
                    LocalDate.parse(values[8], DateTimeFormatter.ISO_DATE)))
                .collect(Collectors.toList());
    }
}
```

**Обязанности:**
- Преобразует данные из CSV-файла в объекты `Product`.
- Используйте `LocalDate.parse` для форматирования дат.

---

### 3. `ResourceFileReader.java` (Архивное чтение)
Расположение: `reader/ResourceFileReader.java`

```java
public class ResourceFileReader implements Reader {
    public String read() {
        try {
            return Files.readString(Paths.get("src/main/resources/products.csv"));
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo CSV", e);
        }
    }
}
```

**Обязанности:**
- Считывает файл `products.csv` из `resources` и возвращает его содержимое.
- Обрабатывает исключения в случае ошибок чтения.

---

### 4. `ConcreteProductProvider.java` (Поставщик продукции)
Расположение: `provider/ConcreteProductProvider.java`

```java
public class ConcreteProductProvider implements ProductProvider {
    private final Reader reader;
    private final Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    public List<Product> getGoods() {
        return parser.parse(reader.read());
    }
}
```

**Обязанности:**
- Объедините `Reader` и `Parser`, чтобы получить список товаров.

---

### 5. `ConsoleTableRenderer.java` (Рендеринг консоли)
Расположение: `renderer/ConsoleTableRenderer.java`

```java
public class ConsoleTableRenderer implements Renderer {
    public void render(List<Product> products) {
        System.out.println("+------+--------------------+--------+------+--------------------------------+------------------+");
        System.out.println("| ID   | Nombre             | Precio | Stock | URL                             | Fecha Creación  |");
        System.out.println("+------+--------------------+--------+------+--------------------------------+------------------+");
        for (Product product : products) {
            System.out.printf("| %-4d | %-18s | %-6.2f | %-4d | %-30s | %-16s |%n",
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCreatedAt());
        }
        System.out.println("+------+--------------------+--------+------+--------------------------------+------------------+");
    }
}
```

**Обязанности:**
- Отображает продукты в формате таблицы.

---

### 6. `AppConfig.java` (Конфигурация Spring)
Расположение: `ru/bsuedu/cad/lab/AppConfig.java`

```java
@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
public class AppConfig {
    @Bean
    public Reader resourceFileReader() {
        return new ResourceFileReader();
    }

    @Bean
    public Parser csvParser() {
        return new CSVParser();
    }

    @Bean
    public ProductProvider productProvider() {
        return new ConcreteProductProvider(resourceFileReader(), csvParser());
    }

    @Bean
    public Renderer consoleTableRenderer() {
        return new ConsoleTableRenderer();
    }
}
```

**Обязанности:**
- Определите бобы Spring для каждого компонента.

---

### 7. `App.java` (Main)
Расположение: `ru/bsuedu/cad/lab/App.java`

```java
public class App {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        ConcreteProductProvider provider = context.getBean(ConcreteProductProvider.class);
        ConsoleTableRenderer renderer = context.getBean(ConsoleTableRenderer.class);
        renderer.render(provider.getGoods());
        context.close();
    }
}
```

**Обязанности:**
- Запускает контекст Spring.
- Получает бобы и выполняет рендеринг.

---

# Ответьте на контрольные вопросы

## 1. Spring: Определение, назначение, особенности
Spring — это мощный фреймворк для разработки Java-приложений. Он предоставляет инструменты для упрощения создания сложных корпоративных приложений, включая внедрение зависимостей (DI), управление транзакциями и работу с базами данных.

**Основные особенности Spring:**
- Инверсия управления (IoC)
- Внедрение зависимостей (DI)
- Модульность (разделение на Spring Core, Spring Boot, Spring MVC и др.)
- Поддержка работы с БД (Spring Data)
- Автоматическая настройка с помощью Spring Boot

## 2. Проблемы ручной сборки приложений
Ручная сборка Java-приложений требует:
- Компиляции всех зависимостей вручную
- Указания правильных версий библиотек
- Написания сложных команд для сборки и упаковки проекта
- Отсутствия автоматизированного управления зависимостями

Эти проблемы решаются с помощью систем автоматической сборки, таких как Gradle и Maven.

## 3. Системы автоматической сборки
**Maven** — использует XML (pom.xml) для управления зависимостями и процессом сборки.

**Gradle** — использует DSL (Kotlin или Groovy) для более гибкой настройки сборки.

**Ant** — старый инструмент, требует ручного написания сценариев для сборки.

## 4. Типовая структура Java-проекта
```
project-root/
│── src/
│   ├── main/java/ (основной код)
│   ├── main/resources/ (конфигурационные файлы)
│   ├── test/java/ (тесты)
│   ├── test/resources/ (тестовые данные)
│── build.gradle.kts (файл сборки Gradle)
│── settings.gradle.kts (конфигурация проекта)
│── .gitignore (игнорируемые файлы)
```

## 5. Типы зависимостей в Gradle
- **implementation** — основная зависимость, используется во всех конфигурациях.
- **api** — используется, если библиотеку нужно экспортировать в другие модули.
- **compileOnly** — зависимость используется только во время компиляции.
- **runtimeOnly** — используется только во время выполнения.
- **testImplementation** — зависимость для тестирования.

## 6. Что такое принцип инверсии управления (IoC)?
Принцип инверсии управления (Inversion of Control) — это передача управления созданием объектов контейнеру (например, Spring), а не создание их вручную в коде.

### 7. Разница между IoC и внедрением зависимостей (DI)
- **IoC** — более общий принцип, при котором управление объектами передаётся фреймворку.
- **DI** — частный случай IoC, где объекты передаются другим объектам через конструкторы, сеттеры или интерфейсы.

## 8. Принципы инверсии управления
1. **Dependency Injection (DI)** — внедрение зависимостей.
2. **Service Locator** — объект-контейнер, выдающий нужные зависимости.
3. **Event-Driven Architecture** — система реагирует на события вместо прямого вызова методов.

## 9. Сцепление (Coupling) и связность (Cohesion)
- **Сцепление** — степень зависимости одного модуля от другого (чем ниже, тем лучше).
- **Связность** — насколько функции модуля связаны друг с другом (чем выше, тем лучше).

## 10. Какой принцип управления зависимостями предпочтителен?
Наиболее предпочтительным является **внедрение зависимостей (DI)**, так как оно уменьшает сцепление между компонентами и делает код более гибким и тестируемым.

