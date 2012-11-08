package com.heatmap;

import org.jdesktop.swingx.JXDatePicker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

/** GUI.java - Class that holds the primary GUI for the program.
 *
 * @author Daniel Neel */
public class GUI extends JApplet {
    /** Floor that represents the library's third floor. */
    private final Floor thirdFloor = new Floor(3, this);

    /** Floor that represents the library's fourth floor. */
    private final Floor fourthFloor = new Floor(4, this);
    
    /** The group that radio buttons for each Floor belongs to. */
    private final ButtonGroup floorButtons = new ButtonGroup();

    /** Panel that contains the components for the shelf map. */
    // TODO Look into breaking floorComponents and its Ranges into a separate class.
    private final JPanel floorComponents = addCoords(new JPanel(new GridBagLayout()), 1, 1);

    /** The Range currently clicked on to be edited by the user. */
    private Range focusedRange;

    /** Field to change the clicked-on Range's starting call #. */
    private final JTextField startCallNumber = addCoords(new JTextField("", 10), 3, 0);

    /** Field to change the clicked-on Range's ending call #. */
    private final JTextField endCallNumber = addCoords(new JTextField("", 10), 3, 1);

    /** Date picker to change the Date the clicked Range last had pickup done on it. */
    // TODO Use input verification on the text fields to ensure values are appropriate.
    //      See http://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html#inputVerification
    // TODO Add some checking to make sure the user can't add dates from the future. See my future getDaysSince method.
    // TODO Actually, I can just use datePicker.getMonthView().setLower/UpperBound();
    private final JXDatePicker dayLastChecked = addCoords(new JXDatePicker(), 3, 2);

    /** Date picker to change the Date the clicked Range was last shifted. */
    private final JXDatePicker dayLastShifted = addCoords(new JXDatePicker(), 3, 3);

    /** Date picker to change the Date the clicked Range was last faced. */
    private final JXDatePicker dayLastFaced = addCoords(new JXDatePicker(), 3, 4);

    /** Date picker to change the Date the clicked Range was last dusted. */
    private final JXDatePicker dayLastDusted = addCoords(new JXDatePicker(), 3, 5);

    /** Date picker to change the Date the clicked Range was last shelf read. */
    private final JXDatePicker dayLastRead = addCoords(new JXDatePicker(), 3, 6);

    /** Button to change attributes of the currently clicked Range. */
    private final JButton submitData = addCoords(new JButton("Submit"), 6, 1);

    /** Button to cancel editing of a Range's information. */
    private final JButton cancel = addCoords(new JButton("Cancel"), 6, 0);

    /** Button to allow the user to close the application. */
    private final JButton quit = addCoords(new JButton("Quit"), 6, 38);

    // TODO Consider associating associating a with each Date last X a GUI component. Cleaner potentially.
    /** The collection of input components, gathered together for easy iteration. */
    private final Set<JComponent> inputComponents = new LinkedHashSet<JComponent>();

    /** Constraints used to place components in floorComponents. */
    private GridBagConstraints fCConstraints = new GridBagConstraints();

    /** The Floor currently being viewed by the user. */
    private Floor currentFloor = null;

    /** A bit of text that displays a title for the program. */
    private final JLabel programTitle = addCoords(new JLabel("Stacks Cleanliness Heat Map"), 1, 0);

    /** Button which, when clicked, will change the colors of Ranges to indicate how many days it's
     * been since each Range has been checked. */
    private final JRadioButton viewDaysSinceChecked = addCoords(new JRadioButton("Checked"), 6, 2);

    /** Button which, when clicked, will change the colors of Ranges to indicate how many days it's
     * been since each Range has been shifted. */
    private final JRadioButton viewDaysSinceShifted = addCoords(new JRadioButton("Shifted"), 6, 3);

    /** Button which, when clicked, will change the colors of Ranges to indicate how many days it's
     * been since each Range has been faced. */
    private final JRadioButton viewDaysSinceFaced = addCoords(new JRadioButton("Faced"), 6, 4);

    /** Button which, when clicked, will change the colors of Ranges to indicate how many days it's
     * been since each Range has been dusted. */
    private final JRadioButton viewDaysSinceDusted = addCoords(new JRadioButton("Dusted"), 6, 5);

