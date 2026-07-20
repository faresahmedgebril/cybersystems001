package com.mycompany.cafe;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Hall {

    private VBox root;
    private Stage stage;

    private double total = 0;
    private double drinksTotalAmount = 0; // إجمالي المشاريب الحالية في الأوردر
    private static double dailyTotalIncome = 0; // إجمالي الدخل اليومي (محفوظ على مستوى الجلسة)

    private Label totalLabel;
    private Label drinksTotalLabel;
    private Label dailyIncomeLabel;
    private ListView<String> orderList;
    private Label currentTableLabel;
    private String selectedTable = "ترابيزة 1";

    public Hall(Stage stage){
        this.stage = stage;
        createUI();
    }

    private void createUI(){
        root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:#f5f5f5;");

        // ==========================================
        // 0. الجزء العلوي (Top Header): اسم الشاشة + إجمالي المشاريب + الدخل اليومي ع الشمال
        // ==========================================
        Label title = new Label("نظام الكاشير - الصالة");
        title.setStyle("-fx-font-size:20px; -fx-text-fill:#8B5E3C; -fx-font-weight: bold;");

        currentTableLabel = new Label("الترابيزة: " + selectedTable);
        currentTableLabel.setStyle("-fx-font-size:15px; -fx-text-fill:#d9534f; -fx-font-weight: bold;");

        // إجمالي المشاريب والدخل اليومي (فوق ع الشمال)
        drinksTotalLabel = new Label("إجمالي المشاريب: 0.0 ج");
        drinksTotalLabel.setStyle("-fx-font-size:13px; -fx-text-fill:#333333; -fx-font-weight: bold;");

        dailyIncomeLabel = new Label("دخل اليوم: " + dailyTotalIncome + " ج");
        dailyIncomeLabel.setStyle("-fx-font-size:13px; -fx-text-fill:#2e7d32; -fx-font-weight: bold;");

        VBox statsBox = new VBox(3);
        statsBox.setAlignment(Pos.TOP_LEFT);
        statsBox.getChildren().addAll(drinksTotalLabel, dailyIncomeLabel);

        // تنسيق الهيدر بحيث يكون متوزع (العنوان يمين، الإحصائيات شمال)
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER);
        
        HBox leftStatsWrapper = new HBox(statsBox);
        leftStatsWrapper.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftStatsWrapper, Priority.ALWAYS);

        headerBox.getChildren().addAll(title, new Label("   |   "), currentTableLabel, leftStatsWrapper);

        // ==========================================
        // 1. أزرار الترابيزات
        // ==========================================
        HBox tablesBox = new HBox(8);
        tablesBox.setAlignment(Pos.CENTER);
        for (int i = 1; i <= 6; i++) {
            final String tblName = "ترابيزة " + i;
            Button tBtn = new Button(tblName);
            tBtn.setPrefWidth(90);
            tBtn.setStyle("-fx-background-color: #337ab7; -fx-text-fill: white; -fx-font-weight: bold;");
            tBtn.setOnAction(e -> {
                selectedTable = tblName;
                currentTableLabel.setText("الترابيزة: " + selectedTable);
            });
            tablesBox.getChildren().add(tBtn);
        }

        // ==========================================
        // 2. أزرار الأقسام (مشاريب، شيشة، أكل)
        // ==========================================
        TilePane itemsGrid = new TilePane();
        itemsGrid.setHgap(10);
        itemsGrid.setVgap(10);
        itemsGrid.setPrefColumns(3);
        itemsGrid.setPrefWidth(420);

        Button btnCatDrinks = new Button("مشاريب ☕");
        Button btnCatShisha = new Button("شيشة 💨");
        Button btnCatFood = new Button("أكل 🍔");

        styleCategoryButton(btnCatDrinks);
        styleCategoryButton(btnCatShisha);
        styleCategoryButton(btnCatFood);

        btnCatDrinks.setOnAction(e -> loadItemsToGrid(itemsGrid, "drinks"));
        btnCatShisha.setOnAction(e -> loadItemsToGrid(itemsGrid, "shisha"));
        btnCatFood.setOnAction(e -> loadItemsToGrid(itemsGrid, "food"));

        HBox categoriesBox = new HBox(10);
        categoriesBox.setAlignment(Pos.CENTER);
        categoriesBox.getChildren().addAll(btnCatDrinks, btnCatShisha, btnCatFood);

        loadItemsToGrid(itemsGrid, "drinks");

        VBox leftPanel = new VBox(10);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.getChildren().addAll(new Label("اختر القسم ثم اضغط على الصنف:"), categoriesBox, itemsGrid);

        // ==========================================
        // 3. شاشة الأوردر الحالي (يمين)
        // ==========================================
        orderList = new ListView<>();
        orderList.setPrefWidth(320);
        orderList.setPrefHeight(320);

        Button plusBtn = new Button("+");
        Button minusBtn = new Button("-");
        Button deleteItemBtn = new Button("حذف الصنف");
        
        plusBtn.setPrefWidth(50);
        minusBtn.setPrefWidth(50);
        deleteItemBtn.setPrefWidth(100);

        plusBtn.setOnAction(e -> modifyQuantity(1));
        minusBtn.setOnAction(e -> modifyQuantity(-1));
        deleteItemBtn.setOnAction(e -> deleteSelectedOrderItem());

        HBox qtyBox = new HBox(8);
        qtyBox.setAlignment(Pos.CENTER);
        qtyBox.getChildren().addAll(plusBtn, minusBtn, deleteItemBtn);

        VBox rightPanel = new VBox(10);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.getChildren().addAll(new Label("الأوردر الحالي"), orderList, qtyBox);

        HBox centerContent = new HBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.getChildren().addAll(leftPanel, rightPanel);

        // ==========================================
        // 4. أزرار العمليات (مسح، تأكيد بدون طباعة، طباعة، رجوع)
        // ==========================================
        totalLabel = new Label("الإجمالي: 0.0 جنيه");
        totalLabel.setStyle("-fx-font-size:18px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        Button clearOrderBtn = new Button("مسح الأوردر");
        clearOrderBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        clearOrderBtn.setOnAction(e -> clearOrder());

        // زرار تأكيد الطلب من غير طباعة
        Button confirmOrderBtn = new Button("تأكيد الطلب ✓");
        confirmOrderBtn.setStyle("-fx-background-color: #f0ad4e; -fx-text-fill: white; -fx-font-weight: bold;");
        confirmOrderBtn.setOnAction(e -> confirmOrderWithoutPrint());

        Button printInvoiceBtn = new Button("طباعة الفاتورة 🖨️");
        printInvoiceBtn.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white; -fx-font-weight: bold;");
        printInvoiceBtn.setOnAction(e -> printInvoice());

        Button backBtn = new Button("رجوع");
        backBtn.setOnAction(e -> {
            Dashboard dashboard = new Dashboard(stage);
            stage.setScene(new javafx.scene.Scene(dashboard.getView(), 900, 600));
        });

        HBox bottomActions = new HBox(12);
        bottomActions.setAlignment(Pos.CENTER);
        bottomActions.getChildren().addAll(totalLabel, confirmOrderBtn, clearOrderBtn, printInvoiceBtn, backBtn);

        root.getChildren().addAll(headerBox, new Separator(), tablesBox, centerContent, bottomActions);
    }

    private void styleCategoryButton(Button btn) {
        btn.setPrefWidth(100);
        btn.setPrefHeight(35);
        btn.setStyle("-fx-background-color: #8B5E3C; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    private void loadItemsToGrid(TilePane grid, String category) {
        grid.getChildren().clear();
        String[][] itemsData;

        if (category.equals("drinks")) {
            itemsData = new String[][] {
                {"شاي", "15"}, {"قهوة", "20"}, {"نسكافيه", "30"},
                {"كابتشينو", "45"}, {"لاتيه", "50"}, {"عصير مانجو", "40"},
                {"عصير فراولة", "40"}, {"بيبسي", "20"}
            };
        } else if (category.equals("shisha")) {
            itemsData = new String[][] {
                {"شيشة قص", "35"}, {"شيشة تفاحتين", "40"},
                {"شيشة نخلة", "40"}, {"شيشة مكس", "45"}
            };
        } else {
            itemsData = new String[][] {
                {"توست جبنة", "50"}, {"تشيز كيك", "60"},
                {"كرواسون", "25"}, {"طبق فواكه", "75"}
            };
        }

        for (String[] item : itemsData) {
            String name = item[0];
            double price = Double.parseDouble(item[1]);

            Button itemBtn = new Button(name + "\n" + price + " ج");
            itemBtn.setPrefSize(130, 65);
            itemBtn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-font-weight: bold; -fx-cursor: hand;");
            
            // تحديد القسم لنعرف هل هو مشروب أم لا عند الإضافة
            String itemType = category; 
            itemBtn.setOnAction(e -> addItemToOrder(name, price, 1, itemType));
            
            grid.getChildren().add(itemBtn);
        }
    }

    private void addItemToOrder(String name, double price, int qtyToAdd, String category) {
        boolean found = false;
        for (int i = 0; i < orderList.getItems().size(); i++) {
            String existing = orderList.getItems().get(i);
            if (existing.startsWith(name)) {
                int currentQty = extractQuantity(existing);
                int newQty = currentQty + qtyToAdd;
                orderList.getItems().set(i, name + " - " + price + " ج (x" + newQty + ") [" + category + "]");
                found = true;
                break;
            }
        }

        if (!found) {
            orderList.getItems().add(name + " - " + price + " ج (x1) [" + category + "]");
        }

        total += price * qtyToAdd;
        
        // إذا كان الصنف من قسم المشاريب، نزوده في عداد إجمالي المشاريب
        if (category.equals("drinks")) {
            drinksTotalAmount += price * qtyToAdd;
        }

        updateLabels();
    }

    private void modifyQuantity(int delta) {
        int selectedIndex = orderList.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) return;

        String line = orderList.getItems().get(selectedIndex);
        String[] parts = line.split(" - ");
        String name = parts[0];
        double price = Double.parseDouble(parts[1].split(" ")[0]);
        
        // معرفة القسم من النص المخزن
        boolean isDrink = line.contains("[drinks]");

        int currentQty = extractQuantity(line);
        int newQty = currentQty + delta;

        if (newQty <= 0) {
            total -= price * currentQty;
            if (isDrink) drinksTotalAmount -= price * currentQty;
            orderList.getItems().remove(selectedIndex);
        } else {
            String categoryTag = isDrink ? "drinks" : (line.contains("[shisha]") ? "shisha" : "food");
            orderList.getItems().set(selectedIndex, name + " - " + price + " ج (x" + newQty + ") [" + categoryTag + "]");
            total += (price * delta);
            if (isDrink) drinksTotalAmount += (price * delta);
        }
        updateLabels();
    }

    private void deleteSelectedOrderItem() {
        int selectedIndex = orderList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String line = orderList.getItems().get(selectedIndex);
            double itemTotal = extractLineTotal(line);
            total -= itemTotal;
            
            if (line.contains("[drinks]")) {
                drinksTotalAmount -= itemTotal;
            }

            orderList.getItems().remove(selectedIndex);
            updateLabels();
        }
    }

    private void clearOrder() {
        orderList.getItems().clear();
        total = 0;
        drinksTotalAmount = 0;
        updateLabels();
    }

    // زرار تأكيد الطلب (بدون طباعة): يحسب المبلغ في الدخل اليومي ويفرغ الأوردر
    private void confirmOrderWithoutPrint() {
        if(orderList.getItems().isEmpty()) {
            showAlert("تنبيه", "الأوردر فارغ!");
            return;
        }
        dailyTotalIncome += total; // إضافة إجمالي الأوردر للدخل اليومي
        showAlert("تم التأكيد", "تم تسجيل وتأكيد الطلب لـ (" + selectedTable + ") بنجاح!");
        clearOrder();
    }

    private void printInvoice() {
        if(orderList.getItems().isEmpty()) {
            showAlert("تنبيه", "الأوردر فارغ ولا يمكن طباعة فاتورة!");
            return;
        }
        dailyTotalIncome += total; // الطباعة تعتبر تأكيد ودفع أيضاً
        showAlert("تمت الطباعة", "تم إرسال الفاتورة لـ (" + selectedTable + ") إلى الطابعة وتسجيل الدخل!");
        clearOrder();
    }

    private int extractQuantity(String itemText) {
        try {
            int start = itemText.indexOf("(x") + 2;
            int end = itemText.indexOf(")", start);
            return Integer.parseInt(itemText.substring(start, end));
        } catch (Exception e) {
            return 1;
        }
    }

    private double extractLineTotal(String itemText) {
        try {
            String[] parts = itemText.split(" - ");
            double price = Double.parseDouble(parts[1].split(" ")[0]);
            int qty = extractQuantity(itemText);
            return price * qty;
        } catch (Exception e) {
            return 0;
        }
    }

    private void updateLabels() {
        totalLabel.setText("الإجمالي: " + total + " جنيه");
        drinksTotalLabel.setText("إجمالي المشاريب: " + drinksTotalAmount + " ج");
        dailyIncomeLabel.setText("دخل اليوم: " + dailyTotalIncome + " ج");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView(){
        return root;
    }
}
