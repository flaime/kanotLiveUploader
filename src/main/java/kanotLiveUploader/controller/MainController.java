package kanotLiveUploader.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kanotLiveUploader.database.ParseDatabasToTävling;
import kanotLiveUploader.paresePdf.Tävling;
import kanotLiveUploader.utils.PushController;
import kanotLiveUploader.utils.SaveController;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static kanotLiveUploader.utils.JsonUtils.formatJson;

public class MainController {
    private Stage primaryStage;
    private boolean runLoop = false;
    private Timer timer = new Timer();

    public void setStageAndSetupListeners(Stage primaryStage) {
        this.primaryStage = primaryStage;

    }

    @FXML
    Label status;
    @FXML
    Label lastUpdated;
    @FXML
    TextField databaseUrl;
    @FXML
    TextArea raceData;
    @FXML
    ToggleButton start;
    @FXML
    ToggleButton stop;
    @FXML
    Button readDatabase;
    @FXML
    TextField competitionName;

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final FileChooser fileChooser = new FileChooser();
    private final PushController pushController = new PushController();
    private File databasFile = null;
    private ParseDatabasToTävling pdb = null;
    SaveController saveController;
    private Tävling tävling = null;

    @FXML
    protected void initialize() throws IOException {
        List<Control> toSave = new LinkedList<>(Arrays.asList(
                databaseUrl,
                start,
                stop,
                competitionName
        ));

        saveController = new SaveController(toSave);
        saveController.loadSettings();
        databasFile = new File(databaseUrl.getText());
        System.out.println(databasFile);

        setEnabled(!databaseUrl.getText().isEmpty());
        runLoop = start.isSelected();

        competitionName.setEditable(!start.isSelected());

        runMainLoop();
    }


    private void runMainLoop() {

        long timeBetweenExecution = 30000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (runLoop) {
                    System.out.println("Running: " + new java.util.Date());
                    readDatabase();
                    pushDatabase();
                    Date executionTime = new Date(this.scheduledExecutionTime() + timeBetweenExecution);
                    Platform.runLater(() -> {
                        status.setText("Klar, nästa läsning och push är: " + DATE_FORMAT.format(executionTime));
                    });
                }
            }
        }, 0, timeBetweenExecution);
    }

    private void pushDatabase() {
//        pushController.pushCompetition(tävling, "https://kanot.live" ,competitionName.getText());
    }

    @FXML
    private void start() {
        if (competitionName.getText().isEmpty()) {
            showAlertInformation("Saknar tävlingens namn", "Saknar tävlingens namn", "För att kunna starta kräver namnet på tävlignen skriv in det innan du startar.");
            start.setSelected(false);
        } else {
            runLoop = true;
            stop.setSelected(false);
            competitionName.setEditable(false);
        }
    }


    @FXML
    private void stop() {
        runLoop = false;
        start.setSelected(false);
        competitionName.setEditable(true);
    }

    @FXML
    private void selectDatabaseFile() {
        databasFile = fileChooser.showOpenDialog(null);
        if (databasFile == null || !databasFile.getAbsolutePath().endsWith(".mdb")) {
            setEnabled(false);
            databaseUrl.setText("");
        } else {
            databaseUrl.setText(databasFile.getAbsolutePath());
            setEnabled(true);
        }
    }

    private void setEnabled(boolean enabled) {
        stop.setDisable(!enabled);
        start.setDisable(!enabled);

        if (!enabled) {
            stop.setSelected(true);
            start.setSelected(false);
            runLoop = false;
        }

        readDatabase.setDisable(!enabled);

    }

    @FXML
    private void readDatabase() {
        raceData.setText("");
        if (databasFile == null) {

        } else {
            pdb = new ParseDatabasToTävling(databasFile.getAbsolutePath());

        }
        Platform.runLater(() -> {
            status.setText("Loading database");
        });
        tävling = pdb.parserDatbas();
        raceData.setText(formatJson(tävling.getJsonString()));
        Platform.runLater(() -> {
            status.setText("Done loading database");
        });

        Date date = new Date();
        Platform.runLater(() -> {
            lastUpdated.setText(DATE_FORMAT.format(date));
        });
    }

    public void endProgram() {
        runLoop = false;
        timer.cancel();
        timer.purge();
    }

    private void showAlertInformation(String tittle, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tittle);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
