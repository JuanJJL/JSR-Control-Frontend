package models.products;


public class ProductUpdate {
    private String name;
    private Integer price;
    private Integer cost;
    private Integer stock;
    private Integer category_id;

    // Constructor vacío
    public ProductUpdate() {}

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public Integer getCost() { return cost; }
    public void setCost(Integer cost) { this.cost = cost; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getCategory_id() { return category_id; }
    public void setCategory_id(Integer category_id) { this.category_id = category_id; }
}