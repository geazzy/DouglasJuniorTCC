/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.model.miner;

import br.edu.utfpr.cm.JGitMinerWeb.model.InterfaceEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Douglas
 */
@Entity
@Table(name = "gitMilestone",uniqueConstraints = {
    @UniqueConstraint(columnNames = {"repository_id", "number"})
}, indexes = {
    @Index(columnList = "repository_id,number", unique = true),
    @Index(columnList = "url", unique = true),
    @Index(columnList = "creator_id"),
    @Index(columnList = "repository_id"),
    @Index(columnList = "createdAt")
})
@NamedQueries({
    @NamedQuery(name = "Milestone.findByURL", query = "SELECT m FROM EntityMilestone m WHERE m.url = :url"),
    @NamedQuery(name = "Milestone.findByID", query = "SELECT m FROM EntityMilestone m WHERE m.id = :id")
})
public class EntityMilestone implements InterfaceEntity, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date mineredAt;
    @Column(name = "createdAt")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dueOn;
    private Integer closedIssues;
    @Column(name = "number")
    private Integer number;
    private Integer openIssues;
    @Column(columnDefinition = "text")
    private String description;
    private String stateMilestone;
    @Column(columnDefinition = "text")
    private String title;
    @Column(unique = true, name = "url")
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EntityUser creator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id")
    private EntityRepository repository;

    public EntityMilestone() {
        mineredAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getMineredAt() {
        return mineredAt;
    }

    public void setMineredAt(Date mineredAt) {
        this.mineredAt = mineredAt;
    }

    public Integer getClosedIssues() {
        return closedIssues;
    }

    public void setClosedIssues(Integer closedIssues) {
        this.closedIssues = closedIssues;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public EntityUser getCreator() {
        return creator;
    }

    public void setCreator(EntityUser creator) {
        this.creator = creator;
    }

    public EntityRepository getRepository() {
        return repository;
    }

    public void setRepository(EntityRepository repository) {
        this.repository = repository;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueOn() {
        return dueOn;
    }

    public void setDueOn(Date dueOn) {
        this.dueOn = dueOn;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(Integer openIssues) {
        this.openIssues = openIssues;
    }

    public String getStateMilestone() {
        return stateMilestone;
    }

    public void setStateMilestone(String stateMilestone) {
        this.stateMilestone = stateMilestone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EntityMilestone)) {
            return false;
        }
        EntityMilestone other = (EntityMilestone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.cm.JGitMiner.pojo.EntityMilestone[ id=" + id + " ]";
    }
}
