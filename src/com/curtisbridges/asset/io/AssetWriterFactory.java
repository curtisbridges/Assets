package com.curtisbridges.asset.io;

public class AssetWriterFactory {
    public static AssetWriter createAssetWriter(String filename) throws AssetException {
        if(filename.toLowerCase().endsWith(".csv"))
            return new CsvAssetWriter();
        else if(filename.toLowerCase().endsWith(".html"))
            return new HtmlAssetWriter();
        else
            throw new AssetException("Unknown writer for type: " + filename);
    }
}
