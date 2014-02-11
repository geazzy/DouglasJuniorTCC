/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.EntityNode;
import br.edu.utfpr.cm.JGitMinerWeb.services.matriz.nodes.NodeGeneric;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import br.edu.utfpr.cm.JGitMinerWeb.util.Util;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author douglas
 */
public abstract class AbstractServices implements Runnable, Serializable {

    protected final GenericDao dao;
    protected List<EntityNode> nodes;
    protected Map params;
    protected OutLog out;

    public AbstractServices(GenericDao dao, OutLog out) {
        this.dao = dao;
        this.out = out;
    }

    public AbstractServices(GenericDao dao, Map params, OutLog out) {
        this(dao, out);
        this.params = params;
    }

    public Date getBeginDate() {
        return getDateParam("beginDate");
    }

    public Date getEndDate() {
        return getDateParam("endDate");
    }
    
    protected Integer getMilestoneNumber() {
        String mileNumber = params.get("milestoneNumber") + "";
        return Util.tratarStringParaInt(mileNumber);
    }

    public List<EntityNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<EntityNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public abstract void run();

    public String convertToCSV() {
        StringBuilder sb = new StringBuilder(getHeadCSV());
        sb.append("\n");
        for (EntityNode node : nodes) {
            sb.append(node.getLine()).append("\n");
        }
        return sb.toString();
    }

    public static <T> T createInstance(GenericDao dao, OutLog out, String className) {
        try {
            return (T) Class.forName(className).getConstructor(GenericDao.class, OutLog.class).newInstance(dao, out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected void incrementNode(List<NodeGeneric> nodes, NodeGeneric node) {
        int index = nodes.indexOf(node);
        if (index >= 0) {
            nodes.get(index).incWeight();
        } else {
            nodes.add(node);
        }
    }

    protected Date getDateParam(Object key) {
        if (!params.containsKey(key)) {
            throw new IndexOutOfBoundsException(key + "");
        }
        Object obj = params.get(key);
        if (obj != null) {
            if (obj instanceof Date) {
                return (Date) obj;
            } else {
                throw new ClassCastException(key + "");
            }
        }
        return null;
    }

    /**
     * Name of columns separated by ";".
     *
     * @return column1;column2;column3;...
     */
    public abstract String getHeadCSV();
}
