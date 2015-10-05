package nz.co.doltech.databind.core.dto;

import nz.co.doltech.databind.annotation.Observable;

@Observable
class Workplace {
    String name;
    String color;
    Person owner;

    public Workplace() {
    }

    public Workplace(String name, String color, Person owner) {
        this.name = name;
        this.color = color;
        this.owner = owner;
    }
}
