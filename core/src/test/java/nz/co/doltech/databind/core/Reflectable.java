package nz.co.doltech.databind.core;

import nz.co.doltech.databind.core.dto.ANotif;
import nz.co.doltech.databind.core.dto.Employee;
import nz.co.doltech.databind.core.dto.Person;
import nz.co.doltech.databind.reflect.Reflected;

import java.util.ArrayList;

@Reflected(classes = {
    Person.class,
    Employee.class,
    ANotif.class,
    ArrayList.class
})
public class Reflectable {

}
