package nz.co.doltech.databind.core;

import junit.framework.TestCase;
import nz.co.doltech.databind.core.properties.Properties;

public class DTOInheritanceTest extends TestCase {
    public void test() {
        A a = new SubA();
        A b = new SubA();

        Binder.bind(a, "firstName").to(b, "firstName");

        a.setFirstName("titi");
        assertEquals("titi", b.getFirstName());

        b.setFirstName("tata");
        assertEquals("tata", a.getFirstName());
    }

    class A {
        private String firstName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }

    class SubA extends A {
        public void setFirstName(String firstName) {
            super.setFirstName(firstName);

            Properties.notify(this, "firstName");
        }
    }
}