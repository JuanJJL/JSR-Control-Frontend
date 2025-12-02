package models.products;

public class Product {
    private int id;
    private String name;
    private int price;
    private int cost;
    private int stock;
    private int category_id;
    private String created_at;
    private String updated_at;

    // Constructor vacío
    public Product() {}

    // Constructor completo
    public Product(int id, String name, int price, int cost, int stock, int category_id, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.stock = stock;
        this.category_id = category_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getCategory_id() { return category_id; }
    public void setCategory_id(int category_id) { this.category_id = category_id; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + ", stock=" + stock + "}";
    }
}