    /** Button which, when clicked, will change the colors of Ranges to indicate how many days it's
     * been since each Range has been shelf read. */
    private final JRadioButton viewDaysSinceRead = addCoords(new JRadioButton("Read"), 6, 6);

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
    private PreferencesFrame preferencesFrame = new PreferencesFrame();

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
    // TODO Make an options menu, with the ability to change the dates used to determine when a range is good/bad/ok.
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

        //thirdFloor.getButton().addMouseListener(this);
        //fourthFloor.getButton().addMouseListener(this);

        thirdFloor.getButton().setSelected(true);
        floorButtons.add(thirdFloor.getButton());
        floorButtons.add(fourthFloor.getButton());

        // TODO Look into removing the g argument to addComponent, since it's the same for each call of the method.
        addComponent(thirdFloor.getButton(), g);
        addComponent(fourthFloor.getButton(), g);

        g.gridheight = 36;
        g.weighty = 1;
        add(floorComponents, g);

        fCConstraints.insets = new Insets(0, 6, 0, 6);

        g.weighty = 0;
        g.gridheight = 1;
        addComponent(addCoords(new JLabel("Start: "), 2, 0), g);
        addComponent(addCoords(new JLabel("End: "),          2, 1), g);
        addComponent(addCoords(new JLabel("Last checked: "), 2, 2), g);
        addComponent(addCoords(new JLabel("Last shifted: "), 2, 3), g);
        addComponent(addCoords(new JLabel("Last faced: "),   2, 4), g);
        addComponent(addCoords(new JLabel("Last dusted: "), 2, 5), g);
        addComponent(addCoords(new JLabel("Last read: "), 2, 6), g);

        startCallNumber.setMinimumSize(new Dimension(100, 20));
        endCallNumber.setMinimumSize(new Dimension(100, 20));

        // Add all input fields to our input components set.
        inputComponents.addAll(Arrays.asList(startCallNumber, endCallNumber,
                dayLastChecked, dayLastShifted, dayLastFaced, dayLastDusted, dayLastRead));

        // TODO Review, replace any unneeded object arrays with uses of for (Object o : Arrays.asList(objects)).

        // Add properties to each dayLastX component, to allow for easier programming.
        dayLastChecked.putClientProperty("activity", "checked");
        dayLastShifted.putClientProperty("activity", "shifted");
        dayLastFaced.putClientProperty("activity", "faced");
        dayLastDusted.putClientProperty("activity", "dusted");
        dayLastRead.putClientProperty("activity", "read");

