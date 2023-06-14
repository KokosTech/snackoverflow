/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table
@Audited
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private java.util.Calendar createdOn;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    @PrePersist
    protected void onCreate() {
        createdOn = java.util.Calendar.getInstance();
        lastModified = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = new Date();
    }

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> reply;
}
