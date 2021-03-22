package ru.javaops.masterjava.model;

import java.util.Objects;

public class User {
    private final int id;
    private final String name;
    private final String email;
    private final UserFlag userFlag;

    public User(String fullName, String email,UserFlag flag){
        this( Integer.parseInt(null),fullName,email,flag);
    }

    public User(int id, String name, String email, UserFlag userFlag) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userFlag = userFlag;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserFlag getUserFlag() {
        return userFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                name.equals(user.name) &&
                email.equals(user.email) &&
                userFlag == user.userFlag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, userFlag);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userFlag=" + userFlag +
                '}';
    }
}
