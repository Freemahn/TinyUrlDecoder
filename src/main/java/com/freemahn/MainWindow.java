package com.freemahn;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow
        extends Application {

    private Text actionStatus;
    private Text inputFileLabel;
    private Text outputFileLabel;
    private Stage savedStage;
    private Stage stage;
    private Button executeBtn;
    private TextArea txtArea;
    private static final String titleTxt = "Tiny Url Encoder-Decoder";
    private static final String defaultFileName = "output.txt";

    private File inputFile, outputFile;
    private List<String> encodedUrls;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle(titleTxt);

        // Window label
        Label label = new Label("Tiny Url Encoder-Decoder");
        label.setTextFill(Color.DARKBLUE);
        label.setFont(Font.font("Calibri", FontWeight.BOLD, 36));
        HBox labelHb = new HBox();
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(label);

        // Text area in a scrollpane and label
        Label txtAreaLabel = new Label("Text:");
        txtAreaLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));
        txtArea = new TextArea();
        txtArea.setWrapText(true);
        txtArea.setDisable(true);

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(txtArea);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setPrefHeight(150);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox txtAreaVbox = new VBox(5);
        txtAreaVbox.setPadding(new Insets(5, 5, 5, 5));
        txtAreaVbox.getChildren().addAll(txtAreaLabel, scroll);
        // Button
        Button openInputFile = new Button("Choose input file(TXT)...");
        openInputFile.setOnAction(new OpenFileButtonListener());

        Button saveOutputFile = new Button("Choose optional output file(TXT)...");
        saveOutputFile.setOnAction(new SaveButtonListener());
        saveOutputFile.setTooltip(new Tooltip("Default output file is output.txt nearby jar location"));

        executeBtn = new Button("Execute parsing");
        executeBtn.setDisable(true);
        executeBtn.setOnAction(new ExecuteListener());

        HBox buttonHb1 = new HBox(10);
        buttonHb1.setAlignment(Pos.CENTER);
        buttonHb1.getChildren().addAll(openInputFile, saveOutputFile, executeBtn);
        outputFile = new File(defaultFileName);
        // Status message text
        actionStatus = new Text();
        actionStatus.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));
        actionStatus.setFill(Color.FIREBRICK);
        actionStatus.setText("Please choose input file, output file(optional) " +
                "and then execute\nCurrent output file: " + outputFile.getAbsolutePath());

        // Vbox
        VBox vbox = new VBox(30);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(labelHb, txtAreaVbox, buttonHb1, actionStatus);

        // Scene
        Scene scene = new Scene(vbox, 700, 400); // w x h
        primaryStage.setScene(scene);
        primaryStage.show();

        savedStage = primaryStage;


    }

    private class OpenFileButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Load input file");
            inputFile = fileChooser.showOpenDialog(stage);
            if (inputFile != null) {
                encodedUrls = readFile(inputFile);
                txtArea.setText(String.join("\n", encodedUrls));
                txtArea.setDisable(false);
                executeBtn.setDisable(false);
            }

        }
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Save file");
            fileChooser.setInitialFileName(defaultFileName);
            outputFile = fileChooser.showSaveDialog(savedStage);

        }
    }

    private class ExecuteListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            if (encodedUrls == null)
                return;
            List<String> parsedResults = new ArrayList<>();
            if (encodedUrls.get(0).startsWith("/x/"))
                parsedResults = Parser.tinyUrl2Id(encodedUrls);
            else if (isNumeric(encodedUrls.get(0))) {
                parsedResults = Parser.id2TinyUrl(encodedUrls);
            } else {
                actionStatus.setText("Wrong input file format, should be /x/ChDH or 13045770");
            }

            txtArea.setText(String.join("\n", parsedResults));
            writeFile(parsedResults);

        }
    }


    private List<String> readFile(File file) {
        List<String> stringList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                stringList.add(line);
                line = br.readLine();
            }
            actionStatus.setText("File " +
                    inputFile.toString() + " successfully read.\nCurrent output file:  " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            actionStatus.setText("An ERROR occurred while opening input file!" +
                    inputFile.toString() +"\nCurrent output file:  " + outputFile.getAbsolutePath());
        }
        return stringList;
    }

    private void writeFile(List<String> parsedIds) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String s : parsedIds) {
                writer.write(s + "\n");
            }
            actionStatus.setText("File " +
                    outputFile.getAbsolutePath() + " successfully written!");
        } catch (IOException e) {
            e.printStackTrace();
            actionStatus.setText("An ERROR occurred while saving the file!" +
                    outputFile.toString());
        }

    }

    public static boolean isNumeric(String str) {
        try {
            int d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}