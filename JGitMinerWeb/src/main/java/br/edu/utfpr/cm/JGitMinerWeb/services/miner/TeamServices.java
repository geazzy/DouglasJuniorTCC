/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.miner;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityTeam;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTeam;
/**
 *
 * @author douglas
 */
public class TeamServices implements Serializable {

    public static List<GHTeam> getGitTeamsFromRepository(GHRepository gitRepo, OutLog out) {
        List<GHTeam> teams = new ArrayList<>();
        try {
            out.printLog("Baixando Teams...\n");
            
            teams.addAll(AuthServices.getGitHubClient().getRepository(gitRepo.getFullName()).getTeams());
            out.printLog(teams.size() + " Teams baixados no total!");
        } catch (Exception ex) {
            ex.printStackTrace();
            out.printLog(teams.size() + " Teams baixadaos no total! Erro: " + ex.toString());
        }
        return teams;
    }

    public static EntityTeam createEntity(GHTeam gitTeam, GenericDao dao) {
        try {
            if (gitTeam == null) {
                return null;
            }
            
            EntityTeam team = getTeamByTeamID(gitTeam.getId(), dao);
            
            if (team == null) {
                team = new EntityTeam();
            }
            
            team.setIdTeam(gitTeam.getId());
            team.setMembersCount(gitTeam.getMembers().size());
            team.setName(gitTeam.getName());
            team.setPermission(gitTeam.getPermission());
            team.setReposCount(gitTeam.getRepositories().size());
            team.setUrl(null);
            
            if (team.getId() == null || team.getId().equals(new Long(0))) {
                dao.insert(team);
            } else {
                dao.edit(team);
            }
            
            return team;
        } catch (IOException ex) {
            Logger.getLogger(TeamServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static EntityTeam getTeamByTeamID(int idTeam, GenericDao dao) {
        List<EntityTeam> teams = dao.executeNamedQueryWithParams("Team.findByTeamID", new String[]{"idTeam"}, new Object[]{idTeam}, true);
        if (!teams.isEmpty()) {
            return teams.get(0);
        }
        return null;
    }
}
