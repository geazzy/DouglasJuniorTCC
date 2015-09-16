/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitFile;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.kohsuke.github.GHCommit;

/**
 *
 * @author Douglas
 */
public class CommitFileServices implements Serializable {

    public static EntityCommitFile createEntity(GHCommit.File gitCommitFile, GenericDao dao, EntityRepositoryCommit repoCommit) {
        if (gitCommitFile == null) {
            return null;
        }

        EntityCommitFile commitFile = findByCommitAndSHA(gitCommitFile.getSha(), repoCommit, dao);

        if (commitFile == null) {
            commitFile = new EntityCommitFile();
            commitFile.setMineredAt(new Date());
            commitFile.setAdditions(gitCommitFile.getLinesAdded());
            commitFile.setBlobUrl(gitCommitFile.getBlobUrl().toString());
            commitFile.setChanges(gitCommitFile.getLinesChanged());
            commitFile.setDeletions(gitCommitFile.getLinesDeleted());
            commitFile.setFilename(gitCommitFile.getFileName());
            commitFile.setPatch(gitCommitFile.getPatch());
            commitFile.setRawUrl(gitCommitFile.getRawUrl().toString());
            commitFile.setSha(gitCommitFile.getSha());
            commitFile.setStatus(gitCommitFile.getStatus());
            dao.insert(commitFile);
        }

        return commitFile;
    }

    private static EntityCommitFile findByCommitAndSHA(String sha, EntityRepositoryCommit repoCommit, GenericDao dao) {
        List<EntityCommitFile> files = dao.executeNamedQueryWithParams("CommitFile.findByCommitAndSHA", new String[]{"sha", "repoCommit"}, new Object[]{sha, repoCommit}, true);
        if (!files.isEmpty()) {
            return files.get(0);
        }
        return null;
    }
}
