package org.project.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;

@Entity
@Cacheable
public class POSRequest extends PanacheEntity{

    @Column(name = "original_string", nullable = false, columnDefinition="TEXT")
    private String originalString;

    @Column(name = "pos_string", nullable = false, columnDefinition="TEXT")
    private String posString;

    public String getPosString() {
        return posString;
    }

    public void setPosString(String posString) {
        this.posString = posString;
    }

    public String getOriginalString() {
        return originalString;
    }

    public void setOriginalString(String originalString) {
        this.originalString = originalString;
    }
}
