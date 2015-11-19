package com.fda.aps.view.managedbean;

import com.fda.aps.ADFUtils;
import com.fda.aps.JSFUtils;

import com.fda.aps.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;

import java.util.logging.Logger;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.adf.share.ADFContext;
import oracle.adf.share.logging.ADFLogger;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.DialogEvent.Outcome;

import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;
import oracle.jbo.domain.Number;
import oracle.jbo.server.ViewObjectImpl;

import org.apache.myfaces.trinidad.model.RowKeySet;

public class BudgetBean {
    private RichPopup addBudgetDetailsPopup;
    private RichPopup canSearcPopup;
    private RichPopup approveRequestPopup;
    private RichPopup confirmSubmitPopup;
    private RichTable canSearchTable;
    private RichPopup budgetApprovePopup;

    private static List<String> rowKeyList = new ArrayList<String>();
    private RichPopup budgetRejectPopup;
    private static ADFLogger _logger = 
            ADFLogger.createADFLogger(BudgetBean.class); 

    public BudgetBean() {
        super();
    }

    public void onLineItemChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        _logger.info("Entering onLineItemChanged");
        ADFUtils.setValueToEL("#{bindings.CenterItemNoROVO.inputValue}",
                              valueChangeEvent.getNewValue());
        DCIteratorBinding iterator =
            ADFUtils.findIterator("CenterItemNoROVOIterator");
        Row currentRow = iterator.getCurrentRow();
        String itemLineNo = (String)currentRow.getAttribute("CenterItemNo");
        Number requestId = (Number)currentRow.getAttribute("RequestId");
        System.err.println("itemLineNo " + itemLineNo);
        int centerCodeIdx = StringUtils.nthOccurrence(itemLineNo, '-', 1);
        String centerItemCode =
            itemLineNo.substring(centerCodeIdx, centerCodeIdx + 1);
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("lineItmNo",
                                                                    itemLineNo);
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("lineItmCode",
                                                                    centerItemCode);

