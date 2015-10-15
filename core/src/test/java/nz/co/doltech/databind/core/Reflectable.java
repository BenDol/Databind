package nz.co.doltech.databind.core;

import nz.co.doltech.databind.core.dto.Person;
import nz.co.doltech.databind.reflect.Reflected;

@Reflected(classes = {
    Person.class
})
public class Reflectable {}
