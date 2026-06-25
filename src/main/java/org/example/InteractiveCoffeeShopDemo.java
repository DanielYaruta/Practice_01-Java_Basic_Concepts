package org.example;

import org.example.coffeeshop.*;
import org.example.util.InputHelper;
import org.example.util.InputHelperException;

import java.util.ArrayList;
import java.util.List;

/**
 * Интерактивное демо кофейни.
 *
 * InputHelper-методы, задействованные здесь:
 *   readNonBlank      — название кофейни, название позиции, имя клиента
 *   readDouble(min,max) — цены (метод, которого нет в SmartHome демо)
 *   readInt(min,max)  — пункты меню, выбор позиции в заказе
 *   readEnum          — тип позиции, крепость/сорт, размер
 *   readBoolean       — без глютена, тёплое, подтверждение заказа
 *   readLine          — опциональный комментарий к заказу (может быть пустым)
 */
public class InteractiveCoffeeShopDemo {

    // Тип новой позиции меню — внутренний enum со своим toString для readEnum
    enum ItemType {
        COFFEE("Coffee / Кофе"),
        TEA("Tea    / Чай"),
        PASTRY("Pastry / Выпечка");

        private final String label;
        ItemType(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    private final InputHelper    input;
    private       CoffeeShop     shop;
    // Локальная копия меню — getMenu() в CoffeeShop package-private
    private final List<MenuItem> localMenu = new ArrayList<>();

    public InteractiveCoffeeShopDemo(InputHelper input) {
        this.input = input;
    }

    // ── Точка входа ────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║  Coffee Shop — Interactive Demo      ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(maxRetries=3: 4 попытки на каждый ввод)\n");

        try (InputHelper ih = new InputHelper(System.in, System.out, 3)) {
            new InteractiveCoffeeShopDemo(ih).run();
        } catch (InputHelperException e) {
            System.err.println("\n[!] Слишком много неверных вводов: " + e.getMessage());
        }
    }

    // ── Запуск ─────────────────────────────────────────────────────────────────

    public void run() {
        // readNonBlank: пустое название недопустимо
        String name = input.readNonBlank("Название кофейни: ");
        shop = new CoffeeShop(name);
        loadDefaultMenu();
        System.out.println("Кофейня «" + name + "» открыта! В меню " + localMenu.size() + " позиции по умолчанию.\n");

        boolean running = true;
        while (running) {
            printMainMenu();
            // readInt с диапазоном [0..5]
            int choice = input.readInt("Выбор: ", 0, 5);
            System.out.println();
            switch (choice) {
                case 1 -> showMenu();
                case 2 -> addMenuItem();
                case 3 -> takeOrder();
                case 4 -> showOrders();
                case 5 -> shop.getStats().printReport();
                case 0 -> { System.out.println("Кофейня закрыта. До свидания!"); running = false; }
            }
            if (choice != 0) System.out.println();
        }
    }

    // ── Заполнение меню по умолчанию (без InputHelper) ────────────────────────

    private void loadDefaultMenu() {
        addToShop(new Coffee("Espresso",   2.50, Coffee.Strength.STRONG,  Size.SMALL));
        addToShop(new Coffee("Cappuccino", 3.00, Coffee.Strength.MEDIUM,  Size.MEDIUM));
        addToShop(new Tea("Green Tea",     2.00, Tea.TeaType.GREEN,        Size.MEDIUM));
        addToShop(new Pastry("Croissant",  2.50, false, true));
    }

    private void addToShop(MenuItem item) {
        shop.addToMenu(item);
        localMenu.add(item);
    }

    // ── Главное меню ───────────────────────────────────────────────────────────

    private void printMainMenu() {
        System.out.println("━━━ Меню кофейни ━━━");
        System.out.println("  1. Посмотреть меню");
        System.out.println("  2. Добавить позицию в меню");
        System.out.println("  3. Принять заказ");
        System.out.println("  4. История заказов");
        System.out.println("  5. Статистика продаж");
        System.out.println("  0. Закрыть кофейню");
    }

    // ── 1. Показ меню ──────────────────────────────────────────────────────────

    private void showMenu() {
        if (localMenu.isEmpty()) {
            System.out.println("Меню пусто.");
            return;
        }
        System.out.println("─── Меню ───");
        for (int i = 0; i < localMenu.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, localMenu.get(i));
        }
    }

    // ── 2. Добавить позицию ────────────────────────────────────────────────────

