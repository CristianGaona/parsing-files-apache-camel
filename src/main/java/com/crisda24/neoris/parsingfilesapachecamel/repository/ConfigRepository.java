package com.crisda24.neoris.parsingfilesapachecamel.repository;

import com.crisda24.neoris.parsingfilesapachecamel.models.Config;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ConfigRepository extends ReactiveCrudRepository<Config, Long> {}
