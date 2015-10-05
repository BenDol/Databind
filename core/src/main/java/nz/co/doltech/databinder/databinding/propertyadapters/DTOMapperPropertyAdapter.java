package nz.co.doltech.databinder.databinding.propertyadapters;

import nz.co.doltech.databinder.databinding.DTOMapper;

public class DTOMapperPropertyAdapter extends WriteOnlyPropertyAdapter {
    Object destinationOfMapping;
    Object mapperResources = null;

    public DTOMapperPropertyAdapter(Object destinationOfMapping) {
        this.destinationOfMapping = destinationOfMapping;
    }

    @Override
    public void setValue(Object object) {
        if (mapperResources != null)
            DTOMapper.freeMapping(mapperResources);

        if (object != null)
            mapperResources = DTOMapper.map(object, destinationOfMapping);
    }
}