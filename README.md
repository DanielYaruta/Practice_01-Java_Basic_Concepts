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
├── smarthome/     # 11 test classes · ~154 tests
├── coffeeshop/    # 10 test classes · ~176 tests
├── taskmanager/   # 10 test classes · ~145 tests
└── util/          #  1 test class  ·  ~44 tests
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

## Tests

519 JUnit 5 tests covering every class, interface, and edge case.

| Package | Classes | Tests |
|---|---|---|
| `smarthome` | 11 | ~154 |
| `coffeeshop` | 10 | ~176 |
| `taskmanager` | 10 | ~145 |
| `util` | 1 | ~44 |

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
