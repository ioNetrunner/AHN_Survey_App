/*
 * Copyright 2017 - Allegheny Health Network
 * @author Roy Gustafson <roy.gustafson@ahn.org> <royagustafson@gmail.com>
 */
package org.ahn.recserver.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author rgustafs
 */
@Configuration
@EnableWebMvc
@ComponentScan({ "org.ahn.recserver.controllers" })
public class ResourceServerWebConfig extends WebMvcConfigurerAdapter {
    // TODO depending on how site is hosted, may need to add host site
    
    /*
    @Override
    public void addCorsMappngs(CorsRegistry registry) {
        registry.addMapping("");
    }
    */
}
