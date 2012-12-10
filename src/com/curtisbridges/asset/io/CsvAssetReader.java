package com.curtisbridges.asset.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import com.curtisbridges.asset.Asset;
import com.curtisbridges.asset.events.LoggingAssetListener;


public class CsvAssetReader extends AbstractAssetReader {
    protected Path path;
    protected String line;
    protected BufferedReader reader;

    @Override
    public void openFileByName(String filename) {
        path = FileSystems.getDefault().getPath(".", filename);

        Charset charset = Charset.forName("UTF-8");
        try {
            reader = Files.newBufferedReader(path, charset);
        }
        catch(IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    protected Asset parseLine(String string) throws AssetParsingException {
        if(string == null)
            throw new AssetParsingException("Nothing to parse, line is null.");
        
        String[] strings = string.split(",", -1);
        
        if(strings.length != AssetType.values().length)
            throw new AssetParsingException(string);
        
        return createAsset(strings);
    }

    @Override
    public boolean isDone() {
        return path != null && line != null;
    }

    @Override
    protected String getNextLine() {
        try {
            return reader.readLine();
        }
        catch(IOException exc) {
            line = null;
            exc.printStackTrace();
            return null;
        }
    }

    /**
     * This is here to unit test this class.
     * @param argv
     */
    public static void main(String[] argv) {
        CsvAssetReader reader = new CsvAssetReader();
        
        reader.addAssetListener(new LoggingAssetListener());
        reader.openFileByName("input.csv");
        reader.run();
    }
}
