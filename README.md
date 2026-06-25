# Java Basic Concepts — Practice 01

[![Java CI](https://github.com/DanielYaruta/Practice_01-Java_Basic_Concepts/actions/workflows/maven.yml/badge.svg)](https://github.com/DanielYaruta/Practice_01-Java_Basic_Concepts/actions/workflows/maven.yml)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.10-25A162?logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Tests](https://img.shields.io/badge/tests-519%20passed-brightgreen)](#тесты)

Три независимых Java-симулятора, охватывающих основные концепции ООП из типичного курса по Java: абстрактные классы, интерфейсы, наследование, вложенные классы, перечисления, валидация и интерактивный ввод с консоли.

---

## Симуляторы

| Пакет | Предметная область | Точка входа |
|---|---|---|
| [`smarthome`](#smarthome) | Управление умным домом | `InteractiveSmartHomeDemo` |
| [`coffeeshop`](#coffeeshop) | Заказы в кофейне | `InteractiveCoffeeShopDemo` |
| [`taskmanager`](#taskmanager) | Управление проектами и задачами | `InteractiveTaskManagerDemo` |

---

## Концепции

### Структура ООП

| Концепция | Где применяется |
|---|---|
| Абстрактный класс | `SmartDevice`, `MenuItem` |
| Интерфейс | `Controllable`, `Preparable`, `Manageable` |
| Наследование | `SmartLight`, `SmartThermostat`, `SmartTV` / `Coffee`, `Tea`, `Pastry` |
| `final`-методы | Бизнес-правила в `SmartDevice`, `Task` — подклассы не могут переопределить |
| Нестатический внутренний класс | `CoffeeShop.Order` (обращается к `CoffeeShop.this.name`), `Project.TaskComparator` |
| Статический вложенный класс | `SmartHome.HomeStats`, `CoffeeShop.CoffeeShopStats`, `TaskManager.ProjectStats` |
| Enum с полями | `RoomType`, `Size`, `TaskStatus`, `Coffee.Strength`, `Tea.TeaType` |

### Валидация и ввод

| Компонент | Роль |
|---|---|
| `Validate` | Общая утилита — `requireNonBlank`, `requireNonNegative`, `requireNonNull` |
| `InputHelper` | Консольный ввод с автоматическим повтором при неверном вводе |
| `InputHelperException` | Бросается при исчерпании `maxRetries` или конце потока ввода |

---

## Структура проекта

```
src/
├── main/java/org/example/
│   ├── Validate.java
│   ├── util/
│   │   ├── InputHelper.java
│   │   └── InputHelperException.java
│   ├── smarthome/
│   │   ├── SmartDevice.java          # abstract
│   │   ├── Controllable.java         # interface
│   │   ├── SmartLight.java
│   │   ├── SmartThermostat.java
│   │   ├── SmartTV.java
│   │   ├── SmartHome.java            # + static nested HomeStats
│   │   └── RoomType.java             # enum
│   ├── coffeeshop/
│   │   ├── MenuItem.java             # abstract
│   │   ├── Preparable.java           # interface
│   │   ├── Coffee.java               # + inner enum Strength
│   │   ├── Tea.java                  # + inner enum TeaType
│   │   ├── Pastry.java
│   │   ├── CoffeeShop.java           # + inner Order, static CoffeeShopStats
│   │   └── Size.java                 # enum
│   └── taskmanager/
│       ├── Task.java
│       ├── Manageable.java           # interface
│       ├── Project.java              # + inner TaskComparator
│       ├── TaskManager.java          # + static nested ProjectStats
│       └── TaskStatus.java           # enum
├── InteractiveSmartHomeDemo.java
├── InteractiveCoffeeShopDemo.java
└── InteractiveTaskManagerDemo.java

src/test/java/org/example/
├── smarthome/
├── coffeeshop/
├── taskmanager/
└── util/
```

---

## Интерактивные демо

Каждое демо работает в цикле, читает ввод через `InputHelper` (автоповтор при неверном вводе, `maxRetries=3`) и завершается по `0`.

### Smart Home

```
━━━ Главное меню ━━━
  1. Добавить устройство
  2. Список устройств
  3. Управление устройством
  4. Управление комнатой
  5. Статистика
  0. Выход
```

- **Добавить устройство** — выбор типа (`SmartLight` / `SmartThermostat` / `SmartTV`) из нумерованного списка, ввод имени, выбор комнаты (`RoomType`), опциональное включение сразу.
- **Управление устройством** — подменю, специфичное для типа: яркость ± (свет), температура ± (термостат), громкость ± и навигация по каналам (ТВ), удаление с подтверждением.
- **Управление комнатой** — выбор `RoomType`, включение или выключение всех устройств в комнате разом.
- **Статистика** — `HomeStats`: зарегистрировано устройств, событий включения / выключения.

Методы `InputHelper`: `readNonBlank` · `readInt(min,max)` · `readEnum` · `readBoolean`

---

### Coffee Shop

```
━━━ Меню кофейни ━━━
  1. Посмотреть меню
  2. Добавить позицию в меню
  3. Принять заказ
  4. История заказов
  5. Статистика продаж
  0. Закрыть кофейню
```

Стартует с 4 позициями по умолчанию (Espresso, Cappuccino, Green Tea, Croissant).

- **Добавить позицию** — тип (`Coffee` / `Tea` / `Pastry`), название, цена через `readDouble(0.50, 50.0)`, крепость/сорт/размер (кофе и чай) или флаги без глютена/тёплым (выпечка). Итоговая цена = base × `Size.getPriceMultiplier()`.
- **Принять заказ** — имя клиента, опциональный комментарий (`readLine`), выбор позиций из меню в цикле, просмотр итога, подтверждение через `readBoolean`.
- **Статистика** — `CoffeeShopStats`: оформлено заказов · продано позиций · выручка.

Методы `InputHelper`: `readNonBlank` · `readDouble(min,max)` · `readInt(min,max)` · `readEnum` · `readBoolean` · `readLine`

---

### Task Manager

Двухуровневая навигация: главное меню → меню проекта → меню задачи.

```
━━━ Task Manager ━━━        ←  главное
  1. Список проектов
  2. Новый проект
  3. Открыть проект
  4. Найти проект по названию
  5. Статистика
  0. Выход

━━━ Проект: … ━━━           ←  проект
  1. Список задач            5. Запустить проект
  2. Добавить задачу         6. Завершить проект
  3. Открыть задачу          7. Назначить/сменить менеджера
  4. Сортировать задачи      8. Удалить проект

━━━ Задача ━━━              ←  задача
  1. Изменить статус         4. Изменить дедлайн
  2. Назначить исполнителя   5. Удалить задачу
  3. Снять исполнителя
```

- **Новый проект** — название (обязательно), описание (опционально), опциональное назначение менеджера.
- **Добавить задачу** — название, описание, дедлайн (`ГГГГ-ММ-ДД` или Enter без дедлайна), исполнитель. Дедлайн читается через `readLine` + ручной цикл с `DateTimeParseException`, так как `InputHelper` не знает о `LocalDate`.
- **Сортировать задачи** — выбор критерия (`DUE_DATE` / `STATUS` / `TITLE`) через `readEnum`; сортировка через `Project.TaskComparator` (нестатический внутренний класс).
- **Завершить проект** — предупреждение о незавершённых задачах, подтверждение через `readBoolean`; все задачи переводятся в `DONE`.
- **Найти проект** — поиск без учёта регистра через `TaskManager.findByName`, предложение открыть сразу.

Методы `InputHelper`: `readNonBlank` · `readLine` · `readInt(min,max)` · `readEnum` · `readBoolean`
