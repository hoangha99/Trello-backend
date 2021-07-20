package com.blameo.trello.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @Column(name = "create_by")
    private Long createBy;

    @Column(name = "display_order")
    private Long disPlayOrder;

    @Column(name = "complete")
    private Boolean complete = false;

    @Column(name = "person_work_id")
    private Long personWorkId;

    @Column(name = "deadline")
    private Date deadline;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "work_list_id")
    private WorkList workList;

}
