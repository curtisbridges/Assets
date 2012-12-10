package com.curtisbridges.asset.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class AssetAboutBox extends JDialog {
    private static final String LOGO = "images/logo.jpg";
    private ImageIcon logo;
    
    public AssetAboutBox() {
        logo = new ImageIcon(LOGO);
        
        setLayout(new BorderLayout());
        setContentPane(createContents());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    private JPanel createContents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String string = "<html>Created by <a href=\"curtis@curtisbridges.com\">Curtis Bridges</a></html>";
        panel.add(new JLabel(string, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(new JLabel(logo), BorderLayout.CENTER);
        
        return panel;
    }
}
