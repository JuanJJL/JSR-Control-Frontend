package models.products;

public class ProductCategoryCreate {
    private String category;

    // Constructor
    public ProductCategoryCreate(String category) {
        this.category = category;
    }

    // Getters y Setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}