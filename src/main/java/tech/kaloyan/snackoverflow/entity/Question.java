/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.validation.constraints.Null;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Data
@Table
@Audited
public class Question {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(nullable = false)
    @Lob
    private String description;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Calendar createdOn;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date validFrom;

    @PrePersist
    protected void onCreate() {
        createdOn = Calendar.getInstance();
        lastModified = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = new Date();
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = true)
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> image;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Saved> saved;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rated> rated;
}
