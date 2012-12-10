package com.curtisbridges.asset.ui;

import java.awt.Color;
import java.awt.Component;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import com.curtisbridges.asset.ConsolidatedAsset;

@SuppressWarnings("serial")
public class AssetTableCellRenderer extends DefaultTableCellRenderer {
    private static final String[] IMPORTANT_KEYS = { 
        "Model", "NetBios name", "Operating System", "Serial number", "Total Physical Memory", "User name" 
    };

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // alternate colors
        if(row % 2 == 0)
            comp.setBackground(Color.lightGray);
        else
            comp.setBackground(Color.white);
        
        if(isSelected)
            comp.setBackground(Color.blue);
        
        TableModel model = table.getModel();
        if(model instanceof AssetTableModel) {
            AssetTableModel assetModel = (AssetTableModel) model;
            ConsolidatedAsset asset = assetModel.getAsset(row);
            setToolTipText(generateTooltipText(asset));
        }
        
        return comp;
    }
    
    private String generateTooltipText(ConsolidatedAsset asset) {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("<html><table>");
        buffer.append("<tr><th>Key</th><th>Value</th></tr>");
        
        Map<String, String> map = asset.getProperties();
        for(String key : IMPORTANT_KEYS) {
            if(map.containsKey(key)) {
                buffer.append("<tr><td>");
                buffer.append(key);
                buffer.append("</td><td>");
                buffer.append(map.get(key));
                buffer.append("</td></tr>\n");
            }
        }
        
        buffer.append("</table></html>");
        
        return buffer.toString();
    }
}
