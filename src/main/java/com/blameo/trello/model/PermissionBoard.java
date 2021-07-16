package com.blameo.trello.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permisstion_board")
public class PermissionBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="permission_board_id")
    private Long permissionBoardID;

    @Column(name = "description")
    private  String description;

    @Column(name = "name")
    private String name;

    @Column(name = "is_hide")
    private Boolean isHide;

    @Column(name = "created_by")
    private int createBy;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "permission_board_id", referencedColumnName = "permission_board_id")
    private List<Board> boards;
}
