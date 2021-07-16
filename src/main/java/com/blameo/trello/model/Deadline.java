package com.blameo.trello.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "deadline")
@Data
public class Deadline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dead_line_id")
    private Long deadLineID;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "is_hide")
    private Boolean isHide;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "complete")
    private Boolean complete;


    @Column(name = "created_by")
    private Long createBy;

//    @ManyToOne
//    @JoinColumn(name = "task_id")
//    private Task taskDeadline;
}

