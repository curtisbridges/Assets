package com.curtisbridges.asset;

import java.util.ArrayList;
import java.util.List;

public class MultiPatternAssetFilter implements AssetFilter {
    private boolean isMatchAll = false;
    private List<AssetFilter> filters;
    
    public MultiPatternAssetFilter() {
        filters = new ArrayList<AssetFilter>();
    }
    
    public MultiPatternAssetFilter(boolean matchAll) {
        isMatchAll = matchAll;
    }
    
    public void addAssetFilter(AssetFilter filter) {
        filters.add(filter);
    }
    
    public void removeAssetFilter(AssetFilter filter) {
        filters.remove(filter);
    }
    
    @Override
    public boolean include(Asset asset) {
        if(isMatchAll) {
            for(AssetFilter filter : filters) {
                if(!filter.include(asset))
                    return false;
            }
            
            return true;
        }
        else {
            for(AssetFilter filter : filters) {
                if(filter.include(asset))
                    return true;
            }
            
            return false;
        }
    }
}
