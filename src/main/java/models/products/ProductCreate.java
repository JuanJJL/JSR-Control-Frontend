package models.products;

public class ProductCreate {
    private String name;
    private int price;
    private int cost;
    private int stock;
    private int category_id;

    // Constructor
    public ProductCreate(String name, int price, int cost, int stock, int category_id) {
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.stock = stock;
        this.category_id = category_id;
    }

    // Getters y Setters
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
}