        OperationBinding executeRequest =
            ADFUtils.findOperation("executeSubmittedRequest");
        executeRequest.getParamsMap().put("requestId", requestId);
        executeRequest.execute();
    }

    public void onCenterLineSelected(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void addBudgetDetail(ActionEvent actionEvent) {
        // Add event code here...
        //        OperationBinding addBudgetDetail =
        //            ADFUtils.findOperation("addBudgetDetail");
        //        addBudgetDetail.execute();

        //String itemLineNo = (String)JSFUtils.resolveExpression("#{bindings.CenterItemNo.inputValue}");
        _logger.info("Entering addBudgetDetail");
        DCIteratorBinding iter =
            ADFUtils.findIterator("CenterItemNoROVOIterator");
        Row currentRow = iter.getCurrentRow();
        String itemLineNo = (String)currentRow.getAttribute("CenterItemNo");
        Number requestId = (Number)currentRow.getAttribute("RequestId");
        int dashFirstIndex = itemLineNo.indexOf("-");
        String centerCode = itemLineNo.substring(0, dashFirstIndex);
        OperationBinding filterCan = ADFUtils.findOperation("filterCanSearch");
        filterCan.getParamsMap().put("center", centerCode);
        filterCan.getParamsMap().put("requestId", requestId);
        filterCan.execute();

        rowKeyList.clear();


        ADFUtils.showPopup(canSearcPopup);
    }

    public void setAddBudgetDetailsPopup(RichPopup addBudgetDetailsPopup) {
        this.addBudgetDetailsPopup = addBudgetDetailsPopup;
    }

    public RichPopup getAddBudgetDetailsPopup() {
        return addBudgetDetailsPopup;
    }

    public void setCanSearcPopup(RichPopup canSearcPopup) {
        this.canSearcPopup = canSearcPopup;
    }

    public RichPopup getCanSearcPopup() {
        return canSearcPopup;
    }

    public void launchAllowanceSearch(ActionEvent actionEvent) {
        launchCanSearchPopup(actionEvent);
    }

    public void launchSubAllowanceSearch(ActionEvent actionEvent) {
        launchCanSearchPopup(actionEvent);
    }

    public void launchCostCenter(ActionEvent actionEvent) {
        launchCanSearchPopup(actionEvent);
    }

    private void launchCanSearchPopup(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering launchCanSearchPopup");
        Object bap = JSFUtils.resolveExpression("#{bindings.Bap.inputValue}");
        Object fund =
            JSFUtils.resolveExpression("#{bindings.Fund.inputValue}");
        Object allowance =
            JSFUtils.resolveExpression("#{bindings.Allowance.inputValue}");
        Object subAllowance =
            JSFUtils.resolveExpression("#{bindings.SubAllowance.inputValue}");
        Object costCenter =
            JSFUtils.resolveExpression("#{bindings.CostCenter.inputValue}");
        DCIteratorBinding iterator =
            ADFUtils.findIterator("CANSearchIterator");
        ViewObjectImpl canSearchVO = (ViewObjectImpl)iterator.getViewObject();
        canSearchVO.clearCache();
        ViewCriteria criteria =
            canSearchVO.getViewCriteria("CANSearchCriteria");
        canSearchVO.setNamedWhereClauseParam("bind_Bap", bap);
        canSearchVO.setNamedWhereClauseParam("bind_Fund", fund);
        canSearchVO.setNamedWhereClauseParam("bind_Allowance", allowance);
        canSearchVO.setNamedWhereClauseParam("bind_SubAllowance",
                                             subAllowance);
        canSearchVO.setNamedWhereClauseParam("bind_CostCenter", costCenter);
        canSearchVO.applyViewCriteria(criteria);
        //canSearchVO.executeQuery();


        ADFUtils.showPopup(canSearcPopup);


    }

    public void onCanSelected(ActionEvent actionEvent) {
        _logger.info("Entering onCanSelected");
        // Add event code here...
        RowKeySet selectedEmps = getCanSearchTable().getSelectedRowKeys();
        Iterator selectedEmpIter = selectedEmps.iterator();
        DCIteratorBinding canIterator =
            ADFUtils.findIterator("CANSearchIterator");
        DCIteratorBinding detailedIterator =
            ADFUtils.findIterator("DetailedExpensesViewIterator");

        ViewObject detailedVO = detailedIterator.getViewObject();
        RowSetIterator canRSIter = canIterator.getRowSetIterator();

        while (selectedEmpIter.hasNext()) {
            Row expensesCurrentRow = detailedVO.createRow();

            Key key = (Key)((List)selectedEmpIter.next()).get(0);
            Row currentRow = canRSIter.getRow(key);
            //System.out.println(currentRow.getAttribute("Ename"));

            expensesCurrentRow.setAttribute("Bap",
                                            currentRow.getAttribute("Bap"));
            expensesCurrentRow.setAttribute("Fund",
                                            currentRow.getAttribute("Fund"));
            expensesCurrentRow.setAttribute("SubAllowance",
                                            currentRow.getAttribute("Suballowance"));
            expensesCurrentRow.setAttribute("Allowance",
                                            currentRow.getAttribute("Allowance"));
            //        expensesCurrentRow.setAttribute("Fund",
            //                                        canCurrentRow.getAttribute("Fund"));
            expensesCurrentRow.setAttribute("CostCenter",
                                            currentRow.getAttribute("CostCenter"));

            detailedVO.insertRow(expensesCurrentRow);

        }


        //        expensesCurrentRow.setAttribute("FundingObjCode",
        //                                        null);


        ADFUtils.hidePopup(canSearcPopup);

    }

    public void onSaveBudgetDetail(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering onSaveBudgetDetail");
        ADFUtils.findOperation("Commit").execute();

        ADFUtils.hidePopup(addBudgetDetailsPopup);
    }

    public void onCancelBudgetDetails(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering onCancelBudgetDetails");
        DCIteratorBinding expensesIterator =
            ADFUtils.findIterator("DetailedExpensesViewIterator");
        expensesIterator.getCurrentRow().remove();
        ADFUtils.hidePopup(addBudgetDetailsPopup);
    }

    public void setApproveRequestPopup(RichPopup approveRequestPopup) {
        this.approveRequestPopup = approveRequestPopup;
    }

    public RichPopup getApproveRequestPopup() {
        return approveRequestPopup;
    }

    public void cancelApproveRequest(ActionEvent actionEvent) {
        // Add event code here...
        ADFUtils.hidePopup(approveRequestPopup);
    }

    public void approveRequest(ActionEvent actionEvent) {
        // Add event code here...
        //approveRequestPopup

    }

    public void setConfirmSubmitPopup(RichPopup confirmSubmitPopup) {
        this.confirmSubmitPopup = confirmSubmitPopup;
    }

    public RichPopup getConfirmSubmitPopup() {
        return confirmSubmitPopup;
    }

    public void launchSubmitDetailPopup(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering launchSubmitDetailPopup");
        OperationBinding insertComment =
            ADFUtils.findOperation("insertComment");
        insertComment.execute();
        ADFUtils.showPopup(confirmSubmitPopup);
    }

    public void rejectAquisitionByBudgetUser(ActionEvent actionEvent) {


    }

    public void submitDetailToBudgetLead(ActionEvent actionEvent) {
        // Add event code here...

        ADFUtils.findOperation("submitBudgetRequest").execute();
        ADFUtils.hidePopup(confirmSubmitPopup);
    }

    public void cancelBudgetDetailSubmit(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering cancelBudgetDetailSubmit");
        DCIteratorBinding commentIter =
            ADFUtils.findIterator("CommentsVOIterator");
        Row row = commentIter.getViewObject().getCurrentRow();
        if (row != null) {
            row.remove();
        }
        ADFUtils.hidePopup(confirmSubmitPopup);
    }

    public boolean isBudgetLead() {
        boolean isBudgetLead = false;
        isBudgetLead =
                ((Boolean)ADFUtils.findOperation("isBudgetLead").execute()).booleanValue();
        return isBudgetLead;
    }

    public void setCenterLineToScope(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering setCenterLineToScope");
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
        }
    }

    public void launchBap(ActionEvent actionEvent) {
        // Add event code here...
        launchCanSearchPopup(actionEvent);
    }

    public void launchFund(ActionEvent actionEvent) {
        // Add event code here...
        launchCanSearchPopup(actionEvent);
    }

    public void setCanSearchTable(RichTable canSearchTable) {
        this.canSearchTable = canSearchTable;
    }

    public RichTable getCanSearchTable() {
        return canSearchTable;
    }

    public void onOkAction(DialogEvent dialogEvent) {
        // Add event code here...
        _logger.info("Entering onOkAction");
        if (dialogEvent.getOutcome().equals(Outcome.ok)) {

            RowKeySet selectedEmps = getCanSearchTable().getSelectedRowKeys();
            Iterator selectedEmpIter = selectedEmps.iterator();
            DCIteratorBinding submittedRequest =
                ADFUtils.findIterator("SubmittedRequestsViewIterator");
            DCIteratorBinding canIterator =
                ADFUtils.findIterator("FdaCanROVOIterator");
            DCIteratorBinding detailedIterator =
                ADFUtils.findIterator("DetailedExpensesViewIterator");
            //ViewObjectImpl canVo = (ViewObjectImpl)canIterator.getViewObject();
            ViewObject requestVO = submittedRequest.getViewObject();
            ViewObject detailedVO = detailedIterator.getViewObject();
            ViewObjectImpl canSearchVO =
                (ViewObjectImpl)canIterator.getViewObject();
            Object fiscalYear = requestVO.getCurrentRow().getAttribute("FiscalYear");
            // RowSetIterator canRSIter = canIterator.getRowSetIterator();
            for (String r : rowKeyList) {


                //Key key = (Key)((List)selectedEmpIter.next()).get(0);
                //Key key = new Key(new Object[] { r });

                //Row currentRow = canRSIter.findByKey(key, 1)[0];
                //canRSIter.find(key)
                //System.out.println(currentRow.getAttribute("Ename"));
                ViewCriteria canCriteria =
                    canSearchVO.getViewCriteria("ByCan");
                canSearchVO.applyViewCriteria(canCriteria);
                canSearchVO.setNamedWhereClauseParam("bind_Can", r);
                canSearchVO.setNamedWhereClauseParam("bind_FiscalYear", fiscalYear);
                canSearchVO.executeQuery();
                Row currentRow = canSearchVO.first();
                if (currentRow != null) {
                    Row expensesCurrentRow = detailedVO.createRow();
                    expensesCurrentRow.setAttribute("Center",
                                                    currentRow.getAttribute("Center"));
                    expensesCurrentRow.setAttribute("Bap",
                                                    currentRow.getAttribute("FdaBap"));
                    expensesCurrentRow.setAttribute("BapDescrptn",
                                                    currentRow.getAttribute("FdaBapDescription"));
                    expensesCurrentRow.setAttribute("Fund",
                                                    currentRow.getAttribute("FdaFund"));
                    expensesCurrentRow.setAttribute("FundDescrptn",
                                                    currentRow.getAttribute("FdaFundDescription"));
                    expensesCurrentRow.setAttribute("SubAllowance",
                                                    currentRow.getAttribute("FdaSubAllowance"));
                    expensesCurrentRow.setAttribute("SubAllowanceDescrptn",
                                                    currentRow.getAttribute("FdaSubAllowanceDescription"));
                    expensesCurrentRow.setAttribute("Allowance",
                                                    currentRow.getAttribute("FdaAllowance"));
                    expensesCurrentRow.setAttribute("AllowanceDescrptn",
                                                    currentRow.getAttribute("FdaAllowanceDescription"));
                    expensesCurrentRow.setAttribute("CostCenter",
                                                    currentRow.getAttribute("FdaCostCenter"));
                    expensesCurrentRow.setAttribute("CostCenterDescrptn",
                                                    currentRow.getAttribute("FdaCostCenterDescription"));


                    detailedVO.insertRow(expensesCurrentRow);
                    detailedVO.setCurrentRow(expensesCurrentRow);
                    executeAAPFundingObject();
                }
            }

        }


    }

    public void onSelectCan(ValueChangeEvent evt) {
        _logger.info("Entering onSelectCan");
        RichSelectBooleanCheckbox selected =
            (RichSelectBooleanCheckbox)evt.getSource();
        String canRowKey = (String)selected.getAttributes().get("canRowKey");
        if ((Boolean)evt.getNewValue()) {
            rowKeyList.add(canRowKey);
        } else {
            rowKeyList.remove(canRowKey);
        }
        for (String r : rowKeyList) {
            System.err.println(r);
        }
    }


    public void launchBudgetRejectionPopup(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering launchBudgetRejectionPopup");
        OperationBinding insertComment =
            ADFUtils.findOperation("insertComment");
        insertComment.execute();
        ADFUtils.showPopup(budgetRejectPopup);
    }

    public void setBudgetRejectPopup(RichPopup budgetRejectPopup) {
        this.budgetRejectPopup = budgetRejectPopup;
    }

    public RichPopup getBudgetRejectPopup() {
        return budgetRejectPopup;
    }

    public void cancelBudgetRejectPopup(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering cancelBudgetRejectPopup");
        DCIteratorBinding commentIter =
            ADFUtils.findIterator("CommentsVOIterator");
        Row row = commentIter.getViewObject().getCurrentRow();
        if (row != null) {
            row.remove();
        }
        ADFUtils.hidePopup(budgetRejectPopup);
    }

    public void rejectAcquistionLeadRequest(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering rejectAcquistionLeadRequest");
        OperationBinding rejectAquisitionLeadRequest =
            ADFUtils.findOperation("rejectAquisitionLeadRequest");
        rejectAquisitionLeadRequest.execute();

        ADFUtils.hidePopup(budgetRejectPopup);
    }

    public void launchBudgetApprovalPopup(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering launchBudgetApprovalPopup");
        OperationBinding insertComment =
            ADFUtils.findOperation("insertNewComment");
        insertComment.execute();
        ADFUtils.showPopup(budgetApprovePopup);
    }

    public void setBudgetApprovePopup(RichPopup budgetApprovePopup) {
        this.budgetApprovePopup = budgetApprovePopup;
    }

    public RichPopup getBudgetApprovePopup() {
        return budgetApprovePopup;
    }

    public void cancelBudgetApprovePopup(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering cancelBudgetApprovePopup");
        DCIteratorBinding commentIter =
            ADFUtils.findIterator("CommentsVOIterator");
        Row row = commentIter.getViewObject().getCurrentRow();
        if (row != null) {
            row.remove();
        }
        ADFUtils.hidePopup(budgetApprovePopup);
    }

    public void approveBudgetInformation(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering approveBudgetInformation");
        OperationBinding approveOperation = null;

        approveOperation = ADFUtils.findOperation("approveBudgetRequest");
        approveOperation.execute();

        ADFUtils.hidePopup(budgetApprovePopup);
    }

    public boolean isLeadCenterBudgetUser() {
        _logger.info("Entering isLeadCenterBudgetUser");
        boolean isLeadBudgetUser = false;
        DCIteratorBinding submittedRequestIterator =
            ADFUtils.findIterator("SubmittedRequestsViewIterator");
        Row currentRow =
            submittedRequestIterator.getViewObject().getCurrentRow();
        String center = (String)currentRow.getAttribute("CenterId");

        String userCenter = ADFUtils.deriveCenterFromRole();
        if (center.equalsIgnoreCase(userCenter)) {
            isLeadBudgetUser = true;
        }
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("isMainCenterBudgetUser",
                                                                    isLeadBudgetUser);
        return isLeadBudgetUser;
    }

    public void checkLeadCenterBudgetUser() {
        isLeadCenterBudgetUser();

    }

    public void executeAAPFundingObject() {

        _logger.info("Entering executeAAPFundingObject");
        DCIteratorBinding requestIterator =
            ADFUtils.findIterator("SubmittedRequestsViewIterator");
        Row currentRow = requestIterator.getViewObject().getCurrentRow();
        if (currentRow != null) {
            String centItemNo = (String)currentRow.getAttribute("CtrLnItmNo");
            String reqType = null;
            int dash2ndIndex = StringUtils.nthOccurrence(centItemNo, '-', 1);
            String reqTypeSymbol =
                centItemNo.substring(dash2ndIndex, dash2ndIndex + 1);
            if (reqTypeSymbol.equals("C")) {
                reqType = "CONTRACTS";
            }
            if (reqTypeSymbol.equals("I")) {
                reqType = "IAA";
            }
            if (reqTypeSymbol.equals("G")) {
                reqType = "GRANTS";
            }
            OperationBinding executeFundingObjectAccessor =
                ADFUtils.findOperation("executeFundingObjectAccessor");

            executeFundingObjectAccessor.getParamsMap().put("type", reqType);
            executeFundingObjectAccessor.execute();
        }

    }

    public void refreshAAPFundingObject() {
        _logger.info("Entering refreshAAPFundingObject");
        DCIteratorBinding requestIterator =
            ADFUtils.findIterator("SubmittedRequestsViewIterator");
        Row currentRow = requestIterator.getViewObject().getCurrentRow();
        if (currentRow != null) {
            String centItemNo = (String)currentRow.getAttribute("CtrLnItmNo");
            String reqType = null;
            int dash2ndIndex = StringUtils.nthOccurrence(centItemNo, '-', 1);
            String reqTypeSymbol =
                centItemNo.substring(dash2ndIndex, dash2ndIndex + 1);
            if (reqTypeSymbol.equals("C")) {
                reqType = "CONTRACTS";
            }
            if (reqTypeSymbol.equals("I")) {
                reqType = "IAA";
            }
            if (reqTypeSymbol.equals("G")) {
                reqType = "GRANTS";
            }
            OperationBinding executeFundingObjectAccessor =
                ADFUtils.findOperation("refreshFundingObject");

            executeFundingObjectAccessor.getParamsMap().put("type", reqType);
            executeFundingObjectAccessor.execute();
        }
    }


}
