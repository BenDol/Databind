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

import java.util.HashMap;
import java.util.List;

public class SampleDTO1 {
    protected String tata;
    protected int toto;
    protected boolean tutu;
    protected long l;
    protected float f;
    protected byte b;

    protected String[] tatas;
    protected int[] totos;
    protected boolean[] tutus;
    protected long[] ls;
    protected float fs[];
    protected byte bs[];

    protected List<Object> os;

    protected HashMap<String, List<SampleDTO1>> map;

    protected SampleDTO1 titi;
}
