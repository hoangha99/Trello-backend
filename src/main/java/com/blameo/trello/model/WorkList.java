package com.blameo.trello.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_list")
public class WorkList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_list_id")
    private Integer workListId;

    @Column(name = "title")
    private String title;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "create_by")
    private int createBy;

    @Column(name = "display_order")
    private int displayOrder;

    @ManyToOne
    @JoinColumn(name ="board_id")
    private Board board;

    @OneToMany(mappedBy = "workList")
    private Set<Task> tasks;
}
