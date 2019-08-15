package com.louislopez.intercorpchallenge.data.entity;

import java.io.Serializable;

/**
 * Created by louislopez on 14,August,2019
 * MDP Consulting,
 * Peru, Lima.
 */
public class UserEntity implements Serializable {
    private String email = "";
    private String name = "";
    private String lastName = "";
    private String age = "";
    private String birthday = "";

    public UserEntity(String email, String name, String lastName, String age, String birthday) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age='" + age + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
