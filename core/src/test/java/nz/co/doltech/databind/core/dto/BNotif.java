package nz.co.doltech.databind.core.dto;

import nz.co.doltech.databind.core.properties.Properties;

public class BNotif {
    private String firstName;

    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        Properties.notify(this, "firstName");
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        Properties.notify(this, "lastName");
    }
}