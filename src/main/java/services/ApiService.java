package services;
import models.LoginRequest;
import models.TokenResponse;
import models.User;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;


public interface ApiService {

    @POST("auth/login")
    Call<TokenResponse> login(@Body LoginRequest credentials);


    @GET("users")
    Call<User> getUsers(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("users/create")
    Call<Void> createUser(
            @Header("Authorization") String token,
            @Field("username") String username,
            @Field("password") String password,
            @Field("role_id") int role_id
    );

    @DELETE("users/delete/{user_id}")
    Call<Void>  deleteuser(
            @Header("Authorization") String token,
            @Path("user_id") int user_id
    );

    @FormUrlEncoded
    @PUT("user/update/{user_id}")
    Call<Void> updateUser(
            @Header("Authorization") String token,
            @Path("user_id") int user_id,
            @Field("username") String username,
            @Field("role_id") int role_id
    );



}
