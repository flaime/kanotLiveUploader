package hjälpprogram;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {

    public Feedback postCall(String stringUrl, String rawBody) {
        HttpURLConnection connection = null;
        try {
            //store necessary query information
            URL jiraURL = new URL(stringUrl);
            String data = rawBody;

            //establish connection and request properties
            connection = (HttpURLConnection) jiraURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.connect();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(data);
            writer.close();
            wr.flush();
            wr.close();

            Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String message = "";
            for (int c; (c = in.read()) >= 0; message += (char) c);
            return new Feedback(message, 200);

        } catch (MalformedURLException e) {
            String message = "MalformedURLException: " + e.getMessage();
            System.err.println(message);
            return new Feedback(message,400 );
        } catch (java.net.UnknownServiceException e) {
            String message = "UnknownServiceException: " + e.getMessage();
            System.err.println(message);
            return new Feedback(message, -1);
        } catch (IOException e) {
            String message = "IOException: " + e.getMessage();
            System.err.println(message);
            if (connection != null) {
                try {
                    String responseMessage = connection.getResponseMessage();
                    System.out.println(responseMessage);
                    message += "\nResponse Message: " + responseMessage;

                    InputStream errorStream = connection.getErrorStream();
                    if (errorStream != null) {
                        Reader in = new BufferedReader(new InputStreamReader(errorStream));
                        for (int c; (c = in.read()) >= 0; System.out.print((char) c)) ;
                    }
                } catch (IOException e2) {
                }
            }
            return new Feedback(message, -1);
        }
    }

    public class Feedback{
        String message;
        int httpCode;

        public Feedback(String message, int httpCode) {
            this.message = message;
            this.httpCode = httpCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getHttpCode() {
            return httpCode;
        }

        public void setHttpCode(int httpCode) {
            this.httpCode = httpCode;
        }
    }

}
