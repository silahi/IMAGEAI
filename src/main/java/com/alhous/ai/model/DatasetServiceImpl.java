package com.alhous.ai.model;

import java.io.File;

import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasetServiceImpl implements IDatasetService {
    private final File BASE_DIR = new File("C:/datasets/data/images");
    private int counter1 = 0, counter2 = 0;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public String saveCategory(String dataset_name, Category category) { 
        category.getImages().forEach(img -> {
            File f1 = new File(BASE_DIR.getAbsolutePath(), dataset_name);
            File f2 = new File(f1.getAbsolutePath(), category.getName());
            File file = new File(f2.getAbsolutePath(), (f2.listFiles().length + 1) + ".jpg");
            Imgcodecs.imwrite(file.getAbsolutePath(), img);
            LOG.info(file.toPath().toString());
        });

        return (counter1 == category.getImages().size())
                ? String.format("{} images saved in category {}", category.getImages().size(), category.getName())
                : String.format("Failed to save {} in {}", category.getName(), dataset_name);

    }

    @Override
    public String saveDataset(Dataset dataset) {
        counter2 = 0;
        if (!BASE_DIR.exists())
            BASE_DIR.mkdirs();

        File f1 = new File(BASE_DIR.getAbsolutePath(), dataset.getName());
        f1.mkdirs();
        dataset.getCategories().stream().forEach(cat -> {
            counter2++;
            File f2 = new File(f1.getAbsolutePath(), cat.getName());
            if (f2.exists())
                saveCategory(dataset.getName(), cat);
            else {
                f2.mkdirs();
                saveCategory(dataset.getName(), cat);
            }
        });
        String msg = (counter2 == dataset.getCategories().size())
                ? "Dataset " + dataset.getName() + " saved successfully !"
                : "Failed to save dataset " + dataset.getName() + " !";
        return msg;
    }

}
