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
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @Column(name = "create_By")
    private Long createBy;

    @Column(name = "is_hide")
    private Boolean isHide = false;

    @ManyToMany(mappedBy = "boards")
    private Set<User> users;


    @OneToMany(mappedBy = "board")
    private Set<WorkList> workLists;

    public void add(User user){
        users = new HashSet<>();
        users.add(user);
    }

}
