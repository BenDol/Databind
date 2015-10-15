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

import java.util.List;

public class SampleDTO2 {
    String tata;
    int toto;
    boolean tutu;
    long l;
    float f;
    byte b;

    String[] tatas;
    int[] totos;
    boolean[] tutus;
    long[] ls;
    float fs[];
    byte bs[];

    List<Object> os;

    // self reference
    SampleDTO2 titi2;

    SampleDTO1 titi;

    List<SampleDTO1> titis;

    List<SampleDTO1> totos4;
}
