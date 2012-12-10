package com.curtisbridges.asset.io;

import java.util.List;

import com.curtisbridges.asset.Asset;


public interface AssetReader extends Runnable {
    void openFileByName(String file);
    boolean isDone();
    List<Asset> getAssets();
}
