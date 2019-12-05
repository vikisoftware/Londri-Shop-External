package com.vikisoft.externallondrishops.helper;

public class WeeklyOff {
    private char c;
    private boolean selected = false;

    public WeeklyOff(char c, boolean selected) {
        this.c = c;
        this.selected = selected;
    }

    public char getDay() {
        return c;
    }

    public void setDay(char c) {
        this.c = c;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
