package com.blameo.trello.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "work_to_do")
@Data
public class WorkTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_to_do_id")
    private Integer workToDoId;

    @Column(name = "title")
    private String title;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "created_by")
    private int createBy;

    @Column(name = "is_hide")
    private int isHide;

    @Column(name = "complete")
    private int complete;

//    @ManyToOne
//    @JoinColumn(name = "task_id")
//    private Task taskWorkTodo;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "work_to_do_id",referencedColumnName = "work_to_do_id")
//    private List<CategoryWTD> categoryWTDS;
}

