package kanotLiveUploader.utils;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import kanotLiveUploader.paresePdf.Tävling;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ahlin hamberg
 */
public class PushController {


    private static String comepetition = "{}";
    private static String clubs = "{}";
    private LocalDateTime nextForcePush = LocalDateTime.now();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    GuiLogger logger;
    public PushController(GuiLogger logger) {
        this.logger = logger;
    }

    public enum pushStatus {
        UNKNOWN_ERROR,
        IO_ERROR,
        SUCCESSFULLY
    }

    public pushStatus pushCompetition(Tävling competition, String serverSokvag, String competitionName, boolean forcePuch) {

        try {
            String comeptitionTemp = competition.getJsonString();
            String clubsTemp = competition.getClubJson();

            if (forcePuch) {
                //Does not change the time for next force puch
                log("Manual force pusch");
                comepetition = comeptitionTemp;
                clubs = clubsTemp;
                sendPostTävling(serverSokvag, comepetition, competitionName);
            } else if ((comepetition.equalsIgnoreCase(comeptitionTemp) && clubs.equalsIgnoreCase(clubsTemp)) && nextForcePush.isAfter(LocalDateTime.now())) {
                log("The data is identical ignore push and waits for next time (next force pusch is " + nextForcePush + "");
                System.out.println("comepetition: " + comepetition);
            } else {
                nextForcePush = LocalDateTime.now().plusMinutes(10);
                comepetition = comeptitionTemp;
                clubs = clubsTemp;
                sendPostTävling(serverSokvag, comepetition, competitionName);
            }

        } catch (IOException e) {
            log("Gick inte att pusha prova ändra sökvögen eller liknande lycka till ");
            e.printStackTrace();
            return pushStatus.IO_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return pushStatus.UNKNOWN_ERROR;
        }
        if (forcePuch)
            log("Puschat tävlingen (" + LocalDateTime.now().format(formatter) + ")");
        else
            log("Automatiskt puschat tävlingen (" + LocalDateTime.now().format(formatter) + ")");
        return pushStatus.SUCCESSFULLY;
//		if(competitionName.getText().isEmpty()){
//			showAlertInformation("Error", "\"Competition name\" måstet vara ifyllt.", "\"Competition name\" måstet vara ifyllt. Fyll i och försök igen.");
//		}


    }

    private void showAlertInformation(String tittle, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(tittle);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void sendPostKlubbar(String strURL, String klubbarJson) throws IOException {
        strURL += "/api/club/";
        postToServer(strURL, klubbarJson);
    }

    private void sendPostTävling(String strURL, String jsonTävling, String competitionName) throws Exception {
        strURL += "/api/competitions/" + URLEncoder.encode(competitionName, "UTF-8");
        postToServer(strURL, jsonTävling);
    }

    private void postToServer(String puschUrl, String body) throws IOException {
        System.out.println("Body being posted: \n" + body);
        HttpUtils.Feedback feedback = new HttpUtils().postCall(puschUrl, body);

        System.out.println("Sending 'POST' request to URL : " + puschUrl);
        System.out.println("Response status: " + feedback.getHttpCode());
        System.out.println(feedback.getMessage());

        log("Sending 'POST' request to URL : " + puschUrl);
        log("Response Code : " + feedback.getHttpCode());
        log("Response info : " + feedback.getMessage());
    }

    private void log(String message) {
        logger.logToGui(message);
    }
}