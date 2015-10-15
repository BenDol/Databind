/*
 * Copyright 2015 Doltech Systems Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package nz.co.doltech.databind.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import nz.co.doltech.databind.core.dto.Person;
import nz.co.doltech.databind.core.dto.Workplace;
import nz.co.doltech.databind.core.properties.Properties;

public class PathBindingTest extends TestCase {
    public void test() {
        List<Workplace> places = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            places.add(workplace());

        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            persons.add(person(places));

        for (Workplace w : places)
            w.setOwner(persons.get((int) (Math.random() * persons.size())));

        Person personCopy = new Person();

        Properties.setValue(persons, "selected", persons.get(0));

        Binder.bind(persons, "selected").mapTo(personCopy);

        for (int i = 0; i < persons.size(); i++) {
            Properties.setValue(persons, "selected", persons.get(i));

            assertEquals(persons.get(i), personCopy);
        }
    }

    private Workplace workplace() {
        Workplace r = new Workplace();

        r.setName(TestUtils.randomName());
        r.setColor(TestUtils.randomColor());

        return r;
    }

    private Person person(List<Workplace> places) {
        Person p = new Person();

        p.setName(TestUtils.randomName());
        p.setBirthDate(new Date((long) (new Date().getTime() * Math.random())));
        p.setWorkplace(places.get((int) (Math.random() * places.size())));

        return p;
    }
}
