package models.sales;

public class SalesRecord {
    private int id;
    private int id_client;
    private int id_product;
    private int quantity;
    private int unit_price;
    private int total_amount;
    private int profit;
    private int id_payment_method;
    private String created_at;
    private String updated_at;

    // Constructor vacío
    public SalesRecord() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getId_client() { return id_client; }
    public void setId_client(int id_client) { this.id_client = id_client; }

    public int getId_product() { return id_product; }
    public void setId_product(int id_product) { this.id_product = id_product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getUnit_price() { return unit_price; }
    public void setUnit_price(int unit_price) { this.unit_price = unit_price; }

    public int getTotal_amount() { return total_amount; }
    public void setTotal_amount(int total_amount) { this.total_amount = total_amount; }

    public int getProfit() { return profit; }
    public void setProfit(int profit) { this.profit = profit; }

    public int getId_payment_method() { return id_payment_method; }
    public void setId_payment_method(int id_payment_method) { this.id_payment_method = id_payment_method; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }

    @Override
    public String toString() {
        return "SalesRecord{id=" + id + ", client=" + id_client + ", product=" + id_product + 
               ", quantity=" + quantity + ", total=" + total_amount + "}";
    }
}