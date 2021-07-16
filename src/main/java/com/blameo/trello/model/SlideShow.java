package com.blameo.trello.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "slide_show")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SlideShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slide_show_id")
    private Long slideShowId;

    @Column(name = "image_path")
    private String imagePath;

}

