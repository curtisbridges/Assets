package com.curtisbridges.asset.events;

import java.util.List;

import com.curtisbridges.asset.Asset;

public class LoggingAssetListener implements AssetListener {
    @Override
    public void assetParsingStarted() {
        System.out.println("Asset parsing started.");
    }

    @Override
    public void assetFound(Asset asset) {
        System.out.println("Asset found: " + asset);
    }

    @Override
    public void assetParsingCompleted(List<Asset> assets) {
        System.out.println("Asset parsing completed, " + assets.size() + " found.");
    }

    @Override
    public void assetParsingWarning(String reason) {
        System.out.println("Warning: " + reason);
    }

    @Override
    public void assetParsingFailed(Exception exc) {
        exc.printStackTrace();
    }
}
