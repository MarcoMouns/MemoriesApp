package dude.memories.Models;

import com.google.gson.annotations.SerializedName;

public class LoginResponseModel {
    @SerializedName("id")
    String id;
    @SerializedName("uid")
    String uid;

    public LoginResponseModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        uid = uid;
    }
}
