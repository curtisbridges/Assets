package com.curtisbridges.asset.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

import com.curtisbridges.asset.Asset;
import com.curtisbridges.asset.events.LoggingAssetListener;

public class OpenCsvAssetReader extends AbstractAssetReader {
    private boolean isDone = false;
    private CSVReader csvReader;
    
    @Override
    public void openFileByName(String filename) {
        try {
            isDone = false;
            csvReader = new CSVReader(new FileReader(filename), ',', '"');
        }
        catch(FileNotFoundException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void run() {
        fireAssetParsingStarted();

        String [] strings;
        try {
            while ((strings = csvReader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                try {
                    Asset asset = createAsset(strings);
                    assets.add(asset);
                    fireAssetFound(asset);
                }
                catch(AssetParsingException exc) {
                    fireAssetParsingWarning(exc.getMessage());
                }
            }
        }
        catch(IOException exc) {
            fireAssetParsingFailed(exc);
            return;
        }
        
        isDone = true;
        fireAssetParsingCompleted();
    }

    @Override
    protected Asset parseLine(String string) throws AssetParsingException {
        return null;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    protected String getNextLine() {
        return null;
    }

    /**
     * This is here to unit test this class.
     * @param argv
     */
    public static void main(String[] argv) {
        OpenCsvAssetReader reader = new OpenCsvAssetReader();
        
        reader.addAssetListener(new LoggingAssetListener());
        reader.openFileByName("input.csv");
        reader.run();
    }
}
