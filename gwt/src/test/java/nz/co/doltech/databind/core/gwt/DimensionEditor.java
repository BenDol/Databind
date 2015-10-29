/*
 *
 *  * Copyright 2015 Doltech Systems Ltd.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  * use this file except in compliance with the License. You may obtain a copy of
 *  * the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  * License for the specific language governing permissions and limitations under
 *  * the License.
 *
 */
package nz.co.doltech.databind.core.gwt;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBox;

public class DimensionEditor {
    private TextBox m2 = new TextBox();
    private CheckBox approx = new CheckBox();

    public TextBox getM2() {
        return m2;
    }

    public void setM2(TextBox m2) {
        this.m2 = m2;
    }

    public CheckBox getApprox() {
        return approx;
    }

    public void setApprox(CheckBox approx) {
        this.approx = approx;
    }
}
