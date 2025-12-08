package services;

import models.LoginRequest;
import models.TokenResponse;
import models.user.User;
import models.user.UserUpdate;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;
import models.user.UserCreate;
import models.products.Product;
import models.products.ProductCategory;
import models.products.ProductCategoryCreate;
import models.products.ProductCreate;
import models.products.ProductUpdate;
import models.clients.Client;
import models.clients.ClientCreate;
import models.clients.ClientUpdate;
import models.sales.*;
import models.expenses.*;

public interface ApiService {

        @POST("auth/login")
        Call<TokenResponse> login(@Body LoginRequest credentials);

        @GET("users")
        Call<List<User>> getUsers(@Header("Authorization") String token);

        @POST("users/create")
        Call<Void> createUser(
                        @Header("Authorization") String token,
                        @Body UserCreate user);

        @DELETE("users/delete/{user_id}")
        Call<Void> deleteUser(
                        @Header("Authorization") String token,
                        @Path("user_id") int user_id);

        @Headers("Content-Type: application/json")
        @PUT("users/update/{user_id}")
        Call<Void> updateUser(
                        @Path("user_id") int userId,
                        @Body UserUpdate data);

        // ========== PRODUCTS ==========

        @Headers("Content-Type: application/json")
        @GET("products/")
        Call<List<Product>> getAllProducts(@Header("Authorization") String token);

        @Headers("Content-Type: application/json")
        @GET("products/{product_id}")
        Call<Product> getProductById(
                        @Header("Authorization") String token,
                        @Path("product_id") int productId);

        @Headers("Content-Type: application/json")
        @POST("products/create")
        Call<Product> createProduct(
                        @Header("Authorization") String token,
                        @Body ProductCreate data);

        @Headers("Content-Type: application/json")
        @PUT("products/update/{product_id}")
        Call<Product> updateProduct(
                        @Header("Authorization") String token,
                        @Path("product_id") int productId,
                        @Body ProductUpdate data);

        @DELETE("products/delete/{product_id}")
        Call<Void> deleteProduct(
                        @Header("Authorization") String token,
                        @Path("product_id") int productId);

        // ========== PRODUCT CATEGORIES ==========

        @GET("products/categories")
        Call<List<ProductCategory>> getAllCategories(@Header("Authorization") String token);

        @GET("products/categories/{category_id}")
        Call<ProductCategory> getCategoryById(
                        @Header("Authorization") String token,
                        @Path("category_id") int categoryId);

        @Headers("Content-Type: application/json")
        @POST("products/categories/create")
        Call<ProductCategory> createCategory(
                        @Header("Authorization") String token,
                        @Body ProductCategoryCreate data);

        @Headers("Content-Type: application/json")
        @PUT("products/categories/update/{category_id}")
        Call<ProductCategory> updateCategory(
                        @Header("Authorization") String token,
                        @Path("category_id") int categoryId,
                        @Body ProductCategoryCreate data);

        @DELETE("products/categories/delete/{category_id}")
        Call<Void> deleteCategory(
                        @Header("Authorization") String token,
                        @Path("category_id") int categoryId);

        // ========== CLIENTS ==========

        @Headers("Content-Type: application/json")
        @GET("clients/")
        Call<List<Client>> getAllClients(@Header("Authorization") String token);

        @Headers("Content-Type: application/json")
        @GET("clients/{client_id}")
        Call<Client> getClientById(
                        @Header("Authorization") String token,
                        @Path("client_id") int clientId);

        @Headers("Content-Type: application/json")
        @POST("clients/create")
        Call<Client> createClient(
                        @Header("Authorization") String token,
                        @Body ClientCreate data);

        @Headers("Content-Type: application/json")
        @PUT("clients/update/{client_id}")
        Call<Client> updateClient(
                        @Header("Authorization") String token,
                        @Path("client_id") int clientId,
                        @Body ClientUpdate data);

