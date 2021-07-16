package com.blameo.trello.model;

import lombok.Data;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "category_wtd")
@Data
public class CategoryWTD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_wtd_id")
    private Integer categoryWTDID;

    @Column(name = "title")
    private String title;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "created_by")
    private int createBy;

    @Column(name = "is_hide")
    private int isHide;


//    @ManyToOne
//    @JoinColumn(name = "work_to_do_id")
//    private WorkTodo workTodo;

//    @OneToMany( cascade = CascadeType.ALL)
//    @JoinColumn(name = "category_wtd_id", referencedColumnName = "category_wtd_id")
//    private List<UserWorkToDo> userWorkToDos;
}

