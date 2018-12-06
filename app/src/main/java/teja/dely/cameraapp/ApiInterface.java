package teja.dely.cameraapp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by DELY on 10/24/2018.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("/sendImg/")
    Call<ResponseApi> kirim (@Field("idUser") String idUser ,
                             @Field("password") String password,
                             @Field("image") String image);

    @FormUrlEncoded
    @POST("/doTrain/")
    Call<ResponseApi> train (@Field("idUser") String idUser,
                             @Field("password") String password);

    @FormUrlEncoded
    @POST("/doPredict/")
    Call<ResponseApi> predict (@Field("idUser") String idUser ,
                               @Field("password") String password,
                               @Field("image") String image);

    @FormUrlEncoded
    @POST("/signin/")
    Call<ResponseApi> doAbsen (@Field("idUser") String idUser,
                              @Field("password") String password,
                              @Field("image") String image,
                              @Field("Lat") String latitude,
                              @Field("Lon") String longitude,
                              @Field("idAgenda") String idAgenda);
}
