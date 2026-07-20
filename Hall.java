package com.mycompany.cafe;


import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Hall {


    private VBox root;

    private Stage stage;


    private double total = 0;

    private Label totalLabel;


    private ListView<String> orderList;




    public Hall(Stage stage){

        this.stage = stage;

        createUI();

    }




    private void createUI(){


        root = new VBox(20);

        root.setPadding(new Insets(30));

        root.setAlignment(Pos.CENTER);


        root.setStyle(
                "-fx-background-color:#f5f5f5;"
        );



        Label title = new Label("الصالة - Orders");


        title.setStyle(
                "-fx-font-size:30px;" +
                "-fx-text-fill:#8B5E3C;"
        );




        ListView<String> drinks = new ListView<>();


        drinks.getItems().addAll(

                "شاي - 15",

                "قهوة - 20",

                "نسكافيه - 30",

                "كابتشينو - 45",

                "لاتيه - 50",

                "عصير مانجو - 40",

                "عصير فراولة - 40",

                "بيبسي - 20"

        );


        drinks.setPrefWidth(250);

        drinks.setPrefHeight(300);







        orderList = new ListView<>();


        orderList.setPrefWidth(300);

        orderList.setPrefHeight(300);








        Button addBtn = new Button("إضافة");

        addBtn.setPrefWidth(120);



        addBtn.setOnAction(e -> {


            String item = drinks.getSelectionModel().getSelectedItem();


            if(item != null){


                orderList.getItems().add(item);


                String price = item.substring(
                        item.indexOf("-") + 1
                );


                total += Double.parseDouble(price.trim());


                updateTotal();

            }


        });







        Button clearBtn = new Button("مسح الطلب");


        clearBtn.setPrefWidth(120);



        clearBtn.setOnAction(e -> {


            orderList.getItems().clear();


            total = 0;


            updateTotal();


        });








        totalLabel = new Label("الإجمالي: 0 جنيه");


        totalLabel.setStyle(
                "-fx-font-size:20px;"
        );







        Button backBtn = new Button("رجوع");


        backBtn.setOnAction(e -> {


            Dashboard dashboard = new Dashboard(stage);


            stage.setScene(

                    new javafx.scene.Scene(

                            dashboard.getView(),

                            900,

                            600

                    )

            );


        });








        VBox left = new VBox(10);

        left.getChildren().addAll(

                new Label("المشروبات"),

                drinks

        );





        VBox right = new VBox(10);

        right.getChildren().addAll(

                new Label("الأوردر"),

                orderList

        );






        HBox center = new HBox(30);


        center.setAlignment(Pos.CENTER);


        center.getChildren().addAll(

                left,

                addBtn,

                right

        );






        root.getChildren().addAll(

                title,

                center,

                totalLabel,

                clearBtn,

                backBtn

        );


    }





    private void updateTotal(){

        totalLabel.setText(
                "الإجمالي: " + total + " جنيه"
        );

    }







    public VBox getView(){

        return root;

    }


}
