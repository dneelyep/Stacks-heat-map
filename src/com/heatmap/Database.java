package com.heatmap;

import javax.swing.*;
import java.sql.*;
import java.util.Properties;

/** Class to contain functionality related to getting data from our database.
 *
 * @author Daniel Neel */
public class Database {

    /** Create a connection to the project's database. */
    public static Connection getConnection() {
        // TODO Should I make a shared, static Connection for this class? Currently, each time this method is called a new Connection is created. Wasteful.
        // Make a connection.
        Connection conn = null;
        Properties connectionProps = new Properties();
        // TODO Give root an actual password, and/or make a restricted permissions user for this application.
        connectionProps.put("user", "root");
        connectionProps.put("password", "");
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", connectionProps);
        } catch (SQLException e) {
            JPanel errorPanel = new JPanel();
            JOptionPane.showMessageDialog(errorPanel, "Error! Could not connect to the database.",
                    "Could not connect to database.", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            // TODO Add the stack trace to the panel, to be used in debugging?
            //errorPanel.add(new JLabel(e.getStackTrace().toString()));
            System.exit(0);
        }

        return conn;
    }

    /** Run the provided query, return the ResultSet of the executed query. */
    public static void executeQuery() {
        // Run queries.
        Statement stmt = null;
        ResultSet results = null;
        try {
            stmt = getConnection().createStatement();
            results = stmt.executeQuery("SELECT * FROM stacks_heat_map_db.book_range WHERE parent_floor = 2");
            while (results.next()) {
                System.out.println("Floor: " + results.getInt("parent_floor") + " X: "
                        + results.getInt("x_coord") + " Y: " + results.getInt("y_coord"));
            }
            // Print the number of results in the query.
            // TODO Make this a method?
            results.last();
            System.out.println("Results: " + results.getRow());
            results.first();
        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
