package com.heatmap;

import org.jdesktop.swingx.JXDatePicker;

import java.util.Date;

/** A simple class that stores a Date along with a date
 * picker to change that Date. */
public class StacksDate extends Date {

    /** A graphical component to change the Date stored in this StacksDate. */
    private JXDatePicker datePicker;

    /** Create this StacksDate along with the specified JXDatePicker picker. */
    public StacksDate(JXDatePicker picker) {
        super();
        datePicker = picker;
    }
}
