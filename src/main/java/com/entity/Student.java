package com.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Student {
    private Integer id;
    private String name;
    private String major;
    private Integer age;
    private Date enter_time;
    private Short is_graduated; // 修改：Integer → Short
    private BigDecimal tuition;

    public Student() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getEnter_time() {
        return enter_time;
    }

    public void setEnter_time(Date enter_time) {
        this.enter_time = enter_time;
    }

    public Short getIs_graduated() {
        return is_graduated;
    }

    public void setIs_graduated(Short is_graduated) {
        this.is_graduated = is_graduated;
    }

    public BigDecimal getTuition() {
        return tuition;
    }

    public void setTuition(BigDecimal tuition) {
        this.tuition = tuition;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", major='" + major + '\'' +
                ", age=" + age +
                ", enter_time=" + enter_time +
                ", is_graduated=" + is_graduated +
                ", tuition=" + tuition +
                '}';
    }
}
