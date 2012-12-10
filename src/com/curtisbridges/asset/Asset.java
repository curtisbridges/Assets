package com.curtisbridges.asset;


public class Asset {
    public static final String PART_SEPARATOR = "\\";
    
	// Device,Type,Property,Value
	private String name;
	private String type;
	private String property;
	private String value;
	
	public Asset(String string) {
		name = string;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
	public boolean equals(Object arg0) {
		return name.equals(arg0);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name + PART_SEPARATOR + type + PART_SEPARATOR + property + PART_SEPARATOR + value;
	}
}
