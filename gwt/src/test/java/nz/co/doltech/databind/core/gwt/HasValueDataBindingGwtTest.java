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
package nz.co.doltech.databind.core.gwt;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.TextBox;
import nz.co.doltech.databind.reflect.Reflected;
import nz.co.doltech.databind.reflect.ReflectionRegistry;

/**
 * In addition to the normal binding adapters, the GWT version provides
 * binding functionality for Widgets implementing the HasValue interface.
 * <p/>
 * Here are the related tests.
 *
 * @author Arnaud Tournier
 * @author Ben Dol
 */
@Reflected(classes = {
    TextBox.class, DTO.class

})
public class HasValueDataBindingGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "nz.co.doltech.databind.core.DatabindTest";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        ReflectionRegistry.register();
    }

    /**
     * Binding of a DTO property to a TextBox
     */
    public void test01() {
        DTO dto = new DTO();

        TextBox box = new TextBox();

        Binder.bind(dto, "name").to(box);

        dto.setName("toto");
        assertEquals("toto", box.getValue());

        box.setValue("titi", true);
        assertEquals("titi", dto.getName());

        /**
         * The setText method doesn't throw an event so the
         * value does not get updated in b2.
         * Note that if the user changes the text and leaves
         * the text box, it will trigger an event and activate
         * the data binding
         */
        box.setText("tata");
        assertEquals("titi", dto.getName());
    }

    /**
     * Bind two TextBox together
     */
    public void test02() {
        TextBox b1 = new TextBox();
        TextBox b2 = new TextBox();

        Binder.bind(b1).to(b2);

        b2.setValue("titi", true);
        assertEquals("titi", b1.getText());

        /**
         * The setText method doesn't throw an event so the
         * value does not get updated in b2.
         * Note that if the user changes the text and leaves
         * the text box, it will trigger an event and activate
         * the data binding
         */
        b1.setText("toto");
        assertEquals("titi", b2.getValue());
    }
}