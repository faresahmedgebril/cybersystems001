package com.mycompany.cafe;


import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.stage.Stage;



public class LoginScreen {


    private VBox root;

    private Stage stage;



    public LoginScreen(Stage stage){

        this.stage = stage;

        createUI();

    }




    private void createUI(){


        root = new VBox();


        root.setAlignment(Pos.CENTER);

        root.setPadding(new Insets(30));


        root.setStyle(
                "-fx-background-color:#F5F5F5;"
        );




        VBox card = new VBox(18);


        card.setAlignment(Pos.CENTER);

        card.setPadding(new Insets(40));

        card.setPrefWidth(350);



        card.setStyle(

                "-fx-background-color:white;" +

                "-fx-background-radius:20;" +

                "-fx-effect:dropshadow(gaussian,#999,15,0,0,5);"

        );




        Label logo = new Label("☕");

        logo.setFont(
                Font.font(50)
        );




        Label title = new Label("Cafe ZERDAK");


        title.setFont(
                Font.font("Arial",30)
        );


        title.setTextFill(
                Color.web("#8B5E3C")
        );





        Label welcome = new Label("Welcome Back");


        welcome.setFont(
                Font.font(18)
        );





        TextField username = new TextField();


        username.setPromptText("Username");

        username.setPrefHeight(40);

        username.setMaxWidth(250);





        PasswordField password = new PasswordField();


        password.setPromptText("Password");

        password.setPrefHeight(40);

        password.setMaxWidth(250);







        Button login = new Button("Login");


        login.setPrefWidth(250);

        login.setPrefHeight(40);




        login.setStyle(

                "-fx-background-color:#8B5E3C;" +

                "-fx-text-fill:white;" +

                "-fx-font-size:16;" +

                "-fx-background-radius:10;"

        );





        login.setOnAction(e -> {


            String user = username.getText();

            String pass = password.getText();




            if(user.equals("admin") && pass.equals("1234")){



                Dashboard dashboard = new Dashboard(stage);



                stage.setScene(

                        new javafx.scene.Scene(

                                dashboard.getView(),

                                900,

                                600

                        )

                );



            }

            else{


                Alert alert = new Alert(
                        Alert.AlertType.ERROR
                );


                alert.setTitle("Error");

                alert.setHeaderText(null);

                alert.setContentText(
                        "Wrong Username or Password"
                );


                alert.show();



            }



        });






        card.getChildren().addAll(

                logo,

                title,

                welcome,

                username,

                password,

                login

        );





        root.getChildren().add(card);


    }






    public VBox getView(){

        return root;

    }


}
