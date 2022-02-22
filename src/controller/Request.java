package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

import patterns.Singleton;

public class Request {
    private Connection conn;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private ResultSetMetaData rsmd;

    // get data using query string 
    protected Vector<String> executeDb(String type, String query, String... params) {
        conn = Singleton.getInstance();
        Vector<String> result = new Vector<String>();

        try {
            statement = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            if (type.equals("update")) {
                statement.executeUpdate();
                return null;
            } else {
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    result.add(resultSet.getString(1));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    protected Vector< Vector<String> > executeDb2(String type, String query, String... params) {
        conn = Singleton.getInstance();
        Vector< Vector<String> > result = new Vector< Vector<String> >();

        try {
            statement = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            if (type.equals("update")) {
                statement.executeUpdate();
                return null;
            } else {
                resultSet = statement.executeQuery();
                rsmd = resultSet.getMetaData();
                
                while (resultSet.next()) {
                    Vector<String> row = new Vector<String>();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        row.add(resultSet.getString(i));
                    }
                    result.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    protected int getRequestId() {
        String query = "SELECT id from Request ORDER BY id DESC";
        Vector<String> ids = executeDb("query", query); 
        if (ids.size() == 0) {
            return 0;
        }
        return Integer.parseInt(ids.firstElement());
    }

    protected void sendData(String type, String studentId, String teacherId, String message) {
        int currentId = getRequestId();
        String query = "INSERT INTO Request VALUES (?, ?, ?, ?, ?, ?)";
        executeDb("update", query, Integer.toString(++currentId), studentId, teacherId, type, message, "Pending");
    }

    public void showMenu() {}

}