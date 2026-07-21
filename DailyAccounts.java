package com.mycompany.cafe;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DailyAccounts {

    private VBox root;
    private Stage stage;

    private static double dailyExpenses = 0.0;
    private static double storeExpenses = 0.0; 

    public DailyAccounts(Stage stage) {
        this.stage = stage;
        createUI();
    }

    private void createUI() {
        root = new VBox(12);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:#f5f5f5;");

        Label title = new Label("كافيه زردة - الحسابات اليومية الشاملة");
        title.setStyle("-fx-font-size:22px; -fx-text-fill:#8B5E3C; -fx-font-weight: bold;");

        // 1. جلب إيرادات المبيعات الكلية والتفصيلية من كلاس Hall
        double totalRevenue = Hall.getDailyTotalIncome();
        double drinksRev = Hall.getDailyDrinksIncome();
        double shishaRev = Hall.getDailyShishaIncome();
        double foodRev = Hall.getDailyFoodIncome();

        Label revenueLabel = new Label("إجمالي المبيعات اليومية: " + totalRevenue + " جنيه");
        revenueLabel.setStyle("-fx-font-size:16px; -fx-text-fill:#2e7d32; -fx-font-weight: bold;");

        Label breakdownLabel = new Label(String.format("☕ مشاريب: %.1f ج  |  💨 شيشة: %.1f ج  |  🍔 أكل: %.1f ج", drinksRev, shishaRev, foodRev));
        breakdownLabel.setStyle("-fx-font-size:14px; -fx-text-fill:#555555; -fx-font-weight: bold;");

        // 2. جلب يوميات الموظفين المحددين من شاشة شئون الموظفين تلقائياً
        double staffWagesTotal = EmployeesAccounts.getSelectedEmployeesDailyWagesTotal();
        String staffDetailsText = EmployeesAccounts.getSelectedEmployeesDetails();

        Label staffWagesLabel = new Label("إجمالي يوميات الموظفين الحاضرين:\n" + staffDetailsText + "\nالمجموع المخصوم: " + staffWagesTotal + " جنيه");
        staffWagesLabel.setStyle("-fx-font-size:13px; -fx-text-fill:#337ab7; -fx-font-weight: bold;");

        // إجمالي المصروفات تشمل (مصروفات يدوية + مصروفات مخزن + يوميات الموظفين الحاضرين)
        double totalExpensesAll = dailyExpenses + storeExpenses + staffWagesTotal;
        Label expensesLabel = new Label("إجمالي المصروفات اليومية الشاملة: " + totalExpensesAll + " جنيه");
        expensesLabel.setStyle("-fx-font-size:15px; -fx-text-fill:#d9534f; -fx-font-weight: bold;");

        double netProfit = totalRevenue - totalExpensesAll;
        Label netLabel = new Label("صافي الدخل اليومي (بعد خصم الموظفين والنثريات): " + netProfit + " جنيه");
        netLabel.setStyle("-fx-font-size:18px; -fx-text-fill:#333333; -fx-font-weight: bold;");

        // حقول إضافة مصروف عارض جديد
        TextField expenseNameField = new TextField();
        expenseNameField.setPromptText("بيان المصروف (مثلاً: فاتورة كهرباء)");
        expenseNameField.setMaxWidth(250);

        TextField expenseAmountField = new TextField();
        expenseAmountField.setPromptText("المبلغ");
        expenseAmountField.setMaxWidth(250);

        Button addExpenseBtn = new Button("إضافة مصروف عارض جديد");
        addExpenseBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold;");
        
        addExpenseBtn.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(expenseAmountField.getText());
                dailyExpenses += amt;
                
                double updatedTotalExp = dailyExpenses + storeExpenses + EmployeesAccounts.getSelectedEmployeesDailyWagesTotal();
                expensesLabel.setText("إجمالي المصروفات اليومية الشاملة: " + updatedTotalExp + " جنيه");
                
                double updatedNet = totalRevenue - updatedTotalExp;
                netLabel.setText("صافي الدخل اليومي (بعد خصم الموظفين والنثريات): " + updatedNet + " جنيه");
                
                expenseNameField.clear();
                expenseAmountField.clear();
            } catch (Exception ex) {
                showAlert("خطأ", "يرجى إدخال مبلغ صحيح بالمصروفات!");
            }
        });

        Button backBtn = new Button("رجوع للوحة التحكم");
        backBtn.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            Dashboard dashboard = new Dashboard(stage);
            stage.setScene(new javafx.scene.Scene(dashboard.getView(), 900, 600));
        });

        root.getChildren().addAll(
                title, new Separator(),
                revenueLabel, breakdownLabel, staffWagesLabel, expensesLabel, netLabel,
                new Separator(),
                new Label("إضافة مصروفات يومية عارضة:"),
                expenseNameField, expenseAmountField, addExpenseBtn,
                new Separator(),
                backBtn
        );
    }

    public static void addStoreExpense(double amount) {
        storeExpenses += amount;
    }

    public static double getDailyNetProfit() {
        return Hall.getDailyTotalIncome() - (dailyExpenses + storeExpenses + EmployeesAccounts.getSelectedEmployeesDailyWagesTotal());
    }

    public static double getDailyTotalRevenue() {
        return Hall.getDailyTotalIncome();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return root;
    }
}
