package dude.memories.APIs;

import java.util.Date;

import dude.memories.Models.LoginResponseModel;
import dude.memories.Models.UserModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BaseApiService {


    @FormUrlEncoded
    @POST("users/")
    Call<UserModel> registerRequest(@Field("username") String username,
                                    @Field("password") String password,
                                    @Field("email") String email,
                                    @Field("phone") String phone,
                                    @Field("dateOfBirth") Date dateOfBirth);

    @FormUrlEncoded
    @POST("users/login")
    Call<LoginResponseModel> loginRequest(
            @Field("username") String username,
            @Field("password") String password);

    @GET("users/{pathID}")
    Call<UserModel> getInfo(@Path("pathID") String uID);

    @FormUrlEncoded
    @PUT("users/{pathID}")
    Call<UserModel> updateInfo(@Path("pathID") String uID,
                               @Field("username") String username,
                               @Field("password") String password,
                               @Field("email") String email,
                               @Field("phone") String phone,
                               @Field("dateOfBirth") Date dateOfBirth);

}
