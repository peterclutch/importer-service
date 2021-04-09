package com.peter.importerservice.service.mapper;

import com.peter.importerservice.domain.Factory;
import com.peter.importerservice.service.bo.FactoryBO;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class FactoryMapperDecorator implements FactoryMapper {

    @Autowired private FactoryMapper delegate;

    @Override
    public Factory toFactory(FactoryBO factoryBO) {
        if (null == factoryBO) {
            return null;
        }
        Factory factory = delegate.toFactory(factoryBO);

        var brandFactory = delegate.toBrandFactory(factoryBO);
        return factory.addBrandEntity(brandFactory);
    }
}
