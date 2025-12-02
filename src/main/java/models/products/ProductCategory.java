package models.products;

public class ProductCategory {
    private int id;
    private String category;
    private String created_at;
    private String updated_at;

    // Constructor vacío
    public ProductCategory() {}

    // Constructor completo
    public ProductCategory(int id, String category, String created_at, String updated_at) {
        this.id = id;
        this.category = category;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }

    @Override
    public String toString() {
        return category;  // Esto se muestra en el ComboBox
    }
}