package com.mars.weatherService.services;

import com.mars.weatherService.domain.Sol;
import com.mars.weatherService.repositories.SolsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final SolsRepository solsRepository;

    public WeatherServiceImpl(SolsRepository solsRepository) {
        this.solsRepository = solsRepository;
    }

    @Override
    public Sol findSolById(Long id) {
        return solsRepository.findById(id).get();
    }

    @Override
    public List<Sol> findAllSols() {
        return solsRepository.findAll();
    }

    @Override
    public Sol saveSol(Sol sol) {
        return solsRepository.save(sol);
    }


}
