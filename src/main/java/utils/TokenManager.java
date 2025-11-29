package utils;

public class TokenManager {
    private static String token = null;
    private static int user_id = -1;
    private static String username = null;
    private static int role_id = -1;

   public static void setToken(String new_token){
       token = new_token;
   }

   public static String getToken(){
       return token;
   }

   public static void setUserInfo(int id, String user, int role){
       user_id=id;
       username = user;
       role_id = role;
   }

    public static int getUserId() {
        return user_id;
    }

    public static String getUsername() {
        return username;
    }

    public static int getRoleId() {
        return role_id;
    }

    public static boolean isLoggedIn() {
        return token != null && !token.isEmpty();
    }

    public static void clearSession() {
        token = null;
        user_id = -1;
        username = null;
        role_id = -1;
    }

}
