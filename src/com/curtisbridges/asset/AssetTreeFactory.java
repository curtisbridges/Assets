package com.curtisbridges.asset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.curtisbridges.asset.io.AssetException;

public class AssetTreeFactory {
    private static final String[] ALLOWED_TYPES = { "Computer System", "BIOS", "Last Logged in User" };
    private static final List<String> ALLOWED_TYPE_LIST = Arrays.asList(ALLOWED_TYPES);
    
    public static TreeNode createTree(List<Asset> assets) {
        Map<String, Map<String, Map<String, String>>> nameMap = createTreeMap(assets);
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("All");
        
        for(String name : nameMap.keySet()) {
            DefaultMutableTreeNode nameNode = new DefaultMutableTreeNode(name);
            
            Map<String, Map<String, String>> typeMap = nameMap.get(name);
            for(String type : typeMap.keySet()) {
                if(ALLOWED_TYPE_LIST.contains(type)) {
                    DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode(type);
                    
                    Map<String, String> properties = typeMap.get(type);
                    for(String property : properties.keySet()) {
                        DefaultMutableTreeNode propertyNode = new DefaultMutableTreeNode(property);
                        DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(properties.get(property));
                        
                        propertyNode.add(valueNode);
                        typeNode.add(propertyNode);
                    }
                    
                    nameNode.add(typeNode);
                }
            }
            
            root.add(nameNode);
        }
        
        return root;
    }
    
    public static Map<String, Map<String, Map<String, String>>> createTreeMap(List<Asset> assets) {
        Map<String, Map<String, Map<String, String>>> nameMap = new HashMap<String, Map<String, Map<String, String>>>();

        for(Asset asset : assets) {
            try {
                createNode(nameMap, asset);
            }
            catch(AssetException exc) {
                // do nothing
            }
        }
        
        return nameMap;
    }
    
    private static boolean createNode(Map<String, Map<String, Map<String, String>>> nameMap, Asset asset) throws AssetException {
        // first, handle the system name
        String name = asset.getName();
        if(nameMap.containsKey(name)) {
            // it already exists, keep moving down the tree
            Map<String, Map<String, String>> typeMap = nameMap.get(name);
            String type = asset.getType();
            // check to see if the type already exists
            if(typeMap.containsKey(type)) {
                // it does, so keep going...
                Map<String, String> properties = typeMap.get(type);
                String property = asset.getProperty();
                if(properties.containsKey(property)) {
                    // this property already exists?!? Check to see if it is the same value, at least...
                    String value = properties.get(property);
                    if(value.equals(asset.getValue())) {
                        // they are the same, odd but not a problem
                        return false;
                    }
                    else {
                        // they differ -- which value to use?!
                        throw new AssetException("Same node, different values for: " + asset);
                    }
                }
                else {
                    properties.put(asset.getProperty(), asset.getValue());
                    return true;
                }
            }
            else {
                // it doesn't, so create it
                Map<String, String> properties = new HashMap<String, String>();
                properties.put(asset.getProperty(), asset.getValue());
                typeMap.put(type, properties);
                return true;
            }
        }
        else {
            // if not there, create the property map
            Map<String, String> property = new HashMap<String, String>();
            // and store it
            property.put(asset.getProperty(), asset.getValue());
            // and then create the type map
            Map<String, Map<String, String>> typeMap = new HashMap<String, Map<String, String>>();
            // store the property under the type
            typeMap.put(asset.getType(), property);
            // create the name map last
            nameMap.put(name,typeMap);
            
            return true;
        }
    }
}
