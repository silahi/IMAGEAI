package com.alhous.ai.model;

import java.io.File;

import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasetServiceImpl implements IDatasetService {
    private final File BASE_DIR = new File("C:/datasets/data/images");
    private int counter = 0;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public String saveCategory(String dataset_name, Category category) {
        if (!BASE_DIR.exists())
            BASE_DIR.mkdirs();

        File f1 = new File(BASE_DIR.getAbsolutePath(), dataset_name);
        f1.mkdirs();

        File f2 = new File(f1.getAbsolutePath(), category.getName());
        if (f2.exists())
            return "This category exists yet, specify an other name !";

        counter = 0;
        category.getImages().stream().forEach(img -> {
            counter++;
            Imgcodecs.imwrite(new File(f2.getAbsolutePath(), counter + ".jpg").getAbsolutePath(), img);
        });

        return (counter == category.getImages().size())
                ? category.getName() + " category added in dataset " + dataset_name
                : "Failed to save the category " + category.getName() + " in dataset " + dataset_name;

    }

    @Override
    public String saveDataset(Dataset dataset) {
        counter = 0;
        dataset.getCategories().stream().forEach(cat -> {
            counter++;
            LOG.info(" Msg {} : {}", counter, saveCategory(dataset.getName(), cat));
        });
        String msg = (counter == dataset.getCategories().size())
                ? "Dataset " + dataset.getName() + " saved successfully !"
                : "Failed to save dataset " + dataset.getName() + " !";
        return msg;
    }

}
