package com.curtisbridges.asset.io;

import java.util.Map;

import com.curtisbridges.asset.ConsolidatedAsset;

public class CsvAssetWriter extends AbstractAssetWriter {
    public CsvAssetWriter() {
        super();
    }

    // Asset Name, Bios Name, User, Model, Serial, Ram, OS
    @Override
    protected String getString(ConsolidatedAsset asset) {
        String name = asset.getName();
        Map<String, String> props = asset.getProperties();
        
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(name);
        buffer.append(",");
        buffer.append(getProp(props, PROP_NETBIOS));
        buffer.append(",");
        buffer.append(getProp(props, PROP_USER));
        buffer.append(",");
        buffer.append(getProp(props, PROP_MODEL));
        buffer.append(",");
        buffer.append(getProp(props, PROP_SN));
        buffer.append(",");
        buffer.append(getProp(props, PROP_MEM));
        buffer.append(",");
        buffer.append(getProp(props, PROP_OS));
        buffer.append("\n");
        
        return buffer.toString();
    }

    @Override
    protected String getHeader() {
        return "";
    }

    @Override
    protected String getFooter() {
        return "";
    }
}
