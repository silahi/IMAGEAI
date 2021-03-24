package com.alhous.ai.model;

public interface IDatasetService {
    /**
     * 
     * @param dataset_name dataset name
     * @param category     the category
     * @see Category
     * @return a message indicating the state of save operation
     */
    public String saveCategory(String dataset_name, Category category);

    /**
     * 
     * @param dataset the dataset to save
     * @see Dataset
     * @return a message indicating the state of save operation
     */
    public String saveDataset(Dataset dataset);
}
