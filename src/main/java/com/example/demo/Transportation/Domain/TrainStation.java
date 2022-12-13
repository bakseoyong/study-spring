package com.example.demo.Transportation.Domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class TrainStation {
    @Id
    private String nodeId;
    private String nodeName;

    public TrainStation(String nodeId, String nodeName) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
    }
}
