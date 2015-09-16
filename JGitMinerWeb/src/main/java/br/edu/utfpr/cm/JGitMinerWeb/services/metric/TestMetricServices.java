/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.JGitMinerWeb.services.metric;

import br.edu.utfpr.cm.JGitMinerWeb.dao.GenericDao;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrix;
import br.edu.utfpr.cm.JGitMinerWeb.model.matrix.EntityMatrixNode;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.CoChangedFileServices;
import br.edu.utfpr.cm.JGitMinerWeb.services.matrix.auxiliary.AuxPairOfFiles;
import br.edu.utfpr.cm.JGitMinerWeb.services.metric.auxiliary.AuxTeste;
import br.edu.utfpr.cm.JGitMinerWeb.util.JsfUtil;
import br.edu.utfpr.cm.JGitMinerWeb.util.OutLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author geazzy
 */
public class TestMetricServices extends AbstractMetricServices {

    List<AuxPairOfFiles> cochangeList;
    List<AuxTeste> countList;

    public TestMetricServices(GenericDao dao, OutLog out) {
        super(dao, out);
        cochangeList = new ArrayList<>();
        countList = new ArrayList<>();
    }

    public TestMetricServices(GenericDao dao, EntityMatrix matrix, Map params, OutLog out) {
        super(dao, matrix, params, out);
        cochangeList = new ArrayList<>();
        countList = new ArrayList<>();
    }

    @Override
    public void run() {

        if (getMatrix() == null && !getAvailableMatricesPermitted()
                .contains(getMatrix().getClassServicesName())) {
            throw new IllegalArgumentException("Selecione uma matriz gerada pelo Service: "
                    + getAvailableMatricesPermitted());
        }

        System.out.println("Selecionado matriz com " + getMatrix().getNodes().size() + " nodes.");

        setCoChangedList();

        Map<AuxPairOfFiles, Integer> coChangeCountMap = countCoChanges();
        countList = setCountList(coChangeCountMap);
        System.out.println("Count List: " + countList);
        addToEntityMetricNodeList(countList);

    }

    private void setCoChangedList() throws NumberFormatException {

        for (EntityMatrixNode node : getMatrix().getNodes()) {
            String[] coluns = node.getLine().split(JsfUtil.TOKEN_SEPARATOR);

            cochangeList.add(new AuxPairOfFiles(Long.parseLong(coluns[0]), coluns[1], coluns[2], coluns[3], coluns[4]));

        }
    }

    private List<AuxTeste> setCountList(Map<AuxPairOfFiles, Integer> coChangeCountMap) {

        List<AuxTeste> countListTemp = new ArrayList<>();
        for (AuxPairOfFiles auxCoChanged : coChangeCountMap.keySet()) {
            countListTemp.add(new AuxTeste(auxCoChanged, coChangeCountMap.get(auxCoChanged)));
        }
        return countListTemp;
    }

    private Map<AuxPairOfFiles, Integer> countCoChanges() {

        Map<AuxPairOfFiles, Integer> coChangeCountMap = new HashMap<>();

        for (AuxPairOfFiles auxcochange : cochangeList) {
            int count = 0;
            if (!coChangeCountMap.containsKey(auxcochange)) {
                for (AuxPairOfFiles cochangeList1 : cochangeList) {
                    if (cochangeList1.equals(auxcochange)) {
                        count++;
                    }
                }
                coChangeCountMap.put(auxcochange, count);
            }
        }
        return coChangeCountMap;
    }

    @Override
    public String getHeadCSV() {
        return "issue;arq1;arq2;sha1;sha2;count";
    }

    @Override
    public List<String> getAvailableMatricesPermitted() {
        return Arrays.asList(CoChangedFileServices.class.getName());
    }

}
