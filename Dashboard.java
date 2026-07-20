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


        root = new VBox(30);

        root.setAlignment(Pos.CENTER);


        root.setStyle(
                "-fx-background-color:#f5f5f5;"
        );




        Label title = new Label("Cafe Dashboard");


        title.setStyle(
                "-fx-font-size:30px;" +
                "-fx-text-fill:#8B5E3C;"
        );






        Button hallBtn = new Button("الصالة");


        hallBtn.setPrefWidth(250);

        hallBtn.setPrefHeight(50);



        hallBtn.setStyle(
                "-fx-background-color:#8B5E3C;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:18px;"
        );







        Button storeBtn = new Button("المخزن");


        storeBtn.setPrefWidth(250);

        storeBtn.setPrefHeight(50);



        storeBtn.setStyle(
                "-fx-background-color:#333333;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:18px;"
        );







        // فتح الصالة والطلبات

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








        root.getChildren().addAll(

                title,

                hallBtn,

                storeBtn

        );


    }





    public VBox getView(){

        return root;

    }


}
