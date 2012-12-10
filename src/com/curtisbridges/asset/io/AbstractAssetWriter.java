package com.curtisbridges.asset.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.curtisbridges.asset.Asset;
import com.curtisbridges.asset.AssetConsolidator;
import com.curtisbridges.asset.AssetFilter;
import com.curtisbridges.asset.ConsolidatedAsset;
import com.curtisbridges.asset.MultiPatternAssetFilter;
import com.curtisbridges.asset.PatternAssetFilter;

public abstract class AbstractAssetWriter implements AssetWriter {
    protected static final String TYPE_COMP_SYS = "Computer System";
    protected static final String TYPE_USER = "Last Logged In User";
    protected static final String TYPE_OS = "Operating System";
    
    protected static final String PROP_MODEL = "Model";
    protected static final String PROP_NETBIOS = "NetBios name";
    protected static final String PROP_SN = "Serial number";
    protected static final String PROP_MEM = "Total Physical Memory";
    protected static final String PROP_USER = "User name ";
    protected static final String PROP_OS = "Reported OS";
    
    // Asset Name, Bios Name, User, Model, Serial, Ram, OS
    protected static final String[] HEADERS = { 
        "Computer Name", "Primary User", PROP_MODEL, PROP_SN, "RAM", "Operating System" 
    };
    
    protected BufferedWriter writer;
    protected List<AssetFilter> rowFilters;
    protected MultiPatternAssetFilter multiFilter;
    
    public AbstractAssetWriter() {
        rowFilters = createRowFilter();
        
        multiFilter = new MultiPatternAssetFilter();
        for(AssetFilter filter : rowFilters)
            multiFilter.addAssetFilter(filter);
    }
    
    abstract protected String getHeader();
    abstract protected String getString(ConsolidatedAsset asset);
    abstract protected String getFooter();

    private List<AssetFilter> createRowFilter() {
        AssetFilter col1 = new PatternAssetFilter(PatternAssetFilter.WILDCARD, TYPE_COMP_SYS, PROP_MODEL, 
                PatternAssetFilter.WILDCARD);
        AssetFilter col2 = new PatternAssetFilter(PatternAssetFilter.WILDCARD, TYPE_COMP_SYS, PROP_NETBIOS, 
                PatternAssetFilter.WILDCARD);
        AssetFilter col3 = new PatternAssetFilter(PatternAssetFilter.WILDCARD, TYPE_COMP_SYS, PROP_SN, 
                PatternAssetFilter.WILDCARD);
        AssetFilter col4 = new PatternAssetFilter(PatternAssetFilter.WILDCARD, TYPE_COMP_SYS, PROP_MEM, 
                PatternAssetFilter.WILDCARD);
        AssetFilter col5 = new PatternAssetFilter(PatternAssetFilter.WILDCARD, TYPE_OS, PROP_OS, 
                PatternAssetFilter.WILDCARD);
        AssetFilter col6 = new PatternAssetFilter(PatternAssetFilter.WILDCARD, TYPE_USER, PROP_USER, 
                PatternAssetFilter.WILDCARD);
        
        List<AssetFilter> filters = new ArrayList<AssetFilter>();
        
        filters.add(col1);
        filters.add(col2);
        filters.add(col3);
        filters.add(col4);
        filters.add(col5);
        filters.add(col6);
        
        return filters;
    }
    
    @Override
    public void openFile(String filename) {
        Path path = FileSystems.getDefault().getPath(filename);

        Charset charset = Charset.forName("UTF-8");
        try {
            writer = Files.newBufferedWriter(path, charset);
        }
        catch(IOException exc) {
            exc.printStackTrace();
        }
    }
    
    @Override
    public void writeAssets(List<Asset> assets) {
        // get all the unique names
        List<String> names = new ArrayList<String>();
        for(Asset asset : assets) {
            String name = asset.getName();
            if(!names.contains(name))
                names.add(name);
        }

        // then get the assets that match our filter criteria
        List<Asset> rows = new ArrayList<Asset>();
        for(Asset asset : assets) {
            if(multiFilter.include(asset))
                rows.add(asset);
        }

        // then combine them into a single entity
        List<ConsolidatedAsset> consolidated = AssetConsolidator.process(rows);
        try {
            writer.write(getHeader());
        
            // then generate the output
            for(ConsolidatedAsset asset : consolidated) {
                String line = getString(asset);
                writer.write(line);
            }
            
            writer.write(getFooter());
            writer.close();
        }
        catch(IOException exc) {
            exc.printStackTrace();
        }
    }
    
    protected String getProp(Map<String, String> map, String key) {
        if(map.containsKey(key)) {
            return map.get(key);
        }
        else {
            return "-";
        }
    }
}
