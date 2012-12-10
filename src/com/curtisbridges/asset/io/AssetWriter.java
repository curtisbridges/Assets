package com.curtisbridges.asset.io;

import java.util.List;

import com.curtisbridges.asset.Asset;

public interface AssetWriter {
    void openFile(String filename);
    void writeAssets(List<Asset> assets);
}
