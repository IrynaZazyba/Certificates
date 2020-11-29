package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "tags")
    private List<Certificate> certificates;
    @Column(name = "operation")
    private String operation;
    @Column(name = "timestamp")
    private long timestamp;

    @PrePersist
    public void onPrePersist() {
        audit("INSERT");
    }

    @PreUpdate
    public void onPreUpdate() {
        audit("UPDATE");
    }

    @PreRemove
    public void onPreRemove() {
        audit("DELETE");
    }

    private void audit(String operation) {
        setOperation(operation);
        setTimestamp((new Date()).getTime());
    }

}
