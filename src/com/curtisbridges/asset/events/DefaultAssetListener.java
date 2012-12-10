package com.curtisbridges.asset.events;

import java.util.List;

import com.curtisbridges.asset.Asset;

public class DefaultAssetListener implements AssetListener {
    @Override
    public void assetParsingStarted() {
        // Intentionally blank
    }

    @Override
    public void assetFound(Asset asset) {
        // Intentionally blank
    }

    @Override
    public void assetParsingCompleted(List<Asset> assets) {
        // Intentionally blank
    }

    @Override
    public void assetParsingFailed(Exception exc) {
        // Intentionally blank
    }

    @Override
    public void assetParsingWarning(String reason) {
        // Intentionally blank
    }
}
