package kanotLiveUploader.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kanotLiveUploader.database.ParseDatabasToTävling;
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
    TextField status;
    @FXML
    TextField lastUpdated;
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

    final FileChooser fileChooser = new FileChooser();
    private File databasFile = null;
    private ParseDatabasToTävling pdb = null;
    SaveController saveController;

    @FXML
    protected void initialize() throws IOException {
        List<Control> toSave = new LinkedList<>(Arrays.asList(
                databaseUrl,
                start,
                stop
        ));

        saveController = new SaveController(toSave);
        saveController.loadSettings();
        databasFile = new File(databaseUrl.getText());
        System.out.println(databasFile);

        setEnabled(!databaseUrl.getText().isEmpty());
        runLoop = start.isSelected();

        runMainLoop();
    }


    private void runMainLoop() {


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (runLoop) {
                    System.out.println("Running: " + new java.util.Date());
                    readDatabase();
                    puchDatabase();
                }
            }
        }, 0, 30000);
    }

    private void puchDatabase() {

    }

    @FXML
    private void start() {
        runLoop = true;
        stop.setSelected(false);
    }


    @FXML
    private void stop() {
        runLoop = false;
        start.setSelected(false);
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
        status.setText("Loading database");
        raceData.setText(formatJson(pdb.parserDatbas().getJsonString()));
        status.setText("Done loading database");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        lastUpdated.setText(dateFormat.format(date));
    }

    public void endProgram() {
        runLoop = false;
        timer.cancel();
        timer.purge();
    }
}
