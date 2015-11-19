package com.fda.aps.view.managedbean;

import com.fda.aps.ADFUtils;

import com.fda.aps.JSFUtils;

import javax.faces.event.ActionEvent;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandImageLink;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.DialogListener;

import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.domain.Date;

public class AdminBean {
    private RichPopup slaPopup;
    private RichPopup addSlaPopup;

    public AdminBean() {
        super();
    }

    public void onClickOk(DialogEvent evt) {
        // Add event code here...
        String launchType =
            (String)AdfFacesContext.getCurrentInstance().getViewScope().get("launchType");

        if ("addSLA".equalsIgnoreCase(launchType)) {
            String actionType =
                (String)JSFUtils.resolveExpression("#{bindings.ActionType.inputValue}");
            oracle.jbo.domain.Number slaDays =
                (oracle.jbo.domain.Number)JSFUtils.resolveExpression("#{bindings.SlaDays.inputValue}");
            Date cutOffDate =
                (Date)JSFUtils.resolveExpression("#{bindings.CutOffDate.inputValue}");
            String type =
                (String)JSFUtils.resolveExpression("#{bindings.Type.inputValue}");
            OperationBinding addSla = ADFUtils.findOperation("addSLA");
            addSla.getParamsMap().put("description", actionType);
            addSla.getParamsMap().put("slaDays", slaDays);
            addSla.getParamsMap().put("cutoffDate", cutOffDate);
            addSla.getParamsMap().put("type", type);
            addSla.execute();
        }
        ADFUtils.getApplicationModuleForDataControl("APSApplicationModuleDataControl").getTransaction().commit();
        //ADFUtils.hidePopup(slaPopup);
    }

    public void setSlaPopup(RichPopup slaPopup) {
        this.slaPopup = slaPopup;
    }

    public RichPopup getSlaPopup() {
        return slaPopup;
    }

    public void launchSLAPopup(ActionEvent actionEvent) {
        // Add event code here...
        ADFUtils.showPopup(slaPopup);
    }

    public void launchAddSLAPopup(ActionEvent actionEvent) {
        // Add event code here...
        DCIteratorBinding referenceIter =
            ADFUtils.findIterator("ApsReferenceViewIterator");
        ViewObject refVO = referenceIter.getViewObject();
        Row newRefRow = refVO.createRow();
        refVO.insertRow(newRefRow);
        refVO.setCurrentRow(newRefRow);

        DCIteratorBinding slaCutOffIter =
            ADFUtils.findIterator("AapSlaCutoffdateVO1Iterator");
        ViewObject slaCutOffVO = slaCutOffIter.getViewObject();
        Row newSLARow = slaCutOffVO.createRow();
        slaCutOffVO.insertRow(newSLARow);
        slaCutOffVO.setCurrentRow(newSLARow);


        ADFUtils.showPopup(addSlaPopup);
    }

    public void setAddSlaPopup(RichPopup addSlaPopup) {
        this.addSlaPopup = addSlaPopup;
    }

    public RichPopup getAddSlaPopup() {
        return addSlaPopup;
    }

    public void onClickAddSla(DialogEvent dialogEvent) {
        // Add event code here...
        String actionType =
            (String)JSFUtils.resolveExpression("#{bindings.ActionType1.inputValue}");
        oracle.jbo.domain.Number slaDays =
            (oracle.jbo.domain.Number)JSFUtils.resolveExpression("#{bindings.SlaDays1.inputValue}");
        Date cutOffDate =
            (Date)JSFUtils.resolveExpression("#{bindings.CutOffDate1.inputValue}");
        String type =
            (String)JSFUtils.resolveExpression("#{bindings.Type1.inputValue}");
        OperationBinding addSla = ADFUtils.findOperation("addSLA");
        addSla.getParamsMap().put("description", actionType);
        addSla.getParamsMap().put("slaDays", slaDays);
        addSla.getParamsMap().put("type", type);
        addSla.execute();
    }

    public void createReferenceRow(ActionEvent evt) {
        RichCommandToolbarButton addButton =
            (RichCommandToolbarButton)evt.getSource();
        String iterName = (String)addButton.getAttributes().get("iterName");
        String refType = (String)addButton.getAttributes().get("refType");
        DCIteratorBinding addReferenceIter = ADFUtils.findIterator(iterName);
        ViewObject addReferenceViewObject = addReferenceIter.getViewObject();
        Row newRow = addReferenceViewObject.createRow();
        newRow.setNewRowState(Row.STATUS_INITIALIZED);
        newRow.setAttribute("ReferenceCode",
                            refType + newRow.getAttribute("ReferenceId"));
        newRow.setAttribute("ReferenceType", refType);
        addReferenceViewObject.last();
        addReferenceViewObject.next();
        addReferenceViewObject.insertRow(newRow);
    }

    // public void

    public void addNaics(ActionEvent actionEvent) {
        // Add event code here...
        createReferenceRow(actionEvent);
    }

    public void commit(ActionEvent evt) {
        RichCommandImageLink commitLink = null;
        RichCommandButton commitButton = null;
        String iterName = null;
        if (evt.getSource() instanceof RichCommandImageLink) {
            commitLink = (RichCommandImageLink)evt.getSource();
            iterName = (String)commitLink.getAttributes().get("iterName");
        }
        if (evt.getSource() instanceof RichCommandButton) {
            commitButton = (RichCommandButton)evt.getSource();
            iterName = (String)commitButton.getAttributes().get("iterName");
        }


        OperationBinding commit = ADFUtils.findOperation("Commit");
        commit.execute();
        commit.getErrors().size();

        ADFUtils.findIterator(iterName).getViewObject().executeQuery();
    }

    public void addActionType(ActionEvent actionEvent) {
        // Add event code here...
        createReferenceRow(actionEvent);
    }

    public void addContractType(ActionEvent actionEvent) {
        // Add event code here...
        createReferenceRow(actionEvent);
    }

    public void addServiceCode(ActionEvent actionEvent) {
        // Add event code here...
        createReferenceRow(actionEvent);
    }

    public void addRequirementType(ActionEvent actionEvent) {
        // Add event code here...
        createReferenceRow(actionEvent);
    }

    public void addCommodityType(ActionEvent actionEvent) {
        // Add event code here...
        createReferenceRow(actionEvent);
    }

    public void addCompetitionType(ActionEvent actionEvent) {
        // Add event code here...
        createReferenceRow(actionEvent);
    }

    public void commitActionType(ActionEvent actionEvent) {
        // Add event code here...
        commit(actionEvent);
    }

    public void commitNaicsCode(ActionEvent actionEvent) {
        // Add event code here...
        commit(actionEvent);
    }

    public void commitContractType(ActionEvent actionEvent) {
        // Add event code here...
        commit(actionEvent);
    }

    public void commitServiceCOde(ActionEvent actionEvent) {
        // Add event code here...
        commit(actionEvent);
    }

    public void commitRequirementType(ActionEvent actionEvent) {
        // Add event code here...
        commit(actionEvent);
    }

    public void commitCommodityType(ActionEvent actionEvent) {
        // Add event code here...
        commit(actionEvent);
    }

    public void commitCommpetitionType(ActionEvent actionEvent) {
        // Add event code here...
        commit(actionEvent);
    }

    public void onCommitStartDate(ActionEvent actionEvent) {
        // Add event code here...
        commit(actionEvent);
    }
}
