# Java Basic Concepts — Practice 01

[![Java CI](https://github.com/DanielYaruta/Practice_01-Java_Basic_Concepts/actions/workflows/maven.yml/badge.svg)](https://github.com/DanielYaruta/Practice_01-Java_Basic_Concepts/actions/workflows/maven.yml)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.10-25A162?logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Tests](https://img.shields.io/badge/tests-519%20passed-brightgreen)](#tests)

Three independent Java simulators that cover the core OOP concepts taught in a typical Java fundamentals course — abstract classes, interfaces, inheritance, nested classes, enums, validation, and interactive console I/O.

---

## Simulators

| Package | Domain | Entry point |
|---|---|---|
| [`smarthome`](#smarthome) | Smart home control | `InteractiveSmartHomeDemo` |
| [`coffeeshop`](#coffeeshop) | Coffee shop ordering | `InteractiveCoffeeShopDemo` |
| [`taskmanager`](#taskmanager) | Project & task tracking | `InteractiveTaskManagerDemo` |

---

## Concepts Covered

### OOP Structure
| Concept | Where |
|---|---|
| Abstract class | `SmartDevice`, `MenuItem` |
| Interface | `Controllable`, `Preparable`, `Manageable` |
| Inheritance | `SmartLight`, `SmartThermostat`, `SmartTV` / `Coffee`, `Tea`, `Pastry` |
| `final` methods | Business rules in `SmartDevice`, `Task` that subclasses must not override |
| Non-static inner class | `CoffeeShop.Order` (accesses `CoffeeShop.this.name`), `Project.TaskComparator` |
| Static nested class | `SmartHome.HomeStats`, `CoffeeShop.CoffeeShopStats`, `TaskManager.ProjectStats` |
| Enum with fields | `RoomType`, `Size`, `TaskStatus`, `Coffee.Strength`, `Tea.TeaType` |

### Validation & I/O
| Component | Role |
|---|---|
| `Validate` | Shared utility — `requireNonBlank`, `requireNonNegative`, `requireNonNull` |
| `InputHelper` | Console input with automatic retry on invalid input |
| `InputHelperException` | Thrown when `maxRetries` is exhausted or input stream ends |

---

## Project Structure

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

## Running

```bash
# Run all 519 tests
mvn test

# Interactive demos (use the console to navigate menus)
mvn exec:java -Dexec.mainClass=org.example.InteractiveSmartHomeDemo
mvn exec:java -Dexec.mainClass=org.example.InteractiveCoffeeShopDemo
mvn exec:java -Dexec.mainClass=org.example.InteractiveTaskManagerDemo
```

---

## Interactive Demos

Each demo runs in a loop, reads input from the console via `InputHelper` (automatic retry on invalid input, `maxRetries=3`), and exits cleanly on `0`.

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

- **Add device** — pick type (`SmartLight` / `SmartThermostat` / `SmartTV`) via numbered list, enter name, pick room (`RoomType` enum), optionally turn on immediately.
- **Control device** — device-specific sub-menu: brightness ± (Light), temperature ± (Thermostat), volume ± and channel navigation (TV), or delete with confirmation.
- **Control room** — select a `RoomType`, turn all devices in that room on or off at once.
- **Stats** — `HomeStats`: total devices registered, total turn-on / turn-off events.

`InputHelper` methods used: `readNonBlank` · `readInt(min,max)` · `readEnum` · `readBoolean`

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

Starts with 4 default items (Espresso, Cappuccino, Green Tea, Croissant).

- **Add item** — select type (`Coffee` / `Tea` / `Pastry`), enter name, set price with `readDouble(0.50, 50.0)`, configure strength/type/size (Coffee & Tea) or gluten-free/warm flags (Pastry). Final price = base × `Size.getPriceMultiplier()`.
- **Take order** — enter customer name, optional free-text comment (`readLine`), pick items from numbered menu in a loop, review total, confirm with `readBoolean`.
- **Stats** — `CoffeeShopStats`: total orders · items sold · revenue.

`InputHelper` methods used: `readNonBlank` · `readDouble(min,max)` · `readInt(min,max)` · `readEnum` · `readBoolean` · `readLine`

---

### Task Manager

Two-level navigation: main menu → project menu → task menu.

```
━━━ Task Manager ━━━        ←  main
  1. Список проектов
  2. Новый проект
  3. Открыть проект
  4. Найти проект по названию
  5. Статистика
  0. Выход

━━━ Проект: … ━━━           ←  project
  1. Список задач            5. Запустить проект
  2. Добавить задачу         6. Завершить проект
  3. Открыть задачу          7. Назначить/сменить менеджера
  4. Сортировать задачи      8. Удалить проект

━━━ Задача ━━━              ←  task
  1. Изменить статус         4. Изменить дедлайн
  2. Назначить исполнителя   5. Удалить задачу
  3. Снять исполнителя
```

- **New project** — name (required), description (optional), optional manager assignment.
- **Add task** — title, description, deadline (`YYYY-MM-DD` or Enter for none), optional assignee. Deadline uses `readLine` + manual `DateTimeParseException` loop because `InputHelper` has no `LocalDate` support.
- **Sort tasks** — choose `Criterion` (`DUE_DATE` / `STATUS` / `TITLE`) via `readEnum`; sorted list printed via `Project.TaskComparator` (non-static inner class).
- **Complete project** — warns about pending tasks, requires `readBoolean` confirmation; marks all tasks `DONE`.
- **Find project** — case-insensitive search via `TaskManager.findByName`, offers to open immediately.

`InputHelper` methods used: `readNonBlank` · `readLine` · `readInt(min,max)` · `readEnum` · `readBoolean`

---

## Tests

519 JUnit 5 tests covering every class, interface, and edge case.

**Patterns used:**
- Abstract contract test — `ControllableContractTest`, `ManageableContractTest` run the same assertions for every implementation via JUnit 5 inheritance
- Anonymous subclass — `MenuItemTest` tests the abstract `MenuItem` without coupling to a concrete type
- `@Nested` — groups related scenarios inside a single test class
- `@ParameterizedTest` + `@EnumSource` — exercises every enum constant automatically
- `ByteArrayOutputStream` stdout capture — tests that print to console without polluting test output
- Fixed dates (`LocalDate.of(2000,1,1)` / `LocalDate.of(2099,12,31)`) — eliminates time-dependency in `isOverdue()` tests

---

## Package Details

### smarthome

```
SmartDevice (abstract)
  ├── SmartLight    implements Controllable  → brightness 0–100 %
  ├── SmartThermostat implements Controllable → temperature 15–30 °C
  └── SmartTV       implements Controllable  → volume 0–100, channels

SmartHome
  └── HomeStats (static nested)  totalDevices · turnOnCount · turnOffCount
```

`final` methods in `SmartDevice` — `turnOn()`, `turnOff()`, `isOn()` — enforce consistent state management that subclasses cannot bypass.

### coffeeshop

```
MenuItem (abstract)  ← name, price, getDescription()
  ├── Coffee   implements Preparable  → Strength enum, Size multiplier
  ├── Tea      implements Preparable  → TeaType enum (temp, steep time), Size
  └── Pastry                          → glutenFree, servedWarm flags

CoffeeShop
  ├── Order (non-static inner)       getShopName() → CoffeeShop.this.name
  └── CoffeeShopStats (static nested) totalOrders · totalItems · totalRevenue
```

### taskmanager

```
Task   title · description · dueDate · TaskStatus · assignee
         final isOverdue() · isCompleted() · isAssigned()

Project implements Manageable  (assign / start / complete)
  └── TaskComparator (non-static inner)  describe() → Project.this.name

TaskManager
  ├── findByName(String) → Optional<Project>
  └── ProjectStats (static nested)  snapshot computed at call time
```

### util — InputHelper

```java
try (InputHelper input = new InputHelper(System.in, System.out, 3)) {
    String name   = input.readNonBlank("Name: ");          // retry on blank
    double price  = input.readDouble("Price: ", 0.5, 50);  // retry on invalid / out-of-range
    int    choice = input.readInt("Choice: ", 1, 5);       // retry on invalid / out-of-range
    boolean ok    = input.readBoolean("Confirm?");         // y/yes/n/no, retry otherwise
    Size size     = input.readEnum("Size:", Size.class);   // numbered list, retry on invalid
    String note   = input.readLine("Note: ");              // any input, no retry
}
// InputHelperException thrown when maxRetries exceeded or stream ends
```
