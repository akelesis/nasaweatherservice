package com.mars.weatherService.services;

import com.mars.weatherService.domain.Sol;

import java.util.List;

public interface WeatherService {
    Sol findSolById(Long id);
    List<Sol> findAllSols();

    Sol saveSol(Sol sol);

}
