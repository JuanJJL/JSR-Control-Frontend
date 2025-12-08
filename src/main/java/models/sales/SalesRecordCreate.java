package models.sales;

public class SalesRecordCreate {
    private int id_client;
    private int id_product;
    private int quantity;
    private int id_payment_method;

    // Constructor
    public SalesRecordCreate(int id_client, int id_product, int quantity, int id_payment_method) {
        this.id_client = id_client;
        this.id_product = id_product;
        this.quantity = quantity;
        this.id_payment_method = id_payment_method;
    }

    // Getters y Setters
    public int getId_client() { return id_client; }
    public void setId_client(int id_client) { this.id_client = id_client; }

    public int getId_product() { return id_product; }
    public void setId_product(int id_product) { this.id_product = id_product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getId_payment_method() { return id_payment_method; }
    public void setId_payment_method(int id_payment_method) { this.id_payment_method = id_payment_method; }
}