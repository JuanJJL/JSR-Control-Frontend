package models.expenses;

public class ExpenseCategoryCreate {
    private String category;

    public ExpenseCategoryCreate(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
