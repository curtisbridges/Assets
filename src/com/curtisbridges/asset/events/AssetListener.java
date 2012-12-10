package com.curtisbridges.asset.events;

import java.util.List;

import com.curtisbridges.asset.Asset;

public interface AssetListener {
    void assetParsingStarted();
    void assetFound(Asset asset);
    void assetParsingCompleted(List<Asset> assets);
    void assetParsingWarning(String reason);
    void assetParsingFailed(Exception exc);
}
