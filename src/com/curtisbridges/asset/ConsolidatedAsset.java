package com.curtisbridges.asset;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConsolidatedAsset {
    private String name;
    private Map<String, String> propertyMap;
    
    public ConsolidatedAsset(Collection<Asset> assets) {
        propertyMap = new HashMap<String, String>();
        
        for(Asset asset : assets) {
            if(name == null)
                name = asset.getName();
            
            if(asset.getName().equals(name)) {
                propertyMap.put(asset.getProperty(), asset.getValue());
            }
        }
    }
    
    public ConsolidatedAsset(Asset prime) {
        name = prime.getName();
        
        propertyMap = new HashMap<String, String>();
        propertyMap.put(prime.getProperty(), prime.getValue());
    }
    
    public void addAsset(Asset asset) {
        propertyMap.put(asset.getProperty(), asset.getValue());
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(propertyMap);
    }
}
