package com.curtisbridges.asset;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.curtisbridges.asset.io.AssetWriter;
import com.curtisbridges.asset.io.AssetWriterFactory;
import com.curtisbridges.asset.io.OpenCsvAssetReader;
import com.curtisbridges.asset.ui.AssetFrame;
import com.curtisbridges.ui.util.ScreenUtilities;

public class Main {
    private static final String INPUT_ARG = "-input";
    private static final String OUTPUT_ARG = "-output";
    
    private Main(String[] args) {
        try {
            handleCommandLine(args);
        }
        catch(Exception exc) {
            printUsage();
        }        
    }

    private void showUI(File file) {
        ScreenUtilities.setSystemLnF();
        
        AssetFrame frame = new AssetFrame(file);
        
        frame.setVisible(true);
        ScreenUtilities.centerOnScreen(frame);
    }
    
    private void handleCommandLine(String[] args) throws Exception {
        if(args.length == 0) {
            // there are no command line args, use default settings.
            showUI(null);
        }
        else if(args.length % 2 == 1) {
            // our args come in pairs, so if there aren't pairs, throw an exception
            throw new Exception("Invalid args: " + Arrays.toString(args));
        }
        else {
            boolean isInput = false;
            boolean isOutput = false;
            
            String input = null;
            String output = null;
            
            // handle whatever was passed in
            for(String arg : args) {
                if(isInput) {
                    input = arg;
                    isInput = false;
                }
                
                if(isOutput) {
                    output = arg;
                    isOutput = false;
                }
                
                if(arg.equals(INPUT_ARG))
                    isInput = true;
                if(arg.equals(OUTPUT_ARG))
                    isOutput = true;
            }
            
            // now use the args
            if(input != null && output != null) {
                // we have both input and output, so just process the file and get out
                OpenCsvAssetReader reader = new OpenCsvAssetReader();
                reader.openFileByName(input);
                reader.run();
                
                List<Asset> assets = reader.getAssets();
                AssetWriter writer = AssetWriterFactory.createAssetWriter(output);
                writer.openFile(output);
                writer.writeAssets(assets);
            }
            else if(input != null) {
                // just have the input file, load the UI with it.
                File file = new File(input);
                showUI(file);
            }
            else {
                // wtf?
                throw new Exception("I have no idea how to handle these args: " + Arrays.toString(args));
            }
        }
    }
    
    private void printUsage() {
        System.out.println("assets [-input inputfile][-output outputfile]");
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    @SuppressWarnings("unused")
        Main main = new Main(args);
	}
}
