package com.curtisbridges.asset.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.curtisbridges.asset.ConsolidatedAsset;

@SuppressWarnings("serial")
public class AssetTableModel extends AbstractTableModel {
    private String[] COLUMNS = {"System", "Properties"};
	private List<ConsolidatedAsset> assets = new ArrayList<ConsolidatedAsset>();
	
	public AssetTableModel() {
	    super();
	}
	
	public void setAssets(List<ConsolidatedAsset> list) {
	    assets.clear();
	    assets.addAll(list);
	    
	    fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public int getRowCount() {
		return assets.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
	    ConsolidatedAsset asset = assets.get(row);
	    switch(col) {
	        case 0:
	            return asset.getName();
	        case 1:
//	            return asset.getDescription();
//	        case 2:
	            return asset.getProperties().size();
	        default:
	            return "unknown";
	    }
	}
	
	public ConsolidatedAsset getAsset(int row) {
	    return assets.get(row);
	}
	
    @Override
    public String getColumnName(int column) {
        if(column < COLUMNS.length)
            return COLUMNS[column];
        else
            return "ERROR";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0: return String.class;
            case 1: return Integer.class;
            default: return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // for now, don't allow editing.
        return false;
    }
}
