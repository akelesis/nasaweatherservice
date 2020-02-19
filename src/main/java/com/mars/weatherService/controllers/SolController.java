package com.mars.weatherService.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mars.weatherService.domain.Sol;
import com.mars.weatherService.services.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.web.bind.annotation.*;

import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(SolController.BASE_URL)
public class SolController {
    public static final String BASE_URL = "api/sols";
    private final WeatherService weatherService;

    public SolController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    List<Sol> getAllSols(){
        return weatherService.findAllSols();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public Sol getSolById(@PathVariable Long id){
        return weatherService.findSolById(id);
    }

    @Scheduled(fixedRate = 600000)
    @PostMapping
    public void getSols() throws IOException {
        String link = "https://api.nasa.gov/insight_weather/?api_key=DEMO_KEY&feedtype=json&ver=1.0";

        List<Sol> allSols = getAllSols();
        LocalDate recordsDate;
        if(allSols.size() > 1){
            recordsDate = allSols.get(1).getLoadDate();
        }
        else{
            recordsDate = null;
        }

        if(LocalDate.now().equals(recordsDate)){
           return;
        }
        else{
            URL url = new URL(link);
            URLConnection request = url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = null; //from gson
            JsonElement root = jp.parseReader(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
            JsonArray sols = rootObj.getAsJsonObject("validity_checks").getAsJsonArray("sols_checked");

            for(int i = 0; i < sols.size() - 1; i++){
                String parameter = sols.get(i).getAsString();
                JsonObject auxObj = rootObj.getAsJsonObject(parameter).getAsJsonObject("AT");
                Long id = Long.valueOf(i);
                Double avtemp = auxObj.get("av").getAsDouble();
                Double maxtemp = auxObj.get("mx").getAsDouble();
                Double mintemp = auxObj.get("mn").getAsDouble();
                LocalDate today = LocalDate.now();

                Sol sol = new Sol();

                sol.setSol(sols.get(i).getAsInt());
                sol.setId(id);
                sol.setAverageTemp(avtemp);
                sol.setMaxTemp(maxtemp);
                sol.setMinTemp(mintemp);
                sol.setLoadDate(today);

                saveSol(sol);

            }
        }

    }

    @ResponseStatus(HttpStatus.CREATED)
    public Sol saveSol(@RequestBody Sol sol){
        return weatherService.saveSol(sol);
    }

    @PutMapping(path = "/{id}")
    public Sol updateSol(@PathVariable Long id, @RequestBody Sol sol){
        sol.setId(id);
        return weatherService.saveSol(sol);
    }
}
