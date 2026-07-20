package com.mycompany.cafe;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard {

    private VBox root;
    private Stage stage;

    public Dashboard(Stage stage){
        this.stage = stage;
        createUI();
    }

    private void createUI(){
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-background-color:#f5f5f5;"
        );

        Label title = new Label("Cafe Dashboard");
        title.setStyle(
                "-fx-font-size:26px;" +
                "-fx-text-fill:#8B5E3C;" +
                "-fx-font-weight: bold;"
        );

        // زر الصالة
        Button hallBtn = new Button("الصالة");
        styleDashboardButton(hallBtn, "#8B5E3C");

        // زر المخزن
        Button storeBtn = new Button("المخزن");
        styleDashboardButton(storeBtn, "#333333");

        // 1. زر حسابات الموظفين
        Button employeesBtn = new Button("حسابات الموظفين");
        styleDashboardButton(employeesBtn, "#337ab7");

        // 2. زر الحسابات اليومية
        Button dailyAccountsBtn = new Button("الحسابات اليومية");
        styleDashboardButton(dailyAccountsBtn, "#5cb85c");

        // 3. زر الحسابات الشهرية
        Button monthlyAccountsBtn = new Button("الحسابات الشهرية");
        styleDashboardButton(monthlyAccountsBtn, "#f0ad4e");

        // ربط الأزرار بالانتقال للشاشات المطلوبة
        hallBtn.setOnAction(e -> {
            Hall hall = new Hall(stage);
            stage.setScene(
                    new javafx.scene.Scene(
                            hall.getView(),
                            900,
                            600
                    )
            );
        });

        storeBtn.setOnAction(e -> {
            Store store = new Store(stage);
            stage.setScene(
                    new javafx.scene.Scene(
                            store.getView(),
                            900,
                            600
                    )
            );
        });

        // الأحداث الخاصة بالأزرار الجديدة (يمكن ربطها بشاشات أو نوافذ خاصة بها لاحقاً)
        employeesBtn.setOnAction(e -> {
            // Placeholder للشاشة أو الوظيفة المستقبلية لحسابات الموظفين
            showAlert("حسابات الموظفين", "سيتم فتح شاشة إدارة الرواتب والسلف والجزاءات للموظفين.");
        });

        dailyAccountsBtn.setOnAction(e -> {
            showAlert("الحسابات اليومية", "عرض تفاصيل الإيرادات والمصروفات اليومية وصافي الدخل.");
        });

        monthlyAccountsBtn.setOnAction(e -> {
            showAlert("الحسابات الشهرية", "عرض التقارير المالية الشهرية الشاملة للأرباح والخسائر.");
        });

        root.getChildren().addAll(
                title,
                hallBtn,
                storeBtn,
                employeesBtn,
                dailyAccountsBtn,
                monthlyAccountsBtn
        );
    }

    // دالة موحدة لتنسيق أزرار لوحة التحكم بنفس الأبعاد والشكل الجمالي
    private void styleDashboardButton(Button btn, String bgColor) {
        btn.setPrefWidth(250);
        btn.setPrefHeight(45);
        btn.setStyle(
                "-fx-background-color:" + bgColor + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-size:16px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );
    }

    // دالة مساعدة لعرض التنبيهات عند الضغط على الأزرار الجديدة
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView(){
        return root;
    }
}
