package com.shibumi.research.orm.hibernate.model;


import javax.persistence.*;

@Entity
@Table(name="food")
public class FoodModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long m_id;

    @Column(name = "food")
    private String m_food;

    @Column(name = "group")
    private String m_group;

    @Column(name = "calories")
    private String m_calories;


    public FoodModel(String food, String group, String calories) {
        m_food = food;
        m_group = group;
        m_calories = calories;
    }

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    public String getFood() {
        return m_food;
    }

    public void setFood(String food) {
        m_food = food;
    }

    public String getGroup() {
        return m_group;
    }

    public void setGroup(String group) {
        m_group = group;
    }

    public String getCalories() {
        return m_calories;
    }

    public void setCalories(String calories) {
        m_calories = calories;
    }


}
