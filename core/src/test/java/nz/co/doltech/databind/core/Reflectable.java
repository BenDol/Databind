package nz.co.doltech.databind.core;

import nz.co.doltech.databind.core.dto.ANotif;
import nz.co.doltech.databind.core.dto.Employee;
import nz.co.doltech.databind.core.dto.Person;
import nz.co.doltech.databind.reflect.Reflected;

@Reflected(classes = {
    Person.class,
    Employee.class,
    ANotif.class
})
public class Reflectable {

}
