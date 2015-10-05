package nz.co.doltech.databinder.databinding.test.dto;

import nz.co.doltech.databinder.databinding.annotation.Observable;

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
