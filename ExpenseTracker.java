package PersonalEXPENSES;

import java.util.*;

enum Category { FOOD, TRAVEL, SHOPPING, BILLS, HEALTH, OTHER }

class Expense {
int id;
String description;
double amount;
Category category;
java.time.LocalDate date;

Expense(int id, String description, double amount, Category category, java.time.LocalDate date) {
this.id = id;
this.description = description;
this.amount = amount;
this.category = category;
this.date = date;
}

public String toString() {
return id + " | " + description + " | " + amount + " | " + category + " | " + date;
}
}

public class ExpenseTracker {

static ArrayList<Expense> expenses = new ArrayList<>();
static HashMap<Category, Double> categoryTotal = new HashMap<>();
static int id = 1;

static void addExpense(String description, double amount, Category category) {
Expense e = new Expense(id++, description, amount, category, java.time.LocalDate.now());
expenses.add(e);
System.out.println("Expense added successfully");
}

static void deleteExpense(int expenseId) {
boolean removed = expenses.removeIf(e -> e.id == expenseId);

if (removed)
System.out.println("Expense deleted successfully");
else
System.out.println("Expense not found");
}

static void displayExpenses() {
if (expenses.isEmpty()) {
System.out.println("No expenses found");
return;
}

expenses.forEach(System.out::println);
}

static void totalExpense() {
double total = expenses.stream()
.mapToDouble(e -> e.amount)
.sum();

System.out.println("Total Expense: " + total);
}

static void highestExpense() {
if (expenses.isEmpty()) {
System.out.println("No expenses found");
return;
}

Expense highest = expenses.stream()
.max(Comparator.comparingDouble(e -> e.amount))
.get();

System.out.println("Highest Expense: " + highest);
}

static void categoryWiseExpense() {
categoryTotal.clear();

for (Expense e : expenses) {
categoryTotal.put(e.category,
categoryTotal.getOrDefault(e.category, 0.0) + e.amount);
}

System.out.println("Category Wise Expenses:");

for (Map.Entry<Category, Double> entry : categoryTotal.entrySet()) {
System.out.println(entry.getKey() + " : " + entry.getValue());
}
}

public static void main(String[] args) {

Scanner sc = new Scanner(System.in);

while (true) {

System.out.println("\n===== PERSONAL EXPENSE TRACKER =====");
System.out.println("1. Add Expense");
System.out.println("2. Delete Expense");
System.out.println("3. Display All Expenses");
System.out.println("4. Calculate Total Expense");
System.out.println("5. Find Highest Expense");
System.out.println("6. Category Wise Expenses");
System.out.println("7. Exit");

System.out.print("Enter choice: ");
int choice = sc.nextInt();
sc.nextLine();

switch (choice) {

case 1:
System.out.print("Enter description: ");
String description = sc.nextLine();

System.out.print("Enter amount: ");
double amount = sc.nextDouble();

System.out.println("Categories:");
System.out.println("1. FOOD");
System.out.println("2. TRAVEL");
System.out.println("3. SHOPPING");
System.out.println("4. BILLS");
System.out.println("5. HEALTH");
System.out.println("6. OTHER");

System.out.print("Enter category: ");
int categoryChoice = sc.nextInt();

Category category;

switch (categoryChoice) {
case 1:
category = Category.FOOD;
break;
case 2:
category = Category.TRAVEL;
break;
case 3:
category = Category.SHOPPING;
break;
case 4:
category = Category.BILLS;
break;
case 5:
category = Category.HEALTH;
break;
default:
category = Category.OTHER;
}

addExpense(description, amount, category);
break;

case 2:
System.out.print("Enter expense ID: ");
int deleteId = sc.nextInt();

deleteExpense(deleteId);
break;

case 3:
displayExpenses();
break;

case 4:
totalExpense();
break;

case 5:
highestExpense();
break;

case 6:
categoryWiseExpense();
break;

case 7:
System.out.println("Thank you");
sc.close();
return;

default:
System.out.println("Invalid choice");
}
}
}
}