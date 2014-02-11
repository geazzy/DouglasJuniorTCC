/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matriz.EntityMatriz;
import br.edu.utfpr.cm.JGitMinerWeb.model.metric.EntityMetricNode;
import br.edu.utfpr.cm.JGitMinerWeb.services.AbstractServices;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.AbstractMatrizServices;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author douglas
 */
public abstract class AbstractMetricServices extends AbstractServices {

    private final EntityMatriz matriz;

    public AbstractMetricServices(GenericDao dao, OutLog out) {
        super(dao, out);
        this.matriz = null;
    }

    public AbstractMetricServices(GenericDao dao, EntityMatriz matriz, Map params, OutLog out) {
        super(dao, params, out);
        this.matriz = matriz;
    }

    public EntityMatriz getMatriz() {
        return matriz;
    }

    @Override
    public abstract void run();

    /**
     * Name of columns separated by ";".
     *
     * @return column1;column2;column3;...
     */
    @Override
    public abstract String getHeadCSV();

    protected void addToEntityMetricNodeList(List list) {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        for (Object obj : list) {
            nodes.add(new EntityMetricNode(obj));
        }
    }

    public List<EntityMetricNode> getMetricNodes() {
        return (List) super.getNodes();
    }

    /**
     * @return List the names of "classes services" available matrices allowed for this metric.
     */
    public abstract List<String> getAvailableMatricesPermitted();
}
