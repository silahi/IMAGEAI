package com.alhous.ai.config;

import com.alhous.ai.model.Camera;
import com.alhous.ai.model.Category;
import com.alhous.ai.model.Dataset;
import com.alhous.ai.model.DatasetServiceImpl;
import com.alhous.ai.model.IDatasetService;
import com.alhous.ai.model.LiveServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BeanDefinition {
    @Bean
    @Scope("prototype")
    public Category category() {
        return new Category();
    }

    @Bean
    @Scope("prototype")
    public Dataset dataset() {
        return new Dataset();
    }

    @Bean
    public Camera camera() {
        return new Camera();
    }

    @Bean
    public IDatasetService datasetService() {
        return new DatasetServiceImpl();
    }

    @Bean
    public LiveServiceImpl liveServiceImpl() {
        return new LiveServiceImpl();
    }
}
