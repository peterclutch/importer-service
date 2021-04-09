package com.peter.importerservice.repository;

import com.peter.importerservice.domain.BrandFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandFactoryRepository extends JpaRepository<BrandFactory, BrandFactory.BrandEntityKey> {
}
