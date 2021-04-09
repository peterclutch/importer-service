package com.peter.importerservice.service;

import com.peter.importerservice.domain.Factory;
import com.peter.importerservice.repository.BrandRepository;
import com.peter.importerservice.repository.FactoryRepository;
import com.peter.importerservice.service.bo.FactoryBO;
import com.peter.importerservice.service.mapper.FactoryMapper;
import com.peter.importerservice.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class FactoryService {

    private final FactoryMapper factoryMapper;

    private final BrandRepository brandRepository;
    private final FactoryRepository factoryRepository;

    public FactoryBO create(final FactoryBO factoryBO, Boolean dryRun) {
        if (!dryRun) {
//            BusinessEvent.builder()
//                    .elementType(BusinessEventElementType.FACTORY)
//                    .actionType(BusinessEventActionType.CREATE)
//                    .comment("Create a new factory")
//                    .publish();
        }
        final Factory factoryToSave = factoryMapper.toFactory(factoryBO);

        var brandId = SecurityUtils.getCurrentUserBrandWhenAuthorized();

        factoryToSave
                .getBrandFactories()
                .forEach(
                        brandFactory ->
                                brandFactory.getId().brand(brandRepository.getOne(brandId)).factory(factoryToSave));
//        factoryToSave.setType(factoryTypeRepository.getOne(factoryBO.getFactoryTypeId()));
        final Factory factorySaved = factoryRepository.save(factoryToSave);
        factoryRepository.save(factoryToSave);
//        manageHierarchy(
//                factoryToSave, factoryBO.getChildren(), factoryBO.getParents(), factoryBO.getChildOfBrand());

        if (!dryRun) {
//            BusinessEvent.builder()
//                    .elementType(BusinessEventElementType.FACTORY)
//                    .elementId(factorySaved.getId())
//                    .actionType(BusinessEventActionType.CREATE)
//                    .comment(String.format("Factory %d is created", factorySaved.getId()))
//                    .publish();
        }

        factoryBO.setId(factorySaved.getId());

//        if (factoryBO.hasRegistrationNumber()) {
//            registrationNumberService.save(factoryBO.getRegistrationNumber(), factorySaved, dryRun);
//        }
        final FactoryBO result = factoryMapper.toBO(factorySaved);
//        result.setRegistrationNumber(factoryBO.getRegistrationNumber());
        return result;
    }
}
