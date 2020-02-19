package com.mars.weatherService.repositories;

import com.mars.weatherService.domain.Sol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolsRepository extends JpaRepository <Sol, Long> {

}
