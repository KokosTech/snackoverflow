/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.UUID;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

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
    private Date lastModified;

    @Column(nullable = false)
    private Date validFrom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = true)
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> image;

    @ToString.Exclude
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Audited(targetAuditMode = NOT_AUDITED)
    @NotAudited
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment;

    @ToString.Exclude
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Audited(targetAuditMode = NOT_AUDITED)
    @NotAudited
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Saved> saved;

    @ToString.Exclude
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Audited(targetAuditMode = NOT_AUDITED)
    @NotAudited
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rated> rated;

    @PrePersist
    protected void onCreate() {
        createdOn = Calendar.getInstance();
        lastModified = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = new Date();
    }
}
