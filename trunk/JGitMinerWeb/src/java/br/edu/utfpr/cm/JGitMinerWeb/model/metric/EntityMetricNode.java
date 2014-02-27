/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.model.metric;

import br.edu.utfpr.cm.JGitMinerWeb.model.EntityNode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author douglas
 */
@Entity
@DiscriminatorValue(value = "EntityMetricNode")
public class EntityMetricNode extends EntityNode {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metric_id")
    private EntityMetric metric;

    public EntityMetricNode() {
        super();
    }

    public EntityMetricNode(Object line) {
        super(line);
    }

    public EntityMetric getMetric() {
        return metric;
    }

    public void setMetric(EntityMetric metric) {
        this.metric = metric;
    }
}
