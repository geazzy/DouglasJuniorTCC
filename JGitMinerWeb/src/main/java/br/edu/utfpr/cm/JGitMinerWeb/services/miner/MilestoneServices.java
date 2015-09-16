/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityMilestone;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepository;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHRepository;

/**
 *
 * @author Douglas
 */
public class MilestoneServices implements Serializable {

    public static EntityMilestone createEntity(GHMilestone gitMilestone, EntityRepository repository, GenericDao dao) {
        if (gitMilestone == null) {
            return null;
        }

        EntityMilestone milestone = getMilestoneByURL(gitMilestone.getUrl().toString(), dao);

        if (milestone == null) {
            try {
                milestone = new EntityMilestone();

                System.out.println("milestone creator: "+gitMilestone.getCreator());
                
               // System.out.println("milestone creator CreateAt: "+gitMilestone.getCreator().get);
                
                milestone.setCreator(UserServices.createEntity(gitMilestone.getCreator(), dao, false));
                milestone.setUrl(gitMilestone.getUrl().toString());
                milestone.setNumber(gitMilestone.getNumber());
                milestone.setCreatedAt(gitMilestone.getCreatedAt());
                if (repository != null) {
                    milestone.setRepository(repository);
                }
            } catch (IOException ex) {
                Logger.getLogger(MilestoneServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        milestone.setDueOn(gitMilestone.getDueOn());
        milestone.setClosedIssues(gitMilestone.getClosedIssues());
        milestone.setOpenIssues(gitMilestone.getOpenIssues());
        milestone.setDescription(gitMilestone.getDescription());
        milestone.setStateMilestone(gitMilestone.getState().toString());
        milestone.setTitle(gitMilestone.getTitle());

        if (milestone.getId() == null || milestone.getId().equals(new Long(0))) {
            dao.insert(milestone);
        } else {
            dao.edit(milestone);
        }

        return milestone;
    }

    private static EntityMilestone getMilestoneByURL(String url, GenericDao dao) {
        List<EntityMilestone> miles = dao.executeNamedQueryWithParams("Milestone.findByURL", new String[]{"url"}, new Object[]{url}, true);
        if (!miles.isEmpty()) {
            return miles.get(0);
        }
        return null;
    }

    public static List<GHMilestone> getGitMilestoneFromRepository(GHRepository gitRepo, boolean open, boolean closed, OutLog out) {
        List<GHMilestone> milestones = new ArrayList<GHMilestone>();
        try {

            if (open) {
                List<GHMilestone> opens;
                out.printLog("Baixando Milestones Abertos...\n");
                opens = AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).listMilestones(GHIssueState.OPEN).asList();

                out.printLog(opens.size() + " Milestones abertos baixadas!");
                milestones.addAll(opens);
            }
            if (closed) {
                List<GHMilestone> closeds;
                out.printLog("Baixando Milestones Fechados...\n");
                closeds = AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).listMilestones(GHIssueState.OPEN).asList();

                out.printLog(closeds.size() + " Milestones fechados baixadas!");
                milestones.addAll(closeds);
            }
            out.printLog(milestones.size() + " Milestones baixados no total!");
        } catch (Exception ex) {
            ex.printStackTrace();
            out.printLog("Erro: " + ex.toString());
        }
        return milestones;
    }
}
