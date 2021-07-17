package kanotLiveUploader.utils;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.io.IOException;
import java.util.List;

public class SaveController {

    private final List<Control> toSave;
    public SaveController(List<Control> toSave) {
        this.toSave = toSave;
        toSave.forEach(it -> {
            if (it instanceof TextField) {
                ((TextField) it).textProperty().addListener((observable, oldValue, newValue) -> {
                    safeSave();
                });
            }else if (it instanceof CheckBox)
                ((CheckBox) it).selectedProperty().addListener((observable, oldValue, newValue) -> { safeSave(); });
            else if (it instanceof ToggleButton)
                ((ToggleButton) it).selectedProperty().addListener((observable, oldValue, newValue) -> { safeSave(); });
        });
    }

    public void loadSettings()  {
        try {
            String config = config = FileReaderOwn.readFile("settings.anna");
            setSettings(config);
        } catch (IOException e) {
            System.err.println("Could not load file");
        }
    }

    private void setSettings(String config) {
        String[] lines = config.split("\\r?\\n");

        if(lines.length -1 == toSave.size())
        for (int i = 0; i < toSave.size(); i++) {
            Control control = toSave.get(i);
            if (control instanceof TextField)
                ((TextField) control).setText(lines[i + 1]);
            else if (control instanceof CheckBox)
                ((CheckBox) control).setSelected(!lines[i + 1].equalsIgnoreCase("false"));
            else if (control instanceof ToggleButton)
                ((ToggleButton) control).setSelected(!lines[i + 1].equalsIgnoreCase("false"));
        }
        else
            System.out.println("File is wrong could not load settings");

    }

    public void safeSave() {
        try {
            saveSettings();
        } catch (IOException e) {
            System.err.println("Could not save settings because of:");
            e.printStackTrace();
        }
    }

    private void  saveSettings() throws IOException {
        String[] save = {"Settings for the program the order is important\n"};

        toSave.forEach(it -> {
            if (it instanceof TextField)
                save[0] += ((TextField) it).getText() + "\n";
            else if (it instanceof CheckBox)
                save[0] += (((CheckBox) it).isSelected() ? "true" : "false") + "\n";
            else if (it instanceof ToggleButton)
                save[0] += (((ToggleButton) it).isSelected() ? "true" : "false") + "\n";
        });

        FileWriterOwn.writeFile("settings.anna", save[0], "");
    }
}