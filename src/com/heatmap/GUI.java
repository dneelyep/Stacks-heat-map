package com.heatmap;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.*;

/** GUI.java - Class that holds the primary GUI for the program.
 *
 * @author Daniel Neel */
public class GUI extends JApplet {

    /** The group that radio buttons for each Floor belongs to. */
    private final ButtonGroup floorButtons = new ButtonGroup();

    /** Panel that contains the components for the shelf map. */
    // TODO Look into breaking floorComponents and its Ranges into a separate class.
    private final JPanel floorComponents = initWithCoords(new JPanel(new GridBagLayout()), new Point(1, 1));

    /** Constraints used to place components in floorComponents. */
    private GridBagConstraints fCConstraints = new GridBagConstraints();

    /** The Range currently clicked on to be edited by the user. */
    private Range focusedRange;

    /** Button to change attributes of the currently clicked Range. */
    private final JButton submitData = initWithCoords(new JButton("Submit"), new Point(6, 1));

    /** Button to cancel editing of a Range's information. */
    private final JButton cancel = initWithCoords(new JButton("Cancel"), new Point(6, 0));

    /** Button to allow the user to close the application. */
    private final JButton quit = initWithCoords(new JButton("Quit"), new Point(6, 38));

    /** The Floor currently being viewed by the user. */
    private Floor currentFloor = null;

    /** A bit of text that displays a title for the program. */
    private final JLabel programTitle = initWithCoords(new JLabel("Stacks Cleanliness Heat Map"), new Point(1, 0));

    /** Button group to only allow viewing the properties of a single property of Ranges at a time. */
    private final ButtonGroup viewDaysSinceButtons = new ButtonGroup();

    /** The primary menu bar for this application. */
    private final JMenuBar menuBar = new JMenuBar();

    /** The File menu located inside the program's menu bar. */
    private final JMenu menu = new JMenu("File");

    /** The menu entry that allows users to change preferences. */
    private final JMenuItem menuPreferences = new JMenuItem("Preferences");

    /** The menu entry that allows users to quit the program. */
    private final JMenuItem menuQuit = new JMenuItem("Quit");

    /** The menu popup that allows users to change preferences. */
    private PreferencesFrame preferencesFrame = new PreferencesFrame(this);

    /** Field to change the clicked-on Range's starting call #. */
    private final JTextField startCallNumberController = initWithCoords(new JTextField("", 10), new Point(3, 0));

    /** Field to change the clicked-on Range's ending call #. */
    private final JTextField endCallNumberController = initWithCoords(new JTextField("", 10), new Point(3, 1));

    /** Floor that represents the library's third floor. */
    private Floor thirdFloor;

    /** Floor that represents the library's fourth floor. */
    private Floor fourthFloor;

    /** Label to display the amount of days considered good since a task has been completed. */
    private JLabel goodLegend = new JLabel("Good: < " + 30 + " days.");

    /** Label to display the amount of days considered ok since a task has been completed. */
    private JLabel okLegend = new JLabel("OK: >= " + 30 +" and < " + 50 + " days.");

    /** Label to display the amount of days considered bad since a task has been completed. */
    private JLabel badLegend = new JLabel("Bad: > " + 50 +" days.");


