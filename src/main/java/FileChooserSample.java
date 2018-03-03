import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class FileChooserSample extends Application {


    @Override
    public void start(final Stage stage) {
        stage.setTitle("File Chooser Sample");

        final FileChooser fileChooser = new FileChooser();

        final Button openButton = new Button("Load file");
        final Button openMultipleButton = new Button("Open Pictures...");

        openButton.setOnAction(
                e -> {
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        List<String> encodedUrls = readFile(file);
                        if (encodedUrls == null)
                            return;
                        List<String> parsedIds = Parser.tinyUrl2Id(encodedUrls);
                        writeFile(parsedIds);
                    }
                });


        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(openButton, 0, 0);
        GridPane.setConstraints(openMultipleButton, 1, 0);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openButton, openMultipleButton);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup));
        stage.show();
    }

    private void writeFile(List<String> parsedIds) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\\\Users\\\\Freemahn\\\\Downloads\\\\tiny_url2.txt"))) {
            for (String s : parsedIds) {
                writer.write(s + "\n");
            }

        } catch (IOException e) {
            System.err.println(e.getCause());
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private List<String> readFile(File file) {
        List<String> stringList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                stringList.add(line);
                line = br.readLine();
            }

        } catch (IOException e) {
            Logger.getLogger(
                    FileChooserSample.class.getName()).log(
                    Level.SEVERE, null, e
            );
            return null;
        }

        return stringList;


    }
}
