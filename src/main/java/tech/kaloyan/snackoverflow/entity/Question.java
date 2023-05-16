/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Data
@Table
public class Question {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(nullable = false)
    @Lob
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdOn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> image;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Saved> saved;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rated> rated;
}
