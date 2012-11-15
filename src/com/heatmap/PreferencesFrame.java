package com.heatmap;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;
import java.util.prefs.Preferences;

/** Class to represent user preferences in the stacks
 * heat map program. */
public class PreferencesFrame extends JFrame {

    /** ComboBox to change preferences for the task selected in the ComboBox. */
    JComboBox<String> preferredTaskController = new JComboBox<String>(new Vector<String>(Arrays.asList("Checked", "Shifted", "Faced", "Dusted", "Read")));

    /** Slider that allows the user to change the number of days considered
     * good since the current task has been completed. */
     private JSlider daysGoodController = new JSlider();

    /** Slider that allows the user to change the number of days considered
     * bad since the current task has been completed. */
    private JSlider daysBadController = new JSlider();

    /** Label to display the current value of the good slider. */
    private JLabel goodLabel = new JLabel("Good:");

    /** Label to display the current value of the bad slider. */
    private JLabel badLabel = new JLabel("Bad:");

    /** Button that, upon click, saves any modified preferences for the user. */
    private JButton applyPreferences = new JButton("Apply");

    /** Set of custom preferences that this user has saved. */
    private Preferences preferences = Preferences.userNodeForPackage(getClass());

    /** Button to close out the this PreferencesFrame. */
    private JButton close = new JButton("Close");

    // TODO Look into converting this class into a Singleton, since I only need one instance of it in the program.
    /** Create a new PreferencesFrame with the given
     * title. Add to the frame various components to allow
     * the user to change the amount of days considered acceptable
     * between task completion. */
    public PreferencesFrame() {
        super("Preferences");
        setLayout(new GridLayout(4, 2));

        // TODO Review use of listeners, see if I can make use of more appropriate types. See docs at http://docs.oracle.com/javase/tutorial/uiswing/events/handling.html
        add(new JLabel("# days since:"));
        add(preferredTaskController);

        for (JSlider slider : Arrays.asList(daysGoodController, daysBadController)) {
            slider.setMajorTickSpacing(10);
            slider.setMinorTickSpacing(5);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            // TODO Have the slider automatically snap while moving the slider, rather than only after releasing mouse.
            //      This looks like it will be more difficult than anticipated. I don't think this is supported out of the box.
            slider.setSnapToTicks(true);
        }

        add(goodLabel);

        daysGoodController.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                goodLabel.setText("Good: " + source.getValue());
                applyPreferences.setEnabled(true);
            }
        });
        // TODO Should I add a default value to days rather than hard-coding 0? IE Using clientProperties.
        daysGoodController.setValue(preferences.getInt("checked.good", 0));

        add(daysGoodController);
        add(badLabel);

        daysBadController.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                badLabel.setText("Bad: " + source.getValue());
                applyPreferences.setEnabled(true);
            }
        });
        daysBadController.setValue(preferences.getInt("checked.bad", 0));
        add(daysBadController);

        // Set actions to perform upon Apply/Close clicking.
        applyPreferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePreferences();
                applyPreferences.setEnabled(false);
            }
        });

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        applyPreferences.setEnabled(false);
        add(applyPreferences);
        add(close);

        // TODO Disallow invalid daysGood/Bad values. For example, days considered good should always be < daysBad.
        // TODO Make screen size values variables I can access?
        // TODO Disallow input into the main GUI when the preferences dialog appears. See the modality API?
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) screenSize.getWidth() / 2, (int) screenSize.getHeight() / 2);

        pack();
    }

    // TODO Rename, implement method since the previous method was to write stuff to disk.
    /** Save user preferences for the currently focused activity. */
    public void savePreferences() {
        preferences.put(preferredTaskController.getSelectedItem().toString().toLowerCase() + ".good", Integer.toString(daysGoodController.getValue()));
        preferences.put(preferredTaskController.getSelectedItem().toString().toLowerCase() + ".bad", Integer.toString(daysBadController.getValue()));
    }
}