    private void addMenuItem() {
        // readEnum: Coffee / Tea / Pastry
        ItemType type = input.readEnum("Тип позиции:", ItemType.class);

        // readNonBlank: название не может быть пустым
        String name = input.readNonBlank("Название: ");

        MenuItem item = switch (type) {
            case COFFEE -> buildCoffee(name);
            case TEA    -> buildTea(name);
            case PASTRY -> buildPastry(name);
        };

        addToShop(item);
        System.out.printf("Добавлено в меню: %s%n", item);
    }

    private Coffee buildCoffee(String name) {
        // readDouble с диапазоном: защита от отрицательных цен и абсурдных значений
        double base = input.readDouble("Базовая цена ($): ", 0.50, 50.0);

        // readEnum: Coffee.Strength (Light / Medium / Strong)
        Coffee.Strength strength = input.readEnum("Крепость:", Coffee.Strength.class);

        // readEnum: Size (Small / Medium / Large) — цена = base * multiplier
        Size size = input.readEnum("Размер:", Size.class);

        System.out.printf("Итоговая цена: $%.2f (%.0f%% множитель)%n",
                base * size.getPriceMultiplier(), size.getPriceMultiplier() * 100);
        return new Coffee(name, base, strength, size);
    }

    private Tea buildTea(String name) {
        double base = input.readDouble("Базовая цена ($): ", 0.50, 50.0);

        // readEnum: Tea.TeaType (Green / Black / Herbal / Oolong)
        Tea.TeaType type = input.readEnum("Сорт чая:", Tea.TeaType.class);

        Size size = input.readEnum("Размер:", Size.class);

        System.out.printf("Итоговая цена: $%.2f | заваривать %d мин при %d°C%n",
                base * size.getPriceMultiplier(), type.getSteepingMinutes(), type.getWaterTempCelsius());
        return new Tea(name, base, type, size);
    }

    private Pastry buildPastry(String name) {
        // readDouble: прямая цена (без множителя)
        double price = input.readDouble("Цена ($): ", 0.50, 50.0);

        // readBoolean: два булевых свойства через y/n
        boolean glutenFree = input.readBoolean("Без глютена?");
        boolean warm       = input.readBoolean("Подавать тёплым?");

        return new Pastry(name, price, glutenFree, warm);
    }

    // ── 3. Принять заказ ───────────────────────────────────────────────────────

    private void takeOrder() {
        if (localMenu.isEmpty()) {
            System.out.println("Меню пусто. Сначала добавьте позиции (пункт 2).");
            return;
        }

        // readNonBlank: имя клиента
        String customerName = input.readNonBlank("Имя клиента: ");

        // readLine: комментарий — может быть пустым
        String comment = input.readLine("Комментарий (Enter — пропустить): ").trim();

        CoffeeShop.Order order = shop.createOrder(customerName);

        showMenu();
        System.out.println("  0. Завершить выбор");

        // Цикл добавления позиций: readInt с диапазоном [0, N]
        while (true) {
            int pick = input.readInt("Добавить позицию: ", 0, localMenu.size());
            if (pick == 0) break;
            MenuItem chosen = localMenu.get(pick - 1);
            order.addItem(chosen);
            System.out.printf("  + %s ($%.2f)%n", chosen.getName(), chosen.getPrice());
        }

        if (order.getItems().isEmpty()) {
            System.out.println("Заказ пуст — отменён.");
            return;
        }

        // Итог перед подтверждением
        System.out.printf("%nЗаказ для: %s%n", customerName);
        if (!comment.isEmpty()) System.out.println("Комментарий: " + comment);
        order.getItems().forEach(it ->
                System.out.printf("  %-24s $%.2f%n", it.getName(), it.getPrice()));
        System.out.printf("  %-24s $%.2f%n", "─────────── Итого:", order.getTotal());

        // readBoolean: подтверждение перед оформлением
        if (input.readBoolean("Оформить заказ?")) {
            System.out.println();
            shop.placeOrder(order);
        } else {
            System.out.println("Заказ отменён.");
        }
    }

    // ── 4. История заказов ─────────────────────────────────────────────────────

    private void showOrders() {
        CoffeeShop.CoffeeShopStats stats = shop.getStats();
        if (stats.getTotalOrders() == 0) {
            System.out.println("Заказов ещё не было.");
            return;
        }
        System.out.println("─── История ───");
        System.out.printf("  Оформлено заказов : %d%n",      stats.getTotalOrders());
        System.out.printf("  Продано позиций   : %d%n",      stats.getTotalItemsSold());
        System.out.printf("  Выручка           : $%.2f%n",   stats.getTotalRevenue());
        System.out.printf("  Средний чек       : $%.2f%n",
                stats.getTotalOrders() > 0
                        ? stats.getTotalRevenue() / stats.getTotalOrders()
                        : 0.0);
    }
}
