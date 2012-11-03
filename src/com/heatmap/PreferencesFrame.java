package com.heatmap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/** Class to represent user preferences in the stacks
 * heat map program. */
public class PreferencesFrame extends JFrame {

    /** ComboBox to change preferences for the task selected in the ComboBox. */
    JComboBox<String> preferredTask;

    // TODO Better names for these text fields.
    /** Text field that holds the number of days considered
     * good since the current task has been completed. */
    private JTextField daysGood = new JTextField(3);

    /** Text field that holds the number of days considered
     * OK (passable) since the current task has been completed. */
    private JTextField daysOK = new JTextField(3);

    /** Text field that holds the number of days considered
     * bad since the current task has been completed. */
    private JTextField daysBad = new JTextField(3);

    /** Button that, upon click, saves any modified preferences for the user. */
    // TODO Grey out/disable apply when changes have not yet been made to a field.
    private JButton applyPreferences = new JButton("Apply");

    /** Representation of the properties associated with the user's preferences. */
    private Properties preferenceProps = new Properties();

    /** Button to cancel any changes in preferences currently entered. */
    private JButton cancel = new JButton("Cancel");

    // TODO Look into converting this class into a Singleton, since I only need one instance of it in the program.
    /** Create a new PreferencesFrame with the given
     * title. Add to the frame various components to allow
     * the user to change the amount of days considered acceptable
     * between task completion. */
    public PreferencesFrame(String title) {
        super(title);
        setLayout(new GridLayout(5, 2));

        // Load in preferences for the user. TODO Make this a method?
        try {
            FileInputStream in = new FileInputStream("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/src/com/heatmap/preferences");
            preferenceProps.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

        // create application properties with default
        Properties applicationProps = new Properties(preferenceProps);
        applicationProps.list(System.out);

        String[] taskStrings = {"Checked", "Shifted", "Faced", "Dusted", "Read"};
        // TODO Move preferredTask to a field.
        preferredTask = new JComboBox<String>(taskStrings);
        // TODO Convert the relevant fields to class members here.
        add(new JLabel("# days since:"));
        add(preferredTask);

        // TODO Add validation to make sure JTextField values are integers.
        add(new JLabel("Good:"));
        add(daysGood);
        add(new JLabel("OK:"));
        add(daysOK);
        add(new JLabel("Bad:"));
        add(daysBad);

        // Set actions to perform upon Cancel/Apply clicking.
        applyPreferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePreferences();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        add(applyPreferences);
        add(cancel);
        // TODO Add a Cancel button to let the user not save any changes they make?
        // TODO Make screen size values variables I can access?
        // TODO Move some of this setup into the constructor?
        // TODO Disallow input into the main GUI when the preferences dialog appears. See the modality API?
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) screenSize.getWidth() / 2, (int) screenSize.getHeight() / 2);

        pack();
    }

    /** Save all user preferences to disk. */
    public void savePreferences() {
        for (JTextField textInput : Arrays.asList(daysGood, daysBad)) {
            // Make sure the input is an integer, and set appropriate properties.
            try {
                Integer.parseInt(textInput.getText());
                preferenceProps.setProperty(preferredTask.getSelectedItem().toString().toLowerCase() + ".good", daysGood.getText());
                preferenceProps.setProperty(preferredTask.getSelectedItem().toString().toLowerCase() + ".bad", daysBad.getText());
            } catch (NumberFormatException e) {
                System.out.println("Error! Input should be an integer.");
            }

            // Now store the properties to the preferences file. TODO Make this step a method?
            try {
                FileOutputStream out = new FileOutputStream("C:/Users/Daniel/Desktop/Programming/Java/Stacks-heat-map/src/com/heatmap/preferences");
                preferenceProps.store(out, "---Stacks heat map preferences---");
            } catch (FileNotFoundException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }

            // TODO Add an Apply button maybe?
            // TODO Actually, rather than clearing input, the text areas should just display what the current preference
            //      values are.
            clearInput();
        }
    }

    /** Remove input from all input components in this PreferencesFrame. */
    public void clearInput() {
        daysGood.setText("");
        daysOK.setText("");
        daysBad.setText("");
    }
}
