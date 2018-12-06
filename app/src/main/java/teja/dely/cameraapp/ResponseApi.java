package teja.dely.cameraapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELY on 10/24/2018.
 */

public class ResponseApi {
    @SerializedName("hasil")
    String hasil;

    @SerializedName("msg")
    String msg;

    public String getHasil() {

        return hasil;
    }

    public String getMsg(){
        return msg;
    }
}
