package com.articleapp;

import com.model.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class ArticleDaoExtension {
    private final static Logger logger = LogManager.getLogger();
    public int getMaxId() {
        try {
            Connection c = Objects.requireNonNull(ConnectionManager.getConnection());
            PreparedStatement ps = c.prepareStatement("select max(id) as max from article;");
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                return result.getInt("max");
            }
        } catch (SQLException e) {
            System.out.println("SQLException-error: " + e);
        }
        return -1;
    }

    public String getResponseByUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                Scanner scan = new Scanner(url.openStream());
                if (scan.hasNext()) {
                    return scan.nextLine();
                }
            }
        } catch (IOException e) {
            logger.error("getResponseByUrl-error: ", e);
        }
        return null;
    }

    public int getDeleteResponseCode(String urlString, String method) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.connect();
            return conn.getResponseCode();
        } catch (IOException e) {
            logger.error("getDeleteResponseCode-error: ", e);
        }
        return -1;
    }

    public void putPostResponseByUrl(String urlString, String jsonData, String method) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(method);
            try(OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())){
                out.write(jsonData);
            }
            conn.getInputStream();
        } catch (IOException e) {
            logger.error("putPostResponseByUrl-error: ", e);
        }
    }
}
