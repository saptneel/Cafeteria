package com.saptneel.cafeteria;

public class User {
    private String username, profilePic, email;

    public User() {
    }

    public User(String username, String profilePic, String email) {
        this.username = username;
        this.profilePic = profilePic;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
