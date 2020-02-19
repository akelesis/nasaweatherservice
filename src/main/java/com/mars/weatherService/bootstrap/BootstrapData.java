package com.mars.weatherService.bootstrap;

import com.mars.weatherService.repositories.SolsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final SolsRepository solsRepository;

    public BootstrapData(SolsRepository solsRepository) {
        this.solsRepository = solsRepository;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
