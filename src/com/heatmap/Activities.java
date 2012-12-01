package com.heatmap;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;

// TODO Disallow opening multiple instances of this program? Could lead to inconsistent data.
/** A list of the valid activities to perform on shelves in the stacks. */
public enum Activities {
    checked("checked", GUI.initWithCoords(new JXDatePicker(), new Point(3, 2)), GUI.initWithCoords(new JRadioButton("Checked"), new Point(6, 2))),
    shifted("shifted", GUI.initWithCoords(new JXDatePicker(), new Point(3, 3)), GUI.initWithCoords(new JRadioButton("Shifted"), new Point(6, 3))),
    faced("faced",     GUI.initWithCoords(new JXDatePicker(), new Point(3, 4)), GUI.initWithCoords(new JRadioButton("Faced"), new Point(6, 4))),
    dusted("dusted",   GUI.initWithCoords(new JXDatePicker(), new Point(3, 5)), GUI.initWithCoords(new JRadioButton("Dusted"), new Point(6, 5))),
    read("read",       GUI.initWithCoords(new JXDatePicker(), new Point(3, 6)), GUI.initWithCoords(new JRadioButton("Read"), new Point(6, 6)));

    // TODO Also store coordinates and the good/bad values in here if that makes sense.
    /** A String representation of the activity to be performed. */
    private final String asText;

    /** A JXDatePicker that allows the user to control the day an activity was last performed. */
    private final JXDatePicker controller;

    /** Radio button that allows the user to view the status of completion on this activity. */
    private final JRadioButton viewer;

    Activities(String asText, JXDatePicker controller, JRadioButton viewer) {
        this.asText = asText;
        this.controller = controller;
        this.viewer = viewer;
    }

    /** Get this Actvities' String representation. */
    @Override
    public String toString() {
        return asText;
    }

    /** Get this Activities' controller. */
    public JXDatePicker getController() {
        return controller;
    }

    /** Get the radio button that allows the user to view the status of this activity. */
    public JRadioButton getViewer() {
        return viewer;
    }
}
