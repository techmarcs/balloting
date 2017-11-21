/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.service;

import com.example.model.Ballot;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author josephmarcus
 */
@Service
public class BallotService {
    
    Logger logger = Logger.getLogger(this.getClass());
    
    private final String IN_FILE = "ballot_months.txt";
    private final String OUT_FILE = "ballot_names.txt";
    List<String> months = new ArrayList<>();
    Map<String, String> out = new HashMap<>();
    Map<String, String> emailMap = new HashMap<>();
    Random r = new Random();
    
    //@PostConstruct
    private void init() throws FileNotFoundException, IOException{
        Resource resource = new ClassPathResource(OUT_FILE);
        try(BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))){
           String line = br.readLine();
            while (line != null && !line.isEmpty()) {
                out.put(line.split("|")[0], line);
                line = br.readLine();
            }
        }
        
        resource = new ClassPathResource(IN_FILE);
        try(BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))){
        
           String line = br.readLine();
           int count = 1;
            while (line != null && !line.isEmpty()) {
                if(out.get(line) == null){
                    months.add(line);
                    line = br.readLine();
                    count++;
                }
            }
        }
    }
    
    public Ballot doBallot(Ballot ballot) throws IOException{
        if(ballot != null && !emailMap.containsKey(ballot.getEmail())){
                if(months.size() > 0){
                    
                    int guess = r.nextInt(months.size()); 
                    ballot.setMonth(months.get(guess));
                    emailMap.put(ballot.getEmail(), ballot.getMonth());
                    
                     try(BufferedWriter bw = new BufferedWriter(new FileWriter(OUT_FILE))){
                        bw.append(ballot.getMonth() + "|" + ballot.getEmail());
                     }
                     logger.info(String.format("%s|%s", ballot.getEmail(), ballot.getMonth()));

                    months.remove(guess);
            }
        }
        return ballot;
    }
}
