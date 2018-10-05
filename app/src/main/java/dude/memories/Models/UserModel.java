package dude.memories.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserModel {
    @SerializedName("id")
    String id;
    @SerializedName("username")
    String username;
    @SerializedName("email")
    String email;
    @SerializedName("phone")
    String phone;
    @SerializedName("dateOfBirth")
    Date dateOfBirth;

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
