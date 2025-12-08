package models.expenses;

public class ExpenseRecordCreate {
    private int supervisor_id;
    private int responsible_id;
    private int category_id;
    private int payment_method_id;
    private String description;
    private double cost;
    private int is_reviewed;

    public ExpenseRecordCreate(int supervisor_id, int responsible_id, int category_id, int payment_method_id,
            String description, double cost, int is_reviewed) {
        this.supervisor_id = supervisor_id;
        this.responsible_id = responsible_id;
        this.category_id = category_id;
        this.payment_method_id = payment_method_id;
        this.description = description;
        this.cost = cost;
        this.is_reviewed = is_reviewed;
    }

    public int getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id(int supervisor_id) {
        this.supervisor_id = supervisor_id;
    }

    public int getResponsible_id() {
        return responsible_id;
    }

    public void setResponsible_id(int responsible_id) {
        this.responsible_id = responsible_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(int payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getIs_reviewed() {
        return is_reviewed;
    }

    public void setIs_reviewed(int is_reviewed) {
        this.is_reviewed = is_reviewed;
    }
}