    /** Attempt to initialize the GUI for this program. */
    @Override
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });

        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully: " + e);
        }
    }

    /** Set up the GUI used for the program.*/
    private void createGUI() {
        // Use the system's default look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        // Set up the program's menu bars.
        menuPreferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preferencesFrame.setVisible(true);
            }
        });

        startCallNumberController.setMinimumSize(new Dimension(100, 20));
        endCallNumberController.setMinimumSize(new Dimension(100, 20));

        // TODO Use an action for menuQuit and quit, since they do the same thing?
        menuQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menu.add(menuPreferences);
        menuPreferences.setMnemonic('P');
        menu.add(menuQuit);
        menuQuit.setMnemonic('Q');
        menu.setMnemonic('F');
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Set the applet's window dimensions to the size of the user's screen.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.NORTHWEST;
        g.weightx = 0.3;

        addComponent(programTitle, g);

        g.gridheight = 36;
        g.weighty = 1;
        add(floorComponents, g);

        fCConstraints.insets = new Insets(0, 6, 0, 6);

        g.weighty = 0;
        g.gridheight = 1;
        addComponent(initWithCoords(new JLabel("Start: "), new Point(2, 0)), g);
        addComponent(initWithCoords(new JLabel("End: "), new Point(2, 1)), g);
        addComponent(initWithCoords(new JLabel("Last checked: "), new Point(2, 2)), g);
        addComponent(initWithCoords(new JLabel("Last shifted: "), new Point(2, 3)), g);
        addComponent(initWithCoords(new JLabel("Last faced: "), new Point(2, 4)), g);
        addComponent(initWithCoords(new JLabel("Last dusted: "), new Point(2, 5)), g);
        addComponent(initWithCoords(new JLabel("Last read: "), new Point(2, 6)), g);

        addComponent(startCallNumberController, g);
        addComponent(endCallNumberController, g);

        for (Activities activity : Activities.values()) {
            // Disallow setting the Date a task was completed to a future Date.
            activity.getController().getMonthView().setUpperBound(new Date());
            addComponent(activity.getController(), g);
        }

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setInputMode(false);
            }
        });
        addComponent(cancel, g);

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        addComponent(quit, g);

        submitData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!startCallNumberController.getText().isEmpty()) {
                    focusedRange.setStart(startCallNumberController.getText());
                }
                if (!endCallNumberController.getText().isEmpty()) {
                    focusedRange.setEnd(endCallNumberController.getText());
                }

                for (Activities activity : Activities.values()) {
                    focusedRange.setDayLast(activity, activity.getController().getDate());
                }

                currentFloor.write();
                setInputMode(false);
            }
        });

        submitData.setEnabled(false);
        addComponent(submitData, g);

        Activities.checked.getViewer().setSelected(true);

        // Add buttons to their button group.
        for (Activities activity : Activities.values()) {
            viewDaysSinceButtons.add(activity.getViewer());
        }

        // TODO Look into adding a ChangeListener to the viewDaysSince ButtonGroup rather than an ActionListener.
        for (Activities activity : Activities.values()) {
            activity.getViewer().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentFloor.updateRangeColors(getSelectedView().getText().toLowerCase());
                    updateLegend();
                }
            });
            addComponent(activity.getViewer(), g);
        }

        thirdFloor = new Floor(3, this);
        fourthFloor = new Floor(4, this);
        currentFloor = thirdFloor;

        thirdFloor.getButton().setSelected(true);
        floorButtons.add(thirdFloor.getButton());
        floorButtons.add(fourthFloor.getButton());

        // TODO Look into removing the g argument to addComponent, since it's the same for each call of the method.
        JPanel legend = initWithCoords(new JPanel() {{
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Legend"),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        }}, new Point(5, 7));
        legend.setBackground(Color.GRAY);
        legend.setLayout(new BoxLayout(legend, BoxLayout.PAGE_AXIS));

        goodLegend.setForeground(Color.GREEN);
        okLegend.setForeground(Color.YELLOW);
        badLegend.setForeground(Color.RED);

        legend.add(goodLegend);
        legend.add(okLegend);
        legend.add(badLegend);

        g.gridwidth = 2;
        addComponent(legend, g);

        g.gridwidth = 1;
        addComponent(thirdFloor.getButton(), g);
        addComponent(fourthFloor.getButton(), g);

        displayFloor(currentFloor);
        allowInput(false);

        // ===============
        // SQL magic here.
        // ===============
        // TODO Clean this up, break it into methods, put them in proper locations.

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
            // TODO Add the stack trace to the panel, to be used in debugging?
            //errorPanel.add(new JLabel(e.getStackTrace().toString()));
            System.exit(0);
        }

        // Run queries.
        String query = "SELECT * FROM stacks_heat_map_db.book_range";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int parentFloor = rs.getInt("parent_floor");
                int xCoord = rs.getInt("x_coord");
                int yCoord = rs.getInt("y_coord");
                System.out.println(parentFloor + "\t" + xCoord + "\t" + yCoord);
            }
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

    /** Display a given Floor floor's Ranges on this GUI, coloring
     * them according to the activity that is currently selected in the GUI. */
     protected void displayFloor(Floor floor) {
        // TODO Add a check to make sure the floor is not focused. Or
        //      alternatively, disable 3rd/4th floor buttons when we enter focus. See inputmode.
        if (floor != thirdFloor && floor != fourthFloor) {
            System.err.println("Error! Tried to display a Floor that does not exist.");
        }
        else {
            floorComponents.removeAll();
            floor.updateRangeColors(getSelectedView().getText().toLowerCase());

            for (Range range : floor.getRanges()) {
                fCConstraints.gridx = range.getXCoord();
                fCConstraints.gridy = range.getYCoord();
                floorComponents.add(range, fCConstraints);
            }

            floorComponents.revalidate();
            programTitle.setText("Stacks Cleanliness Heat Map | " + floor.getButton().getText());
        }
     }

    /** Clear all input fields on this GUI of their data. */
    private void clearInput() {
        startCallNumberController.setText("");
        endCallNumberController.setText("");

        for (Activities activity : Activities.values()) {
            activity.getController().setDate(new Date());
        }
    }

    /** Allow or disallow the user to edit the properties
     * of a clicked-on Range. */
    private void allowInput(Boolean b) {
        // TODO Convert this into a single for each loop.
        for (JComponent picker : Arrays.asList(startCallNumberController, endCallNumberController, submitData, cancel)) {
            picker.setEnabled(b);
        }

        for (Activities activity : Activities.values()) {
            activity.getController().setEnabled(b);
            activity.getViewer().setEnabled(!b);
        }

        thirdFloor.getButton().setEnabled(!b);
        fourthFloor.getButton().setEnabled(!b);
    }

    /** Add a given JComponent component to the GUI. */
    private <T extends JComponent> void addComponent(T component, GridBagConstraints constraints) {
        constraints.gridx = (Integer) component.getClientProperty("x");
        constraints.gridy = (Integer) component.getClientProperty("y");
        add(component, constraints);
    }

    /** (Dis)Allow the user to input data for a selected Range. */
    // TODO Combine this method with allowInput() ?
    protected void setInputMode(Boolean b) {
        if (b) {
            focusedRange.updateColor("clicked");
        }
        else {
            clearInput();
            // TODO Would be better to remove this conditional if possible.
            if (focusedRange != null) {
                focusedRange.updateColor(getSelectedView().getText().toLowerCase());
            }
            focusedRange = null;
        }

        allowInput(b);
    }

    /** Add the coordinates of the Point point to any JComponent component. */
    protected static <T extends JComponent> T initWithCoords(T component, Point point) {
        component.putClientProperty("x", point.x);
        component.putClientProperty("y", point.y);
        return component;
    }

    /** Get the Button that represents the activity that is currently displayed in the GUI. */
    protected AbstractButton getSelectedView() {
        // TODO Shouldn't this be built in to ButtonGroup?
        for (Activities activity : Activities.values()) {
            if (activity.getViewer().isSelected())
                return activity.getViewer();
        }

        // TODO This is a horrible way of handling when no button is selected. Fix it.
        return new JButton("Error!");
    }

    /** Get the Range currently focused on in this GUI. */
    public Range getFocusedRange() {
        return focusedRange;
    }

    /** Set the focusedRange in this GUI to a
     * new Range r. */
    public void setFocusedRange(Range r) {
        focusedRange = r;
    }

    /** Set the current floor being viewed in this GUI. */
    public void setCurrentFloor(Floor floor) {
        currentFloor = floor;
    }

    /** Get the GUI component that changes the focused Range's start call number. */
    public JTextField getStartCallNumberController() {
        return startCallNumberController;
    }

    /** Get the GUI component that changes the focused Range's end call number. */
    public JTextField getEndCallNumberController() {
        return endCallNumberController;
    }

    /** Returns the Floor this GUI is currently displaying. */
    public Floor getCurrentFloor() {
        return currentFloor;
    }

    /** Update the contents of all the labels in the GUI's legend. */
    public void updateLegend() {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String selectedActivity = getSelectedView().getText().toLowerCase();

        goodLegend.setText("Good: < " + prefs.getInt(selectedActivity + ".good", 0) + " days.");
        okLegend.setText("OK: >= " + prefs.getInt(selectedActivity + ".good", 0) +" and < " + prefs.getInt(selectedActivity + ".bad", 100) + " days.");
        badLegend.setText("Bad: >= " + prefs.getInt(selectedActivity + ".bad", 100) +" days.");
    }
}