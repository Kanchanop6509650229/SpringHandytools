/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dev.kanchanop.handytools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author dlwlrma
 */
@Configuration
public class LoadDatabase {
    
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    public CommandLineRunner setupData(StorageRepository storageRepository) {
        return (args) -> {
            log.info("Preloading " + storageRepository.save(new Storage("Hammer", "John", "Garage", false, "")));
            log.info("Preloading " + storageRepository.save(new Storage("Screwdriver", "Jane", "Shed", true, "John")));
            log.info("Preloading " + storageRepository.save(new Storage("Wrench", "Jill", "Basement", false, "")));
        };
    }
}
