package com.curtisbridges.asset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AssetConsolidator {
    public static List<ConsolidatedAsset> process(List<Asset> results) {
        List<ConsolidatedAsset> assets = new ArrayList<ConsolidatedAsset>();
        
        // sort the assets by name
        Collections.sort(results, new AssetNameCompator());
        
        // consolidate the assets
        Map<String, List<Asset>> assetMap = new HashMap<String, List<Asset>>();
        for(Asset asset : results) {
            String name = asset.getName();
            if(assetMap.containsKey(name)) {
                List<Asset> systemsList = assetMap.get(name);
                systemsList.add(asset);
            }
            else {
                List<Asset> list = new LinkedList<Asset>();
                list.add(asset);
                assetMap.put(name, list);
            }
        }
        
        for(String name : assetMap.keySet()) {
            List<Asset> all = assetMap.get(name);
            ConsolidatedAsset consolidated = new ConsolidatedAsset(all);
            assets.add(consolidated);
        }
        
        return assets;
    }
}
