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

    @Column(name = "label")
    private String m_label;

    @Column(name = "calories")
    private int m_calories;


    public FoodModel(String food, String label, int calories) {
        m_food = food;
        m_label = label;
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

    public String getLabel() {
        return m_label;
    }

    public void setGroup(String label) {
        m_label = label;
    }

    public int getCalories() {
        return m_calories;
    }

    public void setCalories(int calories) {
        m_calories = calories;
    }


}
