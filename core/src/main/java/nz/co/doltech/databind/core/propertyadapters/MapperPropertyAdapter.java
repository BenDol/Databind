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
package nz.co.doltech.databind.core.propertyadapters;

import nz.co.doltech.databind.core.ModelMapper;

public class MapperPropertyAdapter extends WriteOnlyPropertyAdapter {
    private Object destinationOfMapping;
    private Object mapperResources = null;

    public MapperPropertyAdapter(Object destinationOfMapping) {
        this.destinationOfMapping = destinationOfMapping;
    }

    @Override
    public void setValue(Object object) {
        if (mapperResources != null)
            ModelMapper.freeMapping(mapperResources);

        if (object != null)
            mapperResources = ModelMapper.map(object, destinationOfMapping);
    }
}
