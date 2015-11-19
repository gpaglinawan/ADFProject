package com.fda.aps;

import com.fda.aps.view.managedbean.TabNavigationBean;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.domain.Number;

public class MainPageNavigation {
    public MainPageNavigation() {
        super();
    }
    private RichPanelTabbed mainTab;
    private Number requestId;
    private String comingFrom;
    private String wfStatus;
    private String submitFlag;


    public void setMainTab(RichPanelTabbed mainTab) {
        this.mainTab = mainTab;
    }

    public RichPanelTabbed getMainTab() {
        return mainTab;
    }

    public void onCenterLineClick(Number requestId, String comingFrom, String wfStatus, String submitFlag) {
        setRequestId(requestId);
        setComingFrom(comingFrom);
        setWfStatus(wfStatus);
        setSubmitFlag(submitFlag);
        TabNavigationBean navBean =
            (TabNavigationBean)JSFUtils.resolveExpression("#{navigationBean}");
        navBean.getDashBoardDetailItem().setDisclosed(false);
        navBean.getCreateRequestDetailItem().setDisclosed(true);

        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("requestId",
                                                                    requestId);
        AdfFacesContext.getCurrentInstance().addPartialTarget(navBean.getIndexTab());


        String itemLineNo =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("centerItemNo");
        if (itemLineNo != null) {
            int centerCodeIdx = StringUtils.nthOccurrence(itemLineNo, '-', 1);
            String centerItemCode =
                itemLineNo.substring(centerCodeIdx, centerCodeIdx + 1);
            AdfFacesContext.getCurrentInstance().getPageFlowScope().put("lineItmCode",
                                                                        centerItemCode);
            ADFContext.getCurrent().getRequestScope().put("lineItmCode",
                                                          centerItemCode);
            int dashFirstIndex = itemLineNo.indexOf("-");
            String centerCode = itemLineNo.substring(0, dashFirstIndex);
            AdfFacesContext.getCurrentInstance().getPageFlowScope().put("centerCode",
                                                                        centerCode);
        }
        
       
    }

    public void setRequestId(Number requestId) {
        this.requestId = requestId;
    }

    public Number getRequestId() {
        return requestId;
    }

    public void returnFromRequestCreation() {
        TabNavigationBean navBean =
            (TabNavigationBean)JSFUtils.resolveExpression("#{navigationBean}");
        navBean.getDashBoardDetailItem().setDisclosed(true);
        navBean.getCreateRequestDetailItem().setDisclosed(false);
        ADFContext.getCurrent().getSessionScope().put("requestId", Math.random());
        AdfFacesContext.getCurrentInstance().addPartialTarget(navBean.getIndexTab());

    }

    public void setComingFrom(String comingFrom) {
        this.comingFrom = comingFrom;
    }

    public String getComingFrom() {
        return comingFrom;
    }

    public void setWfStatus(String wfStatus) {
        this.wfStatus = wfStatus;
    }

    public String getWfStatus() {
        return wfStatus;
    }

    public void setSubmitFlag(String submitFlag) {
        this.submitFlag = submitFlag;
    }

    public String getSubmitFlag() {
        return submitFlag;
    }
}
