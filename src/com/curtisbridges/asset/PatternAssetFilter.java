package com.curtisbridges.asset;

public class PatternAssetFilter implements AssetFilter {
    public static final String WILDCARD = "*";
    
    private String name;
    private String type;
    private String property;
    private String value;
    
    public PatternAssetFilter(String namePart, String typePart, String propPart, String valuePart) {
        name = namePart;
        type = typePart;
        property = propPart;
        value = valuePart;
    }
    
    @Override
    public boolean include(Asset asset) {
        return  matches(name, asset.getName()) &&
                matches(type, asset.getType()) &&
                matches(property, asset.getProperty()) &&
                matches(value, asset.getValue());
    }
    
    private boolean matches(String left, String right) {
        if(left.equals(WILDCARD))
            return true;
        else
            return left.equals(right);
    }
}
