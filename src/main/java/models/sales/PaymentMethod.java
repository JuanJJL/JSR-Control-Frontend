package models.sales;


public class PaymentMethod {
    private int id;
    private String payment_method;
    private String created_at;
    private String updated_at;

    // Constructor vacío
    public PaymentMethod() {}

    // Constructor completo
    public PaymentMethod(int id, String payment_method, String created_at, String updated_at) {
        this.id = id;
        this.payment_method = payment_method;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPayment_method() { return payment_method; }
    public void setPayment_method(String payment_method) { this.payment_method = payment_method; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }

    @Override
    public String toString() {
        return payment_method;  // Esto se muestra en el ComboBox
    }
}