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
package nz.co.doltech.databind.core.inherited;

import nz.co.doltech.databind.annotation.Observable;

import java.util.HashMap;
import java.util.List;

@Observable(inheritDepth = 1)
public class Parent extends Grandparent {

    String parentsString;
    int parentsNumber;

    HashMap<String, List<Integer>> parentsMap;

    private double parentsPrivateNoGetterSetter;
    private double parentsPrivateHasGetterSetter;

    public double getParentsPrivateHasGetterSetter() {
        return parentsPrivateHasGetterSetter;
    }

    public void setParentsPrivateHasGetterSetter(double parentsPrivateHasGetterSetter) {
        this.parentsPrivateHasGetterSetter = parentsPrivateHasGetterSetter;
    }
}
