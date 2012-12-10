package com.curtisbridges.asset;

import java.util.Comparator;

public class ConsolidatedAssetComparator implements Comparator<ConsolidatedAsset> {
    @Override
    public int compare(ConsolidatedAsset o1, ConsolidatedAsset o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
