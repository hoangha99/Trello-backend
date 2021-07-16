package com.blameo.trello.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_work_to_do")
@Data
public class UserWorkToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_work_to_do_id")
    private Integer Id;

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
//    @JoinColumn(name = "user_id")
//    private User userWorktodo;

//    @ManyToOne
//    @JoinColumn(name = "categorywtd_id")
//    private CategoryWTD categoryWTD;
}

