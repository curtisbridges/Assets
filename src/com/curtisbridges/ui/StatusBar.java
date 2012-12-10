package com.curtisbridges.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class StatusBar extends JPanel {
    private List<String> statusTextHistory;
    private JLabel statusLabel;
    private JProgressBar progressBar;

    public StatusBar() {
        statusTextHistory = new ArrayList<String>();
        statusLabel = new JLabel();
        statusLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        progressBar = new JProgressBar();
        // for now, make this invisible. Only will show it when progress is happening.
        progressBar.setVisible(false);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(statusLabel, BorderLayout.WEST);
        // add an empty panel that can expand to fill everything between the label and progress bar
        panel.add(new JPanel());
        panel.add(progressBar, BorderLayout.EAST);
        
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
    }
    
    public String getStatusText() {
        return statusLabel.getText();
    }
    
    public void setStatusText(String text) {
        statusLabel.setText(text);
        if(text != null && !text.isEmpty()) {
            statusTextHistory.add(text);
            statusLabel.setToolTipText(getToolTip());
        }
    }
    
    public List<String> getStatusHistory() {
        return Collections.unmodifiableList(statusTextHistory);
    }
    
    public void clearHistory() {
        statusTextHistory.clear();
    }
    
    public JProgressBar getProgressBar() {
        return progressBar;
    }
    
    private String getToolTip() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html>\n");
        for(String string : statusTextHistory) {
            buffer.append(string);
            buffer.append("<br />");
        }
        buffer.append("</html>");
        return buffer.toString();
    }
}
