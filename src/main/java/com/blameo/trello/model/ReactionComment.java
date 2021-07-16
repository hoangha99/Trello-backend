package com.blameo.trello.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reaction_comment")
public class ReactionComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="reaction_comment_id")
    private Long reactionCommentID;

    @Column(name = "image")
    private  String image;

    @Column(name = "description")
    private  String description;

    @Column(name = "name")
    private String name;

    @Column(name = "is_hide")
    private Boolean isHide;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "created_by")
    private Long createBy;
//
//    @ManyToOne
//    @JoinColumn(name = "comment_id")
//    private Comment commentReaction;

}
