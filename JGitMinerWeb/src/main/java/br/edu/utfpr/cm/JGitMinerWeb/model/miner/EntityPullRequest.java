/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.model.miner;

import br.edu.utfpr.cm.JGitMinerWeb.model.InterfaceEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author Douglas
 */
@Entity
@Table(name = "gitPullRequest",uniqueConstraints = {
    @UniqueConstraint(columnNames = {"repository_id", "number"})
}, indexes = {
    @Index(columnList = "repository_id,number", unique = true),
    @Index(columnList = "idPullRequest", unique = true),
    @Index(columnList = "base_id"),
    @Index(columnList = "head_id"),
    @Index(columnList = "mergedby_id"),
    @Index(columnList = "repository_id"),
    @Index(columnList = "issue_id"),
    @Index(columnList = "user_id"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "number")
})
@NamedQueries({
    @NamedQuery(name = "PullRequest.findByIdPullRequest", query = "SELECT p FROM EntityPullRequest p WHERE p.idPullRequest = :idPullRequest"),
    @NamedQuery(name = "PullRequest.findById", query = "SELECT p FROM EntityPullRequest p WHERE p.id = :idPullRequest"),
    @NamedQuery(name = "PullRequest.findByNumberAndRepository", query = "SELECT p FROM EntityPullRequest p WHERE p.number = :number AND p.repository = :repository"),
    @NamedQuery(name = "PullRequest.findAllByRepository", query = "SELECT p FROM EntityPullRequest p WHERE p.repository = :repository"),
    @NamedQuery(name = "PullRequest.findByMilestoneAndRepository", query = "SELECT p FROM EntityPullRequest p WHERE p.repository = :repository and p.issue.milestone.number = :milestone"),
    @NamedQuery(name = "PullRequest.findByDateAndRepository", query = "SELECT p FROM EntityPullRequest p WHERE p.repository = :repository and p.issue.createdAt >= :dataInicial AND p.issue.createdAt <= :dataFinal"),
    @NamedQuery(name = "PullRequest.findByIssue", query = "SELECT p FROM EntityPullRequest p WHERE p.issue = :issue")
})
public class EntityPullRequest implements InterfaceEntity, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date mineredAt;
    private boolean mergeable;
    private boolean merged;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date closedAt;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date mergedAt;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;
    @Column(unique = true, name = "idPullRequest")
    private Long idPullRequest;
    private Integer additions;
    private Integer changedFiles;
    private Integer commentsCount;
    private Integer commitsCount;
    private Integer deletions;
    @Column(name = "number")
    private Integer number;
    @ManyToOne
    @JoinColumn(name = "base_id")
    private EntityPullRequestMarker base;
    @ManyToOne
    @JoinColumn(name = "head_id")
    private EntityPullRequestMarker head;
    @Column(columnDefinition = "text")
    private String body;
    @Column(columnDefinition = "text")
    private String bodyHtml;
    @Column(columnDefinition = "text")
    private String bodyText;
    @Column(columnDefinition = "text")
    private String diffUrl;
    @Column(columnDefinition = "text")
    private String htmlUrl;
    @Column(columnDefinition = "text")
    private String issueUrl;
    @Column(columnDefinition = "text")
    private String patchUrl;
    @Column(columnDefinition = "text")
    private String statePullRequest;
    private String title;
    private String url;
    @ManyToOne
    @JoinColumn(name = "mergedby_id")
    private EntityUser mergedBy;
    @ManyToOne
    @JoinColumn(name = "")
    private EntityUser user;
    @OneToOne
    @JoinColumn(name = "issue_id")
    private EntityIssue issue;
    @ManyToOne
    @JoinColumn(name = "repository_id")
    private EntityRepository repository;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<EntityRepositoryCommit> repositoryCommits;

    public EntityPullRequest() {
        mineredAt = new Date();
        repositoryCommits = new HashSet<EntityRepositoryCommit>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAdditions() {
        return additions;
    }

    public void setAdditions(Integer additions) {
        this.additions = additions;
    }

    public EntityPullRequestMarker getBase() {
        return base;
    }

    public void setBase(EntityPullRequestMarker base) {
        this.base = base;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public Integer getChangedFiles() {
        return changedFiles;
    }

    public void setChangedFiles(Integer changedFiles) {
        this.changedFiles = changedFiles;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getDeletions() {
        return deletions;
    }

    public void setDeletions(Integer deletions) {
        this.deletions = deletions;
    }

    public String getDiffUrl() {
        return diffUrl;
    }

    public void setDiffUrl(String diffUrl) {
        this.diffUrl = diffUrl;
    }

    public EntityPullRequestMarker getHead() {
        return head;
    }

    public void setHead(EntityPullRequestMarker head) {
        this.head = head;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public Long getIdPullRequest() {
        return idPullRequest;
    }

    public void setIdPullRequest(Long idPullRequest) {
        this.idPullRequest = idPullRequest;
    }

    public String getIssueUrl() {
        return issueUrl;
    }

    public void setIssueUrl(String issueUrl) {
        this.issueUrl = issueUrl;
    }

    public boolean isMergeable() {
        return mergeable;
    }

    public void setMergeable(boolean mergeable) {
        this.mergeable = mergeable;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public Date getMergedAt() {
        return mergedAt;
    }

    public void setMergedAt(Date mergedAt) {
        this.mergedAt = mergedAt;
    }

    public EntityUser getMergedBy() {
        return mergedBy;
    }

    public void setMergedBy(EntityUser mergedBy) {
        this.mergedBy = mergedBy;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPatchUrl() {
        return patchUrl;
    }

    public void setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
    }

    public String getStatePullRequest() {
        return statePullRequest;
    }

    public void setStatePullRequest(String statePullRequest) {
        this.statePullRequest = statePullRequest;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EntityUser getUser() {
        return user;
    }

    public void setUser(EntityUser user) {
        this.user = user;
    }

    public EntityIssue getIssue() {
        return issue;
    }

    public void setIssue(EntityIssue issue) {
        this.issue = issue;
    }

    public EntityRepository getRepository() {
        return repository;
    }

    public void setRepository(EntityRepository repository) {
        this.repository = repository;
    }

    public Integer getCommitsCount() {
        return commitsCount;
    }

    public void setCommitsCount(Integer commitsCount) {
        this.commitsCount = commitsCount;
    }

    public Set<EntityRepositoryCommit> getRepositoryCommits() {
        return repositoryCommits;
    }

    public void setRepositoryCommits(Set<EntityRepositoryCommit> repositoryCommits) {
        this.repositoryCommits = repositoryCommits;
    }

    public Date getMineredAt() {
        return mineredAt;
    }

    public void setMineredAt(Date mineredAt) {
        this.mineredAt = mineredAt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EntityPullRequest)) {
            return false;
        }
        EntityPullRequest other = (EntityPullRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityPullRequest[ id=" + id + " number=" + number + " ]";
    }

    public void addRepoCommit(EntityRepositoryCommit repoCommit) {
        repositoryCommits.add(repoCommit);
    }
}
