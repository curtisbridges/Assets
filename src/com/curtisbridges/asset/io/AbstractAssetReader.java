package com.curtisbridges.asset.io;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.curtisbridges.asset.Asset;
import com.curtisbridges.asset.events.AssetListener;

public abstract class AbstractAssetReader implements AssetReader {
    protected enum AssetType { SystemName, Type, Property, Value }
    
    private List<AssetListener> listeners;
    protected List<Asset> assets;

    public AbstractAssetReader() {
        listeners = new CopyOnWriteArrayList<AssetListener>();
        assets = new ArrayList<Asset>();
    }

    @Override
    public void run() {
        fireAssetParsingStarted();

        while(!isDone()) {
            String line = getNextLine();
            if(line == null)
                break;
            
            try {
                Asset asset = parseLine(line);
                assets.add(asset);
            }
            catch(AssetParsingException exc) {
                fireAssetParsingWarning(exc.getMessage());
            }
        }
      
        fireAssetParsingCompleted();
    }
    
    @Override
    public List<Asset> getAssets() {
        return assets;
    }

    protected abstract String getNextLine();
    protected abstract Asset parseLine(String string) throws AssetParsingException;
    
    protected Asset createAsset(String[] values) throws AssetParsingException {
        String name = values[getIndex(AssetType.SystemName)];
        if(name.isEmpty())
            throw new AssetParsingException("Empty system name");
        
        if(values.length < 4)
            throw new AssetParsingException("Wrong number of values!");
        
        Asset asset = new Asset(name);
        
        asset.setType(values[getIndex(AssetType.Type)]);
        asset.setProperty(values[getIndex(AssetType.Property)]);
        asset.setValue(values[getIndex(AssetType.Value)]);
        
        return asset;
    }
    
    private int getIndex(AssetType type) {
        return type.ordinal();
    }

    public void addAssetListener(AssetListener listener) {
        listeners.add(listener);
    }

    public void removeAssetListener(AssetListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireAssetParsingStarted() {
        for(AssetListener listener : listeners)
            listener.assetParsingStarted();
    }

    protected void fireAssetFound(Asset asset) {
        for(AssetListener listener : listeners)
            listener.assetFound(asset);
    }
    
    protected void fireAssetParsingCompleted() {
        for(AssetListener listener : listeners)
            listener.assetParsingCompleted(assets);
    }
    
    protected void fireAssetParsingWarning(String string) {
        for(AssetListener listener : listeners)
            listener.assetParsingWarning(string);
    }
    
    protected void fireAssetParsingFailed(Exception exc) {
        for(AssetListener listener : listeners)
            listener.assetParsingFailed(exc);
    }
}
