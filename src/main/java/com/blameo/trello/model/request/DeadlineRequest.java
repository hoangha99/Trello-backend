package com.blameo.trello.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeadlineRequest {
    private Long taskId;
    private Date startDate;
    private Date endDate;
}