        @DELETE("clients/delete/{client_id}")
        Call<Void> deleteClient(
                        @Header("Authorization") String token,
                        @Path("client_id") int clientId);

        // ========== SALES RECORDS ==========

        @Headers("Content-Type: application/json")
        @GET("sales/")
        Call<List<SalesRecord>> getAllSales(@Header("Authorization") String token);

        @Headers("Content-Type: application/json")
        @GET("sales/{sale_id}")
        Call<SalesRecord> getSaleById(
                        @Header("Authorization") String token,
                        @Path("sale_id") int saleId);

        @Headers("Content-Type: application/json")
        @POST("sales/create")
        Call<SalesRecord> createSale(
                        @Header("Authorization") String token,
                        @Body SalesRecordCreate data);

        @DELETE("sales/delete/{sale_id}")
        Call<Void> deleteSale(
                        @Header("Authorization") String token,
                        @Path("sale_id") int saleId);

        @Headers("Content-Type: application/json")
        @GET("sales/client/{client_id}")
        Call<List<SalesRecord>> getSalesByClient(
                        @Header("Authorization") String token,
                        @Path("client_id") int clientId);

        @Headers("Content-Type: application/json")
        @GET("sales/product/{product_id}")
        Call<List<SalesRecord>> getSalesByProduct(
                        @Header("Authorization") String token,
                        @Path("product_id") int productId);

        // ========== PAYMENT METHODS ==========

        @Headers("Content-Type: application/json")
        @GET("payment_methods/")
        Call<List<PaymentMethod>> getAllPaymentMethods(@Header("Authorization") String token);

        // ========== EXPENSE CATEGORIES ==========

        @Headers("Content-Type: application/json")
        @GET("expense_categories/")
        Call<List<ExpenseCategory>> getAllExpenseCategories(@Header("Authorization") String token);

        @Headers("Content-Type: application/json")
        @GET("expense_categories/{category_id}")
        Call<ExpenseCategory> getExpenseCategoryById(
                        @Header("Authorization") String token,
                        @Path("category_id") int categoryId);

        @Headers("Content-Type: application/json")
        @POST("expense_categories/create")
        Call<ExpenseCategory> createExpenseCategory(
                        @Header("Authorization") String token,
                        @Body ExpenseCategoryCreate data);

        @Headers("Content-Type: application/json")
        @PUT("expense_categories/update/{category_id}")
        Call<ExpenseCategory> updateExpenseCategory(
                        @Header("Authorization") String token,
                        @Path("category_id") int categoryId,
                        @Body ExpenseCategoryCreate data);

        @DELETE("expense_categories/delete/{category_id}")
        Call<Void> deleteExpenseCategory(
                        @Header("Authorization") String token,
                        @Path("category_id") int categoryId);

        // ========== EXPENSE RECORDS ==========

        @Headers("Content-Type: application/json")
        @GET("expense_records/")
        Call<List<ExpenseRecord>> getAllExpenseRecords(@Header("Authorization") String token);

        @Headers("Content-Type: application/json")
        @GET("expense_records/{record_id}")
        Call<ExpenseRecord> getExpenseRecordById(
                        @Header("Authorization") String token,
                        @Path("record_id") int recordId);

        @Headers("Content-Type: application/json")
        @POST("expense_records/create")
        Call<ExpenseRecord> createExpenseRecord(
                        @Header("Authorization") String token,
                        @Body ExpenseRecordCreate data);

        @Headers("Content-Type: application/json")
        @PUT("expense_records/update/{record_id}")
        Call<ExpenseRecord> updateExpenseRecord(
                        @Header("Authorization") String token,
                        @Path("record_id") int recordId,
                        @Body ExpenseRecordCreate data);

        @DELETE("expense_records/delete/{record_id}")
        Call<Void> deleteExpenseRecord(
                        @Header("Authorization") String token,
                        @Path("record_id") int recordId);

}
