package nz.co.doltech.databind.core.gwt;

import nz.co.doltech.databind.core.properties.Properties;

public class DTO {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Properties.notify(this, "name");
    }
}