        for (JComponent component : inputComponents) {
            addComponent(component, g);
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

        // TODO Search for these addMouseListeners and reenable if/when needed.
        //submitData.addMouseListener(this);
        submitData.setEnabled(false);
        addComponent(submitData, g);

        viewDaysSinceChecked.setSelected(true);

        // Add buttons to their button group.
        viewDaysSinceButtons.add(viewDaysSinceChecked);
        viewDaysSinceButtons.add(viewDaysSinceShifted);
        viewDaysSinceButtons.add(viewDaysSinceFaced);
        viewDaysSinceButtons.add(viewDaysSinceDusted);
        viewDaysSinceButtons.add(viewDaysSinceRead);

        // TODO Look into adding a ChangeListener to the viewDaysSince ButtonGroup rather than an ActionListener.
        for (Enumeration<AbstractButton> buttons = viewDaysSinceButtons.getElements(); buttons.hasMoreElements();) {
            JRadioButton currentButton = (JRadioButton) buttons.nextElement();
            currentButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentFloor.updateRangeColors(getSelectedView().getText().toLowerCase());
                }
            });

            addComponent(currentButton, g);
        }

        allowInput(false);
        currentFloor = thirdFloor;
        displayFloor(currentFloor);
    }

    /** Display a given Floor floor's Ranges on this GUI, coloring
     * them according to the activity that is currently selected in the GUI. */
     private void displayFloor(Floor floor) {
        // TODO Add a check to make sure the floor is not focused. Or
        // alternatively, disable 3rd/4th floor buttons when we enter focus. See inputmode.
        if (floor != thirdFloor && floor != fourthFloor) {
            System.err.println("Error! Tried to display a Floor that does not exist.");
        }
        else {
            floorComponents.removeAll();
            floor.updateRangeColors(getSelectedView().getText().toLowerCase());

            for (Range r : floor.getRanges()) {
                fCConstraints.gridx = r.getXCoord();
                fCConstraints.gridy = r.getYCoord();
                //r.addMouseListener(this);
                r.updateTooltip();
                floorComponents.add(r, fCConstraints);
            }

            floorComponents.revalidate();
            programTitle.setText("Stacks Cleanliness Heat Map | " + floor.getButton().getText());
        }
    }

    /** Clear all input fields on this GUI of their data. */
    private void clearInput() {
        for (JComponent j : inputComponents) {
            if (j instanceof  JTextField)
                ((JTextField) j).setText("");
            else
                ((JXDatePicker) j).setDate(new Date());
        }
    }

    /** Allow or disallow the user to edit the properties
     * of a clicked-on Range. */
     private void allowInput(Boolean b) {
         for (JComponent j : inputComponents) {
             j.setEnabled(b);
         }

        submitData.setEnabled(b);
        cancel.setEnabled(b);
    }

    /** Allow the user to change the clicked Range on the viewed Floor's properties. */
    /*@Override
    public void mouseClicked(MouseEvent e) {
        Component clickedComponent = e.getComponent();

        // TODO Fix how submit/cancel text disappear when not focused.
        if (clickedComponent == submitData && submitData.isEnabled()) {
            if (!startCallNumber.getText().isEmpty()) {
                focusedRange.setStart(startCallNumber.getText());
            }
            if (!endCallNumber.getText().isEmpty()) {
                focusedRange.setEnd(endCallNumber.getText());
            }

            // TODO Another chance for iteration.
            focusedRange.setDayLast((String) dayLastChecked.getClientProperty("activity"), dayLastChecked.getDate());
            focusedRange.setDayLast((String) dayLastShifted.getClientProperty("activity"), dayLastShifted.getDate());
            focusedRange.setDayLast((String) dayLastFaced.getClientProperty("activity"),   dayLastFaced.getDate());
            focusedRange.setDayLast((String) dayLastDusted.getClientProperty("activity"),  dayLastDusted.getDate());
            focusedRange.setDayLast((String) dayLastRead.getClientProperty("activity"),    dayLastRead.getDate());

            currentFloor.write();
            setInputMode(false);
        }

        else if (clickedComponent == thirdFloor.getButton() || clickedComponent == fourthFloor.getButton()) {
            if (clickedComponent == thirdFloor.getButton()) {
                currentFloor = thirdFloor;
            }
            else {
                currentFloor = fourthFloor;
            }

            setInputMode(false);
            displayFloor(currentFloor);
            currentFloor.write();
        }
    }*/

    /** Add a given JComponent component to the GUI. */
    private <T extends JComponent> void addComponent(T component, GridBagConstraints constraints) {
        constraints.gridx = (Integer) component.getClientProperty("x");
        constraints.gridy = (Integer) component.getClientProperty("y");
        add(component, constraints);
    }

    /** (Dis)Allow the user to input data for a selected Range. */
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

    /** Add the coordinates (x,y) to any JComponent component. */
    protected static <T extends JComponent> T addCoords(T component, int x, int y) {
        component.putClientProperty("x", x);
        component.putClientProperty("y", y);
        return component;
    }

    /** Get the Button that represents the activity that is currently displayed in the GUI. */
    protected AbstractButton getSelectedView() {
        for (Enumeration<AbstractButton> buttons = viewDaysSinceButtons.getElements(); buttons.hasMoreElements();) {
            AbstractButton currentButton = buttons.nextElement();
            if (currentButton.isSelected()) {
                return currentButton;
            }
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

    /** Returns the start call number input text field. */
    public JTextField getStartCallNumber() {
        return startCallNumber;
    }

    /** Returns the start call number input text field. */
    public JTextField getEndCallNumber() {
        return endCallNumber;
    }

    /** Get the input picker that allows you to change the day
     * a Range was last checked for cleanliness. */
    public JXDatePicker getDayLastChecked() {
        return dayLastChecked;
    }

    /** Get the input picker that allows you to change the day
     * a Range was last checked shifted. */
    public JXDatePicker getDayLastShifted() {
        return dayLastShifted;
    }

    /** Get the input picker that allows you to change the day
     * a Range was last faced. */
    public JXDatePicker getDayLastFaced() {
        return dayLastFaced;
    }

    /** Get the input picker that allows you to change the day
     * a Range was last dusted. */
    public JXDatePicker getDayLastDusted() {
        return dayLastDusted;
    }

    /** Get the input picker that allows you to change the day
     * a Range was last shelf read. */
    public JXDatePicker getDayLastRead() {
        return dayLastRead;
    }
}