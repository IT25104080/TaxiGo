package com.taxi.taxibookingplatform.model;

public class UserView {

    private final String userId;
    private final String name;
    private final String email;
    private final String phone;
    private final String userType;

    public UserView(String userId, String name, String email, String phone, String userType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
    }

    public static UserView from(User user) {
        String type = user instanceof PremiumPassenger ? "premium" : "regular";
        return new UserView(user.getUserId(), user.getName(), user.getEmail(), user.getPhone(), type);
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getUserType() { return userType; }
}
