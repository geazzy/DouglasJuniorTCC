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
@Table(name = "gitCommit", indexes = {
    @Index(columnList = "author_id"),
    @Index(columnList = "committer_id"),
    @Index(columnList = "tree_id"),
    @Index(columnList = "url", unique = true)
})
@NamedQueries({
    @NamedQuery(name = "Commit.findByURL", query = "SELECT c FROM EntityCommit c WHERE c.url = :url")
})
public class EntityCommit implements InterfaceEntity, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date mineredAt;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private EntityCommitUser author;
    @ManyToOne
    @JoinColumn(name = "committer_id")
    private EntityCommitUser committer;
    @Column(columnDefinition = "text")
    private String message;
    private String sha;
    @Column(unique = true, name = "url")
    private String url;
    @ManyToOne
    @JoinColumn(name = "tree_id")
    private EntityTree tree;
    private Integer commentCount;

    public EntityCommit() {
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

    public EntityCommitUser getAuthor() {
        return author;
    }

    public void setAuthor(EntityCommitUser author) {
        this.author = author;
    }

    public EntityCommitUser getCommitter() {
        return committer;
    }

    public void setCommitter(EntityCommitUser committer) {
        this.committer = committer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public List<EntityCommit> getParents() {
//        return parents;
//    }
//
//    public void setParents(List<EntityCommit> parents) {
//        this.parents = parents;
//    }
//
//    public EntityCommit getSon() {
//        return son;
//    }
//
//    public void setSon(EntityCommit son) {
//        this.son = son;
//    }
    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public EntityTree getTree() {
        return tree;
    }

    public void setTree(EntityTree tree) {
        this.tree = tree;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntityCommit)) {
            return false;
        }
        EntityCommit other = (EntityCommit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.utfpr.cm.JGitMiner.pojo.EntityCommity[ id=" + id + " ]";
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }
}
