package com.trello.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private Long workListId;
    private String title;
    private Long removeId;
    private Long removedIndex;
    private Long addId;
    private Long addedIndex;

    private Long taskId;
    private Long userId;

    private String urlImage;
}
