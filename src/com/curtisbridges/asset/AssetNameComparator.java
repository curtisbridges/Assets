package com.curtisbridges.asset;

import java.util.Comparator;

public class AssetNameComparator implements Comparator<Asset> {
    @Override
    public int compare(Asset o1, Asset o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
