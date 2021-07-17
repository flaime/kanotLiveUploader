package kanotLiveUploader.database;

import java.sql.*;

public class DatabasConection {

    private Connection conn;

    public DatabasConection(String urlToDtabas) throws SQLException, ClassNotFoundException{
        String driver="net.ucanaccess.jdbc.UcanaccessDriver";
        Class.forName(driver);
        conn= DriverManager.getConnection("jdbc:ucanaccess://"+urlToDtabas);
    }

    public ResultSet query(String SQL) throws SQLException{
        Statement stmt=conn.createStatement();
        ResultSet result=stmt.executeQuery(SQL);
        return result;

    }
}
