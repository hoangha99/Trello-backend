package com.blameo.trello.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attch")
public class Attach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attach_id")
    private Integer attachID;

    @Column(name = "link")
    private String link;

    @Column(name = "is_hide")
    private int isHide;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "created_by")
    private int createBy;
//
//    @ManyToOne
//    @JoinColumn(name = "task_id")
//    private Task taskAttach;
}
