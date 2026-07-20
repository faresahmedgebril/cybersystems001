package com.mycompany.cafe;


import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;



public class Store {


    private VBox root;

    private Stage stage;


    private ListView<String> productList;




    public Store(Stage stage){

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





        Label title = new Label("المخزن - Store");


        title.setStyle(
                "-fx-font-size:30px;" +
                "-fx-text-fill:#8B5E3C;"
        );







        TextField name = new TextField();

        name.setPromptText("اسم المنتج");

        name.setPrefWidth(200);






        TextField quantity = new TextField();

        quantity.setPromptText("الكمية");

        quantity.setPrefWidth(200);







        TextField price = new TextField();

        price.setPromptText("السعر");

        price.setPrefWidth(200);







        Button addBtn = new Button("إضافة منتج");


        addBtn.setPrefWidth(150);






        productList = new ListView<>();


        productList.setPrefWidth(400);

        productList.setPrefHeight(300);




        // بيانات تجريبية

        productList.getItems().addAll(

                "قهوة | كمية: 20 | سعر: 150",

                "سكر | كمية: 50 | سعر: 30",

                "لبن | كمية: 30 | سعر: 40",

                "بيبسي | كمية: 40 | سعر: 20"

        );








        addBtn.setOnAction(e -> {


            if(!name.getText().isEmpty()
                    &&
               !quantity.getText().isEmpty()
                    &&
               !price.getText().isEmpty()
            ){



                productList.getItems().add(

                        name.getText()
                        +
                        " | كمية: "
                        +
                        quantity.getText()
                        +
                        " | سعر: "
                        +
                        price.getText()

                );



                name.clear();

                quantity.clear();

                price.clear();


            }


        });







        Button deleteBtn = new Button("حذف المنتج");


        deleteBtn.setPrefWidth(150);



        deleteBtn.setOnAction(e -> {


            String selected = productList
                    .getSelectionModel()
                    .getSelectedItem();



            if(selected != null){

                productList.getItems()
                        .remove(selected);

            }


        });








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







        VBox inputs = new VBox(10);


        inputs.getChildren().addAll(

                name,

                quantity,

                price

        );






        HBox buttons = new HBox(20);


        buttons.setAlignment(Pos.CENTER);


        buttons.getChildren().addAll(

                addBtn,

                deleteBtn

        );






        root.getChildren().addAll(

                title,

                inputs,

                productList,

                buttons,

                backBtn

        );


    }







    public VBox getView(){

        return root;

    }


}
