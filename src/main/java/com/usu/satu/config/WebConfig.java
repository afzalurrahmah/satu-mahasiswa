package com.usu.satu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${folder.upload}")
    String uploadFolder;

    @Value("${folder.logs}")
    String logFolder;

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        Path uploadDir      = Paths.get(uploadFolder);
        String uploadPath   = uploadDir.toFile().getAbsolutePath();

        Path logDir         = Paths.get(logFolder);
        String logPath      = logDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/files/**").addResourceLocations("file:///"+uploadPath+"/");
        registry.addResourceHandler("/logs/**").addResourceLocations("file:///"+logPath+"/");

    }
}
