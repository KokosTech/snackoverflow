/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Data
@Table
@Audited
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 256)
    private String text;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "reply_id", nullable = true)
    @Audited(targetAuditMode = NOT_AUDITED)
    @NotAudited
    private Reply reply;

    @PrePersist
    protected void onCreate() {
        createdOn = new Date();
        lastModified = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = new Date();
    }
}
