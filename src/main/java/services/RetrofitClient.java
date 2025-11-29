package services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase singleton que configura y proporciona el cliente de Retrofit
 */
public class RetrofitClient {
    // URL base de tu backend FastAPI
    private static final String BASE_URL = "http://localhost:8000/";

    private static Retrofit retrofit = null;

    /**
     * Crea y retorna una instancia de Retrofit (solo una vez)
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Retorna una instancia de ApiService lista para usar
     */
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}