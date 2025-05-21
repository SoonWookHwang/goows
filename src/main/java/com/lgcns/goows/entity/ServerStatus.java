package com.lgcns.goows.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ServerStatus extends TimeStamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusId;

    private boolean status;

}
