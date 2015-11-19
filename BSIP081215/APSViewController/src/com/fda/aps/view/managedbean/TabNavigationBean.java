package com.fda.aps.view.managedbean;

import com.fda.aps.ADFUtils;

import javax.faces.component.UIComponent;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.servlet.http.HttpServletRequest;


import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.fragment.RichRegion;

import oracle.jbo.domain.Number;

import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.OperationBinding;

import org.apache.myfaces.trinidad.event.DisclosureEvent;

public class TabNavigationBean {

    private RichShowDetailItem dashBoardDetailItem;
    private RichShowDetailItem createRequestDetailItem;
    private RichPanelTabbed indexTab;
    private RichRegion requestRegion;
    private RichShowDetailItem searchDetailItem;
    private RichShowDetailItem massShowDetailItem;

    public void setDashBoardDetailItem(RichShowDetailItem dashBoardDetailItem) {
        this.dashBoardDetailItem = dashBoardDetailItem;
    }

    public RichShowDetailItem getDashBoardDetailItem() {
        return dashBoardDetailItem;
    }

    public void setCreateRequestDetailItem(RichShowDetailItem createRequestDetailItem) {
        this.createRequestDetailItem = createRequestDetailItem;
    }

    public RichShowDetailItem getCreateRequestDetailItem() {
        return createRequestDetailItem;
    }

    public void setIndexTab(RichPanelTabbed indexTab) {
        this.indexTab = indexTab;
    }

    public RichPanelTabbed getIndexTab() {
        return indexTab;
    }

    public void setRequestRegion(RichRegion requestRegion) {
        this.requestRegion = requestRegion;
    }

    public RichRegion getRequestRegion() {
        return requestRegion;
    }

    public void onCreateRequestDisclosed(DisclosureEvent disclosureEvent) {
        // Add event code here...
        if (createRequestDetailItem.isDisclosed()){
            System.err.println("test");
            AdfFacesContext.getCurrentInstance().getPageFlowScope().put("requestId", null);
            AdfFacesContext.getCurrentInstance().getPageFlowScope().put("comingFrom", null);
            
            ADFContext.getCurrent().getSessionScope().put("requestId", Math.random());
            ADFUtils.setValueToEL("#{bindings.comingFrom.inputValue}", "y");
            
            
            OperationBinding refreshTab = ADFUtils.findOperation("onCenterLineClick");
            refreshTab.getParamsMap().put("requestId", null);
            refreshTab.getParamsMap().put("comingFrom", null);
            refreshTab.execute();
        }
    }
    
    public boolean isAcquisitionLead() {
        String acquisitionLead = "false";

        OperationBinding isAcquisitionLead =
            ADFUtils.findOperation("isAcquisitionLead");
        acquisitionLead = (String)isAcquisitionLead.execute();
        if (acquisitionLead != null ){
            return acquisitionLead.equalsIgnoreCase("true");
        }
        return false;
    }
    
    public boolean isBudgetLead() {
        Object acquisitionLead = null;

        OperationBinding isAcquisitionLead =
            ADFUtils.findOperation("isBudgetLead");
        acquisitionLead =
                isAcquisitionLead.execute();
        
        if(acquisitionLead != null){
            return (Boolean)acquisitionLead;
        }
        
        
        return false;
    }

    public void setSearchDetailItem(RichShowDetailItem searchDetailItem) {
        this.searchDetailItem = searchDetailItem;
    }

    public RichShowDetailItem getSearchDetailItem() {
        return searchDetailItem;
    }

    public void onSearchDisclosed(DisclosureEvent disclosureEvent) {
        // Add event code here...
        if (searchDetailItem.isDisclosed()){
            ADFContext.getCurrent().getSessionScope().put("requestId", Math.random());
        }
    }
    
    public void onMassDisclosed(DisclosureEvent disclosureEvent) {
        // Add event code here...
        if (massShowDetailItem.isDisclosed()){
            ADFContext.getCurrent().getSessionScope().put("requestId", Math.random());
        }
    }

    public void setMassShowDetailItem(RichShowDetailItem massShowDetailItem) {
        this.massShowDetailItem = massShowDetailItem;
    }

    public RichShowDetailItem getMassShowDetailItem() {
        return massShowDetailItem;
    }
}
