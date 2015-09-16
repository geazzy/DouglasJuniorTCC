/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric.social;

import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityCommitComment;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityPullRequest;
import br.edu.utfpr.cm.JGitMinerWeb.model.miner.EntityRepositoryCommit;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxAllMetrics;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.joda.time.DateTime;
import org.joda.time.Hours;

/**
 *
 * @author zanoni
 */
public class TempoDeResposta implements MetricInterface {

    HashMap<Long, Integer> result;

    public TempoDeResposta() {
        result = new HashMap<>();
    }

    @Override
    public void calculateMetric(AuxAllMetrics pair) {

        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object obj1, Object obj2) {
                EntityComment to1 = (EntityComment) obj1;
                EntityComment to2 = (EntityComment) obj2;

                return to1.getCreatedAt().compareTo(to2.getCreatedAt());
            }
        };

        for (EntityPullRequest pullRequest : pair.getPullRequests()) {

            DateTime dataAnterior = null;
            Integer mean = 0;
            // System.out.println("comments size: " + pullRequest.getIssue().getComments().size());
            try {
                List<EntityComment> comentarios = Lists.newArrayList(pullRequest.getIssue().getComments());
                Collections.sort(comentarios, comparator);

                for (EntityComment comment : comentarios) {

                    if (comentarios.indexOf(comment) != 0) {
                        int anterior = comentarios.indexOf(comment) - 1;
                        dataAnterior = new DateTime(comentarios.get(anterior).getCreatedAt());
                        mean += Hours.hoursBetween(dataAnterior, new DateTime(comment.getCreatedAt())).getHours();
                    }

                }
                if (comentarios.size() > 0) {
                    mean = mean / comentarios.size();
                }

                result.put(pair.getId(), mean);

            } catch (Exception e) {
                System.out.println("Erro");
                System.out.println(e.toString());
            }

        }
    }

    @Override
    public MetricResult getResult(Long indexPairOfFiles) {
        return new MetricResult(result.get(indexPairOfFiles), null, null);
    }

    @Override
    public String getHeadCSV() {
        return TempoDeResposta.class.getSimpleName() + "SUM;"
                + TempoDeResposta.class.getSimpleName() + "MEAN;"
                + TempoDeResposta.class.getSimpleName() + "ENTROPY";
    }

}
