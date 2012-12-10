package com.curtisbridges.ui.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

public class ScreenUtilities {
    public static void centerOnScreen(Window window) {
        if(window.isVisible()) {
            Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension windowDim = window.getSize();
            
            int x = (screenRes.width - windowDim.width) / 2;
            int y = (screenRes.height - windowDim.height) / 2;
            
            window.setLocation(x, y);
        }
        else {
            System.out.println("Can't center a non-visible component!");
        }
    }
    
    public static void centerOnParent(Window window, Component parent) {
        if(window.isVisible() && parent.isVisible()) {
            Point parentLoc = parent.getLocationOnScreen();
            Dimension parentDim = parent.getSize();
            Dimension windowDim = window.getSize();
            
            int x = (parentDim.width - windowDim.width) / 2;
            int y = (parentDim.height - windowDim.height) / 2;
            
            window.setLocation(parentLoc.x+x, parentLoc.y+y);
        }
        else {
            System.out.println("Can't center a non-visible component!");
        }
    }
}
