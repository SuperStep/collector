package com.sust.collector.repository;

import com.sust.collector.entity.CollectorConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectorConfigurationRepository extends JpaRepository<CollectorConfiguration, Long> {
}