package com.mycompany.cafe;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hall {

    private VBox root;
    private Stage stage;

    private double total = 0;
    private double drinksTotalAmount = 0;
    
    // متغيرات الدخل الإجمالي والدخل لكل قسم (مشتركة على مستوى السيستم)
    private static double dailyTotalIncome = 0;
    private static double dailyDrinksIncome = 0;
    private static double dailyShishaIncome = 0;
    private static double dailyFoodIncome = 0;

    private static int orderCounter = 1000; // عداد تلقائي لأرقام الأوردرات

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

        Label title = new Label("نظام الكاشير - الصالة");
        title.setStyle("-fx-font-size:20px; -fx-text-fill:#8B5E3C; -fx-font-weight: bold;");

        currentTableLabel = new Label("الترابيزة: " + selectedTable);
        currentTableLabel.setStyle("-fx-font-size:15px; -fx-text-fill:#d9534f; -fx-font-weight: bold;");

        drinksTotalLabel = new Label("إجمالي المشاريب: 0.0 ج");
        drinksTotalLabel.setStyle("-fx-font-size:13px; -fx-text-fill:#333333; -fx-font-weight: bold;");

        dailyIncomeLabel = new Label("دخل اليوم: " + dailyTotalIncome + " ج");
        dailyIncomeLabel.setStyle("-fx-font-size:13px; -fx-text-fill:#2e7d32; -fx-font-weight: bold;");

        VBox statsBox = new VBox(3);
        statsBox.setAlignment(Pos.TOP_LEFT);
        statsBox.getChildren().addAll(drinksTotalLabel, dailyIncomeLabel);

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER);
        
        HBox leftStatsWrapper = new HBox(statsBox);
        leftStatsWrapper.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(leftStatsWrapper, Priority.ALWAYS);

        headerBox.getChildren().addAll(title, new Label("    |    "), currentTableLabel, leftStatsWrapper);

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

        totalLabel = new Label("الإجمالي: 0.0 جنيه");
        totalLabel.setStyle("-fx-font-size:18px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        Button clearOrderBtn = new Button("مسح الأوردر");
        clearOrderBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        clearOrderBtn.setOnAction(e -> clearOrder());

        Button confirmOrderBtn = new Button("تأكيد بدون طباعة ✓");
        confirmOrderBtn.setStyle("-fx-background-color: #f0ad4e; -fx-text-fill: white; -fx-font-weight: bold;");
        confirmOrderBtn.setOnAction(e -> confirmOrderWithoutPrint());

        Button printInvoiceBtn = new Button("طباعة الفواتير والبونات 🖨️");
        printInvoiceBtn.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white; -fx-font-weight: bold;");
        printInvoiceBtn.setOnAction(e -> processAndPrintAll());

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

    // دالة مساعدة لتوزيع المبالغ على الأقسام بدقة عند التأكيد أو الطباعة
    private void addOrderTotalsToDailyIncome() {
        for (String itemLine : orderList.getItems()) {
            double lineTotal = extractLineTotal(itemLine);
            dailyTotalIncome += lineTotal;

            if (itemLine.contains("[drinks]")) {
                dailyDrinksIncome += lineTotal;
            } else if (itemLine.contains("[shisha]")) {
                dailyShishaIncome += lineTotal;
            } else if (itemLine.contains("[food]")) {
                dailyFoodIncome += lineTotal;
            }
        }
    }

    private void confirmOrderWithoutPrint() {
        if(orderList.getItems().isEmpty()) {
            showAlert("تنبيه", "الأوردر فارغ!");
            return;
        }
        addOrderTotalsToDailyIncome();
        showAlert("تم التأكيد", "تم تسجيل وتأكيد الطلب لـ (" + selectedTable + ") بنجاح!");
        clearOrder();
    }

    private void processAndPrintAll() {
        if(orderList.getItems().isEmpty()) {
            showAlert("تنبيه", "الأوردر فارغ ولا يمكن طباعة فواتير!");
            return;
        }

        int currentOrderNo = orderCounter++;
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a"));

        StringBuilder cashierReceipt = new StringBuilder();
        cashierReceipt.append("==========================\n");
        cashierReceipt.append("      CAFE SYSTEM\n");
        cashierReceipt.append("Order #").append(currentOrderNo).append("\n");
        cashierReceipt.append("Table : ").append(selectedTable).append("\n");
        cashierReceipt.append("Date : ").append(dateTime).append("\n");
        cashierReceipt.append("==========================\n\n");
        cashierReceipt.append(String.format("%-5s %-12s %-6s %-6s\n", "Qty", "Item", "Price", "Total"));
        cashierReceipt.append("-----------------------------------\n");

        StringBuilder barTicket = new StringBuilder("===== BAR (Drinks) =====\nTable : " + selectedTable + "\nOrder #" + currentOrderNo + "\n\n");
        StringBuilder kitchenTicket = new StringBuilder("===== KITCHEN (Food) =====\nTable : " + selectedTable + "\nOrder #" + currentOrderNo + "\n\n");
        StringBuilder shishaTicket = new StringBuilder("===== SHISHA =====\nTable : " + selectedTable + "\nOrder #" + currentOrderNo + "\n\n");

        boolean hasDrinks = false;
        boolean hasFood = false;
        boolean hasShisha = false;

        for (String itemLine : orderList.getItems()) {
            String[] parts = itemLine.split(" - ");
            String name = parts[0];
            double price = Double.parseDouble(parts[1].split(" ")[0]);
            int qty = extractQuantity(itemLine);
            double lineTotal = price * qty;

            cashierReceipt.append(String.format("%-5d %-12s %-6.0f %-6.0f\n", qty, name, price, lineTotal));

            if (itemLine.contains("[drinks]")) {
                barTicket.append(qty).append(" ").append(name).append("\n");
                hasDrinks = true;
            } else if (itemLine.contains("[food]")) {
                kitchenTicket.append(qty).append(" ").append(name).append("\n");
                hasFood = true;
            } else if (itemLine.contains("[shisha]")) {
                shishaTicket.append(qty).append(" ").append(name).append("\n");
                hasShisha = true;
            }
        }

        cashierReceipt.append("\n-----------------------------------\n");
        cashierReceipt.append(String.format("TOTAL : %.0f EGP\n", total));
        cashierReceipt.append("-----------------------------------\n");
        cashierReceipt.append("Cashier : Ahmed\n");
        cashierReceipt.append("Thank You\n");
        cashierReceipt.append("==========================\n\n\n");

        printToPrinter("CashierPrinter", cashierReceipt.toString());

        if (hasDrinks) {
            printToPrinter("BarPrinter", barTicket.toString());
        }
        if (hasFood) {
            printToPrinter("KitchenPrinter", kitchenTicket.toString());
        }
        if (hasShisha) {
            printToPrinter("ShishaPrinter", shishaTicket.toString());
        }

        addOrderTotalsToDailyIncome();
        showAlert("تمت الطباعة", "تم إصدار الفاتورة وتوزيع البونات على الأقسام بنجاح!");
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

    // دوال لجلب الإيرادات ليراها كلاس الحسابات اليومية
    public static double getDailyTotalIncome() {
        return dailyTotalIncome;
    }

    public static double getDailyDrinksIncome() {
        return dailyDrinksIncome;
    }

    public static double getDailyShishaIncome() {
        return dailyShishaIncome;
    }

    public static double getDailyFoodIncome() {
        return dailyFoodIncome;
    }

    private void printToPrinter(String printerNameIdentifier, String textContent) {
        try {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService selectedService = null;

            for (PrintService service : printServices) {
                if (service.getName().equalsIgnoreCase(printerNameIdentifier)) {
                    selectedService = service;
                    break;
                }
            }

            if (selectedService == null) {
                selectedService = PrintServiceLookup.lookupDefaultPrintService();
            }

            if (selectedService != null) {
                DocPrintJob printJob = selectedService.createPrintJob();
                byte[] bytes = textContent.getBytes("CP1256"); 
                Doc doc = new SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
                printJob.print(doc, attributes);
            } else {
                System.out.println("تحذير: لا توجد أي طابعة متصلة أو معرفة على الجهاز!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
