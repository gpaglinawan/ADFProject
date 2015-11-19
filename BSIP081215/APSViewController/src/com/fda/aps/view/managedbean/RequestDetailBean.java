package com.fda.aps.view.managedbean;

import com.fda.aps.ADFUtils;

import com.fda.aps.DateUtils;
import com.fda.aps.JSFUtils;

import com.fda.aps.StringUtils;

import com.fda.aps.model.viewobject.RequestsViewImpl;

import com.fda.aps.model.viewobject.RequestsViewRowImpl;

import java.security.Principal;

import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.model.binding.DCIteratorBindingDef;
import oracle.adf.share.ADFContext;
import oracle.adf.share.logging.ADFLogger;
import oracle.adf.share.security.SecurityContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierNodeBinding;

import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.ViewObjectImpl;

import oracle.jbo.uicli.binding.JUCtrlListBinding;

import org.apache.myfaces.trinidad.event.PollEvent;

import weblogic.security.spi.WLSGroup;

public class RequestDetailBean {
    private RichPanelLabelAndMessage collabPanelLabel;
    private RichPanelFormLayout collabFormLayout;
    private RichInputText rejectReasonText;
    private RichPopup approveRequestPopup;
    private RichPopup rejectRequestPopup;
    private RichPopup empPopup;
    private List<Number> collabList;
    private RichTable dashboardTable;
    private List<SelectItem> fiscalYearList;
    
    private static ADFLogger _logger = 
            ADFLogger.createADFLogger(RequestDetailBean.class); 

    public RequestDetailBean() {
        super();
    }

    public String navigateTo() {
        _logger.info("Entering navigateTo");
        AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
        String comingFrom = (String)ctx.getPageFlowScope().get("comingFrom");
        String fromCreate = (String)ctx.getPageFlowScope().get("fromCreate");


        if ("Y".equals(fromCreate)) {
            DCIteratorBinding requestIterator =
                ADFUtils.findIterator("SubmittedRequestsViewIterator");
            Row currentRow = requestIterator.getCurrentRow();
            currentRow.remove();
        }

        if (comingFrom.equals("search")) {
            return "backToSearch";
        } else if (comingFrom.equals("dashboard")) {
            return "backToDashboard";
        } else if (comingFrom.equals("budget")) {
            return "backToBudget";
        } else {
            return "backToSummary";
        }
    }

    public void onValueChange(ValueChangeEvent evt) {
        _logger.info("Entering onValueChange");
        ADFUtils.setValueToEL("#{bindings.ActionTypeViewRO.inputValue}",
                              evt.getNewValue());


        Object newValue = evt.getNewValue();
        System.out.println(newValue);
        if (newValue != null) {
            Object actionType =
                JSFUtils.resolveExpression("#{bindings.ActionTypeViewRO.attributeValues[0]}");
            System.err.println(actionType);
            Map<String, Object> scope =
                AdfFacesContext.getCurrentInstance().getPageFlowScope();
            scope.put("reqType", actionType);
        }
    }

    public String createNewRequest() {
        // Add event code here...
        _logger.info("Entering createNewRequest");
        String reqType =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("reqType");
        String center =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("centerCode");

        String fiscalYear =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("fiscalYear");
        //        Object center = JSFUtils.resolveExpression("#{bindings.ReferenceCode}");

        if (reqType == null) {
            String msg =
                "Please select an action type when creating a new request";
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null,
                           new FacesMessage(FacesMessage.SEVERITY_ERROR, msg,
                                            null));

            return null;
        } else {
            OperationBinding insertNewRequest =
                ADFUtils.findOperation("createNewRequest");
            insertNewRequest.getParamsMap().put("actionType",
                                                reqType.substring(0, 1));
            insertNewRequest.getParamsMap().put("centerCode", center);
            insertNewRequest.getParamsMap().put("fiscalYear", fiscalYear);

            insertNewRequest.execute();

        }
        return "gotoDetail";
    }

    public void onCenterChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        _logger.info("Entering onCenterChanged");
        ADFUtils.setValueToEL("#{bindings.CenterItemViewRO.inputValue}",
                              valueChangeEvent.getNewValue());
        Object center =
            JSFUtils.resolveExpression("#{bindings.CenterItemViewRO.attributeValues[0]}");
        System.err.println(center);
        //        Object refCode = JSFUtils.resolveExpression("#{bindings.ReferenceCode}");
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("centerCode",
                                                                    center);
    }

    public String saveAndSubmitRequest() {
        // Add event code here...
        _logger.info("Entering saveAndSubmitRequest");
        boolean hasBudgetRole = false;
        boolean hasBudgetLeadRole = false;
        boolean hasAcqusitionLeadRole = false;
        DCIteratorBinding submittedReqIter =
            ADFUtils.findIterator("SubmittedRequestsViewIterator");

        Row currentRow = submittedReqIter.getCurrentRow();
        currentRow.setAttribute("SubmitFlag", "S");

        OperationBinding commit = ADFUtils.findOperation("Commit");
        commit.execute();


        //return "backToSummary";

        //        OperationBinding isBudgetLead =
        //                        ADFUtils.findOperation("isBudgetLead");
        //                    Object isBudLead =  isBudgetLead.execute();

        ADFContext context = ADFContext.getCurrent();
        SecurityContext securityContext = context.getSecurityContext();

        //**
        /* replace this with application role define in jazn
         *budgetLead = securityContext.isUserInRole("BUDGET_GROUP");
         * */

        hasBudgetRole = securityContext.isUserInRole("AAPBUDGETCREATE");
        hasBudgetLeadRole = securityContext.isUserInRole("AAPBUDGETAPPROVE");
        hasAcqusitionLeadRole = securityContext.isUserInRole("AAPACQNAPPROVE");
        ADFContext.getCurrent().getRequestScope().put("newReqId",
                                                      Math.random());
        ADFContext.getCurrent().getRequestScope().put("requestId",
                                                      JSFUtils.resolveExpression("#{bindings.RequestId.inputValue}"));
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("requestId",
                                                                    JSFUtils.resolveExpression("#{bindings.RequestId.inputValue}"));
        
        String comingFrom = (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("comingFrom");
        
        if ("budget".equals(comingFrom) || "search".equals(comingFrom)){
            return "backToDashboard";
        }


        if (hasBudgetRole) {
            // return "backToDashboard";
            if (hasBudgetLeadRole) {
                return "gotobudgetApproval";
            }
            return "gotobudget";
        }

        //ADFContext.getCurrent().getRequestScope().put("newReqId", JSFUtils.resolveExpression("#{bindings.RequestId.inputValue}"));

        return "backToDashboard";
    }

    public String saveRequest() {
        // Add event code here...
        _logger.info("Entering saveRequest");
        DCIteratorBinding submittedReqIter =
            ADFUtils.findIterator("SubmittedRequestsViewIterator");

        Row currentRow = submittedReqIter.getCurrentRow();
        currentRow.setAttribute("SubmitFlag", "I");

        OperationBinding commit = ADFUtils.findOperation("Commit");
        commit.execute();
        DCIteratorBinding inProcessRequestsIter =
            ADFUtils.findIterator("InProgressRequestsViewIterator");
        inProcessRequestsIter.getViewObject().executeQuery();

        return "backToDashboard";
    }

    public void onCollaborationChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        _logger.info("Entering onCollaborationChanged");
        ADFUtils.setValueToEL("#{bindings.CollaborationFlag1.inputValue}",
                              valueChangeEvent.getNewValue());
        //        System.err.println(valueChangeEvent.getNewValue());

        //        if (((Boolean)valueChangeEvent.getNewValue())){
        //            collabPanelLabel.setRendered(true);
        //            collabFormLayout.setRendered(true);
        //        }
        //        else{
        //            collabPanelLabel.setRendered(false);
        //            collabFormLayout.setRendered(false);
        //        }
        //        AdfFacesContext.getCurrentInstance().addPartialTarget(collabPanelLabel);
        //        AdfFacesContext.getCurrentInstance().addPartialTarget(collabFormLayout);

    }

    public void setCollabPanelLabel(RichPanelLabelAndMessage collabPanelLabel) {
        this.collabPanelLabel = collabPanelLabel;
    }

    public RichPanelLabelAndMessage getCollabPanelLabel() {
        return collabPanelLabel;
    }

    public void setCollabFormLayout(RichPanelFormLayout collabFormLayout) {
        this.collabFormLayout = collabFormLayout;
    }

    public RichPanelFormLayout getCollabFormLayout() {
        return collabFormLayout;
    }

    public void onRejectReasonChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        System.err.println(valueChangeEvent.getNewValue());
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("rejectReason",
                                                                    valueChangeEvent.getNewValue());
        if (((String)valueChangeEvent.getNewValue()).equals("C")) {
            rejectReasonText.setRendered(true);
        } else {
            rejectReasonText.setRendered(false);
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(rejectReasonText);
    }

    public void setRejectReasonText(RichInputText rejectReasonText) {
        this.rejectReasonText = rejectReasonText;
    }

    public RichInputText getRejectReasonText() {
        return rejectReasonText;
    }

    public void validateEstimatedValue(FacesContext facesContext,
                                       UIComponent uIComponent,
                                       Object object) {
        // Add event code here...
        _logger.info("Entering validateEstimatedValue");
        DCIteratorBinding dcIter =
            ADFUtils.findIterator("SubmittedRequestsViewIterator");
        RequestsViewImpl requestsViewImpl =
            (RequestsViewImpl)dcIter.getViewObject();


        DCIteratorBinding referrenceIter =
            ADFUtils.findIterator("ApsReferenceViewIterator");


        Row currentRow = requestsViewImpl.getCurrentRow();

        System.err.println(currentRow.getAttribute("TransProjectValueCode"));
        //        Object estimatedTotalVal =
        //            currentRow.getAttribute("EstimatedTotalProjValue");
        Object estimatedTotalVal = currentRow.getAttribute("EstimatedTotalProjValue");
        Key key = new Key(new Object[] { estimatedTotalVal });
        RowSetIterator rsi = referrenceIter.getRowSetIterator();
        Row row = rsi.findByKey(key, 1)[0];
        Object value = row.getAttribute("ReferenceCode");
        //        RequestsViewRowImpl currentRow =
        //            (RequestsViewRowImpl)requestsViewImpl.getCurrentRow();
        //        Row row =
        //            currentRow.getEstimatedTotalProjectValueLOV().getCurrentRow();
        //Object value = currentRow.getAttribute("TransProjectValueCode");
        //System.err.println(refCode);
        //        String value =
        //            JSFUtils.resolveExpressionAsString("#{bindings.TransProjectValueCode.inputValue}");
        System.err.println(uIComponent);
        System.err.println(object);
        System.err.println(value);
        //RichInputText estValue = (RichInputText)uIComponent;
        String numVal = (String)object;
        String s = numVal.replaceAll(",", "");
        String f = s.replace("$", "");
        double d = Double.parseDouble(f);
        Number numValue = null;
        try {
            numValue = new Number(d);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        } finally {

        }
        if (value != null) {

            if (value.equals("ETV1")) {
                if (numValue.compareTo("25000") == 1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "The Estimated value is out of range",
                                                                  null));
                }
            } else if (value.equals("ETV2")) {
                if (numValue.compareTo("50000") == 1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "The Estimated value is out of range",
                                                                  null));
                }
            } else if (value.equals("ETV3")) {
                if (numValue.compareTo("75000") == 1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "The Estimated value is out of range",
                                                                  null));
                }
            } else if (value.equals("ETV4")) {
                if (numValue.compareTo("100000") == 1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "The Estimated value is out of range",
                                                                  null));
                }
            } else if (value.equals("ETV5")) {
                if (numValue.compareTo("125000") == 1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "The Estimated value is out of range",
                                                                  null));
                }
            } else if (value.equals("ETV6")) {
                if (numValue.compareTo("150000") == 1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "The Estimated value is out of range",
                                                                  null));
                }
            } else if (value.equals("ETV7")) {
                if (numValue.compareTo("500000") == 1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "The Estimated value is out of range",
                                                                  null));
                }
            } else if (value.equals("ETV8")) {
                if (numValue.compareTo("1000000") == 1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "The Estimated value is out of range",
                                                                  null));
                }
            } else if (value.equals("ETV9")) {
                if (numValue.compareTo("5000000") == 1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "The Estimated value is out of range",
                                                                  null));
                }
            } else if (value.equals("ETV99")) {
                if (numValue.compareTo("10000000") == 1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "The Estimated value is out of range",
                                                                  null));
                }
            }
        }


    }

    public void onEstimatedProjectChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        _logger.info("Entering onEstimatedProjectChanged");
        ADFUtils.setValueToEL("#{bindings.TotalDlarsCrntYr.inputValue}",
                              valueChangeEvent.getNewValue());
    }

    public void validatePoPDates(FacesContext facesContext,
                                 UIComponent uIComponent, Object object) {
        // Add event code here...
        _logger.info("Entering validatePoPDates");
        Date startDate =
            (Date)JSFUtils.resolveExpression("#{bindings.ProjectPopStartDate.inputValue}");
        Date endDate = (Date)object;

        if (startDate == null) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                          "PoP Start Date is null",
                                                          null));
        }

        if (startDate.compareTo(endDate) == 1) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                          "PoP Start Date must be no later than PoP End Date",
                                                          null));
        }

    }

    public void onStartDateChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        ADFUtils.setValueToEL("#{bindings.ProjectPopStartDate.inputValue}",
                              valueChangeEvent.getNewValue());
    }


    public void validateRequiredAwardDate(FacesContext facesContext,
                                          UIComponent uIComponent,
                                          Object object) {
        // Add event code here...
        _logger.info("Entering validateRequiredAwardDate");
        Date awardDate = (Date)object;
        System.err.println(awardDate);
        if (awardDate.compareTo(Date.getCurrentDate()) == -1) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                          "Award Date must be a future date",
                                                          null));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
        Number fiscalYear =
            (Number)JSFUtils.resolveExpression("#{bindings.FiscalYear.inputValue}");

        OperationBinding startDate =
            ADFUtils.findOperation("getReferenceByCode");
        startDate.getParamsMap().put("code", "FISCALYEARSTART");
        String dateStart =
            (String)startDate.execute() + ", " + fiscalYear.add(-1);


        OperationBinding endDate =
            ADFUtils.findOperation("getReferenceByCode");
        endDate.getParamsMap().put("code", "FISCALYEAREND");
        String dateEnd = (String)startDate.execute() + ", " + fiscalYear;
        Date popStartDate = (Date)JSFUtils.resolveExpression("#{bindings.ProjectPopStartDate.inputValue}");

        try {
            java.util.Date ed = sdf.parse(dateEnd);
            java.util.Date sd = sdf.parse(dateStart);
            oracle.jbo.domain.Date jboStartDate =
                DateUtils.convertUtilToJboDate(sd);
            oracle.jbo.domain.Date jboEndDate =
                DateUtils.convertUtilToJboDate(ed);

            if (awardDate.compareTo(jboStartDate) == -1 ||
                awardDate.compareTo(jboEndDate) == 1) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              "Award Date must be within fiscal year range",
                                                              null));
            }
            
            if (popStartDate != null){
                if (popStartDate.compareTo(awardDate) == -1) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  "Required Award Date must be earlier than Pop Start Date",
                                                                  null));
                }
            }
        } catch (ParseException pe) {
            // TODO: Add catch code
            pe.printStackTrace();
            _logger.severe("Parsing error "+pe.getMessage());
        }
    }

    public void deleteSelectedPOC(ActionEvent actionEvent) throws Exception {
        // Add event code here...
        _logger.info("Entering deleteSelectedPOC");
        Number keyVal =
            (Number)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("pocid");
        DCIteratorBinding iter =
            ADFUtils.findIterator("PointOfContactViewIterator");
        //        RowSetIterator rowSetIter = iter.getRowSetIterator();
        //Key key = new Key(new Object[] {keyVal});
        //Row row = rowSetIter.findByKey(key, 1)[0];

        OperationBinding oper =
            ADFUtils.findOperation("setCurrentRowWithKeyValue");
        oper.getParamsMap().put("rowKey", keyVal);
        oper.execute();
        Row row = iter.getCurrentRow();
        row.remove();
        //rowSetIter.setCurrentRow(row);
        //rowSetIter.removeCurrentRow();
    }

    public boolean isAcquisitionLead() {
        String acquisitionLead = "false";

        OperationBinding isAcquisitionLead =
            ADFUtils.findOperation("isAcquisitionLead");
        acquisitionLead = (String)isAcquisitionLead.execute();
        return acquisitionLead.equalsIgnoreCase("true");
    }

    public boolean isBudgetLead() {
        boolean acquisitionLead = false;

        OperationBinding isAcquisitionLead =
            ADFUtils.findOperation("isBudgetLead");
        acquisitionLead =
                ((Boolean)isAcquisitionLead.execute()).booleanValue();
        return acquisitionLead;
    }

    public void cancelApproveRequest(ActionEvent actionEvent) {
        // Add event code here...

        DCIteratorBinding commentIter =
            ADFUtils.findIterator("CommentsVOIterator");
        Row row = commentIter.getViewObject().getCurrentRow();
        if (row != null) {
            row.remove();
        }
        ADFUtils.hidePopup(approveRequestPopup);
    }

    public void approveRequest(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering approveRequest");
        ADFContext context = ADFContext.getCurrent();
        SecurityContext securityContext = context.getSecurityContext();
        OperationBinding approveOperation = null;
        //if (securityContext.isUserInRole("ACQUISITION_GROUP")) {
        approveOperation = ADFUtils.findOperation("approveAcquisitionRequest");
        approveOperation.execute();
        //        //} else {
        //            approveOperation = ADFUtils.findOperation("approveBudgetRequest");
        //            approveOperation.execute();
        //        }

        ADFUtils.hidePopup(approveRequestPopup);
        //approveRequestPopup

    }

    public void setApproveRequestPopup(RichPopup approveRequestPopup) {
        this.approveRequestPopup = approveRequestPopup;
    }

    public RichPopup getApproveRequestPopup() {
        return approveRequestPopup;
    }

    public void launchApprovePopup(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering launchApprovePopup");
        OperationBinding insertComment =
            ADFUtils.findOperation("insertComment");
        insertComment.execute();
        ADFUtils.showPopup(approveRequestPopup);
    }

    public void launchRejectPopup(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering launchRejectPopup");
        OperationBinding insertComment =
            ADFUtils.findOperation("insertComment");
        insertComment.execute();
        ADFUtils.showPopup(rejectRequestPopup);
    }

    public void setRejectRequestPopup(RichPopup rejectRequestPopup) {
        this.rejectRequestPopup = rejectRequestPopup;
    }

    public RichPopup getRejectRequestPopup() {
        return rejectRequestPopup;
    }

    public void setCenterLineItemCode(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering setCenterLineItemCode");
        String comingFrom =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("comingFrom");
        DCIteratorBinding iter = null;
        if ("search".equalsIgnoreCase(comingFrom)) {
            iter = ADFUtils.findIterator("APSSearchViewIterator");
        } else {
            iter = ADFUtils.findIterator("SubmittedRequestsViewIterator");
        }
        Row currentRow = iter.getCurrentRow();
        String itemLineNo = (String)currentRow.getAttribute("CtrLnItmNo");
        int centerCodeIdx = StringUtils.nthOccurrence(itemLineNo, '-', 1);
        String centerItemCode =
            itemLineNo.substring(centerCodeIdx, centerCodeIdx + 1);
        int dashFirstIndex = itemLineNo.indexOf("-");
        String centerCode = itemLineNo.substring(0, dashFirstIndex);
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("lineItmCode",
                                                                    centerItemCode);
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("centerCode",
                                                                    centerCode);
    }

    public void setLineItemCode(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering setLineItemCode");
        String itemLineNo =
            (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("centerItemNo");
        int centerCodeIdx = StringUtils.nthOccurrence(itemLineNo, '-', 1);
        String centerItemCode =
            itemLineNo.substring(centerCodeIdx, centerCodeIdx + 1);
        int dashFirstIndex = itemLineNo.indexOf("-");
        String centerCode = itemLineNo.substring(0, dashFirstIndex);
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("lineItmCode",
                                                                    centerItemCode);
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("centerCode",
                                                                    centerCode);
    }

    public void onFiscalYearChanged(ValueChangeEvent evt) {
        // Add event code here...
        _logger.info("Entering onFiscalYearChanged");
//        ADFUtils.setValueToEL("#{bindings.FiscalYearRO.inputValue}",
//                              evt.getNewValue());
        Object fiscalYear = evt.getNewValue();
           // (String)JSFUtils.resolveExpression("#{bindings.FiscalYearRO.attributeValues[0]}");
        System.err.println("fiscalYear " + fiscalYear);
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("fiscalYear",
                                                                    fiscalYear);
    }

    public void setSearchLineItemCode(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering setSearchLineItemCode");
        setLineItemCode(actionEvent);
        OperationBinding navigateToRequestDetail =
            ADFUtils.findOperation("navigateToRequestDetail");
        navigateToRequestDetail.execute();
    }

    public void SubmitRequest(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering SubmitRequest");
        OperationBinding isAcquisitionLead =
            ADFUtils.findOperation("isAcquisitionLead");
        Object acquisitionLead = isAcquisitionLead.execute();
        System.err.println(acquisitionLead);
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("isAcquisitionLead",
                                                                    acquisitionLead);
    }

    public String navigateFromApprove() {
        // Add event code here...
        _logger.info("Entering navigateFromApprove");
        String navigateTo = "backToDashboard";
        SecurityContext secCtx = ADFContext.getCurrent().getSecurityContext();
        if (secCtx.isUserInRole("BUDGET_GROUP")) {
            if (!isBudgetLead())
                navigateTo = "gotobudget";

        }
        ADFContext.getCurrent().getRequestScope().put("newReqId",
                                                      Math.random());
        return navigateTo;
    }

    public void onCommoditySelected(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        _logger.info("Entering onCommoditySelected");
        //DCIteratorBinding commodityIterator =  ADFUtils.findIterator("CommodityBranchIterator");
        ADFUtils.setValueToEL("#{bindings.CommodityType.inputValue}",
                              valueChangeEvent.getNewValue());
        Object commdity =
            JSFUtils.resolveExpression("#{bindings.CommodityType.attributeValues[0]}");
        OperationBinding populateBranchDivisionInfo =
            ADFUtils.findOperation("populateBranchDivisionInfo");
        populateBranchDivisionInfo.getParamsMap().put("commodityType",
                                                      commdity);
        populateBranchDivisionInfo.execute();
    }

    public void onAnticpatedActionTypeSelected(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        _logger.info("Entering onAnticpatedActionTypeSelected");

        ADFUtils.setValueToEL("#{bindings.AnticipatedActionType.inputValue}",
                              valueChangeEvent.getNewValue());

//        Object actionType =
//            JSFUtils.resolveExpression("#{bindings.AnticipatedActionType.attributeValues[0]}");
//        System.err.println("action type " + actionType);

        //        DCIteratorBinding dcIter =
        //            ADFUtils.findIterator("SubmittedRequestsViewIterator");
        //        RequestsViewImpl requestsViewImpl =
        //            (RequestsViewImpl)dcIter.getViewObject();
        //
        //        Row currentRow = requestsViewImpl.getCurrentRow();
        //
        //        System.err.println(currentRow.getAttribute("TransSLANumdays"));
        //
        //        Object value = currentRow.getAttribute("TransSLANumdays");

        //        DCIteratorBinding dcIter =
        //            ADFUtils.findIterator("SlaCutoffdateCalcVOIterator");


//        OperationBinding populateBranchDivisionInfo =
//            ADFUtils.findOperation("populateDueDate");
//        populateBranchDivisionInfo.getParamsMap().put("sla", actionType);
//        populateBranchDivisionInfo.execute();
        calculateDueDate();
    }
    
    private void calculateDueDate(){
//        ADFUtils.setValueToEL("#{bindings.AnticipatedActionType.inputValue}",
//                              valueChangeEvent.getNewValue());
        _logger.info("Entering calculateDueDate");

        Object actionType =
            JSFUtils.resolveExpression("#{bindings.AnticipatedActionType.attributeValues[0]}");
        Object grantsType =
            JSFUtils.resolveExpression("#{bindings.GrantsCommodity.attributeValues[0]}");
        System.err.println("action type " + actionType);
        if (actionType != null){
            OperationBinding populateBranchDivisionInfo =
            ADFUtils.findOperation("populateDueDate");
            populateBranchDivisionInfo.getParamsMap().put("sla", actionType);
            populateBranchDivisionInfo.execute();
        }
        if (grantsType != null){
           OperationBinding populateBranchDivisionInfo =
           ADFUtils.findOperation("populateDueDate");
           populateBranchDivisionInfo.getParamsMap().put("sla", grantsType);
           populateBranchDivisionInfo.execute();
            
       }
    }


    public void onTotalDollarsChange(ValueChangeEvent valueChangeEvent) {
        // Add event code here...

    }

    public void cloneRequestDetail(ActionEvent actionEvent) {
        // Add event code here...
        _logger.info("Entering cloneRequestDetail");
        setCenterLineItemCode(actionEvent);
        Object reqId = ADFContext.getCurrent().getRequestScope().get("reqId");
        reqId =
                AdfFacesContext.getCurrentInstance().getPageFlowScope().get("reqId");
        OperationBinding cloneRequest = ADFUtils.findOperation("cloneRequest");
        cloneRequest.getParamsMap().put("requestId", reqId);
        cloneRequest.execute();

        //populateCollaboration();
    }

    public void populatePoc(ActionEvent actionEvent) {
        // Add event code here...

        _logger.info("Entering populatePoc");
        DCIteratorBinding iter =
            ADFUtils.findIterator("PointOfContactViewIterator");

        DCIteratorBinding empIter =
            ADFUtils.findIterator("AapEmployeeViewIterator");
        ViewObjectImpl empView = (ViewObjectImpl)empIter.getViewObject();
        Row currentEmpRow = empView.getCurrentRow();

        ViewObject pocObject = iter.getViewObject();
        Row pocRow = pocObject.getCurrentRow();

        pocRow.setAttribute("PocFirstName",
                            currentEmpRow.getAttribute("FirstName"));
        pocRow.setAttribute("PocLastName",
                            currentEmpRow.getAttribute("LastName"));
        pocRow.setAttribute("PocEmail", currentEmpRow.getAttribute("Email"));


        ADFUtils.hidePopup(empPopup);
    }

    public void setEmpPopup(RichPopup empPopup) {
        this.empPopup = empPopup;
    }

    public RichPopup getEmpPopup() {
        return empPopup;
    }

    public void launchEmpPopup(ActionEvent actionEvent) {
        // Add event code here...
        ADFUtils.showPopup(empPopup);
    }

    public void cloneSearchedRequest(ActionEvent actionEvent) {
        cloneRequestDetail(actionEvent);
    }

    public void rejectAacqusitionRequest(ActionEvent actionEvent) {
        // Add event code here...
        ADFUtils.findOperation("rejectAcquisitionRequest").execute();
        ADFUtils.hidePopup(rejectRequestPopup);
    }

    public void cancelRejectAacqusitionRequest(ActionEvent actionEvent) {
        // Add event code here...
        //ADFUtils.findOperation("rejectAcquisitionRequest").execute();
        ADFUtils.hidePopup(rejectRequestPopup);
    }


    public void deriveCenterFromRole() {
        _logger.info("Entering deriveCenterFromRole");
        List<String> roles = null;
        String center = null;
        Set<Principal> principals =
            ADFContext.getCurrent().getSecurityContext().getSubject().getPrincipals();
        if (principals != null) {
            roles = new ArrayList<String>();
            for (Principal role : principals) {
                if (role instanceof WLSGroup &&
                    (role.getName().contains("ACQ") ||
                     role.getName().contains("BDGT"))) {
                    int dashStartIdx = StringUtils.nthOccurrence(role.getName(), '-', 0);
                    int dashEndIdx = StringUtils.nthOccurrence(role.getName(), '-', 1);
                    center = role.getName().substring(dashStartIdx+1, dashEndIdx-1);
                    break;
                }
            }
        }
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("centerCode",
                                                                    center);
        ADFContext.getCurrent().getSessionScope().put("loggedInUserCenterId",
                                                      center);


    }

    public void onCollaborationSelected(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        _logger.info("Entering onCollaborationSelected");
        //ADFUtils.setValueToEL("#{bindings.CollaborationReference1.inputValue}", valueChangeEvent.getNewValue());
        ArrayList<Number> collabList =
            (ArrayList<Number>)valueChangeEvent.getNewValue();
        JUCtrlListBinding collaborationList =
            ADFUtils.getListBinding("CollaborationReference1");
        Object[] selectedData = collaborationList.getSelectedValues();

        DCIteratorBinding itrBinding =
            ADFUtils.findIterator("AapCollaborationViewIterator");

        if (itrBinding != null) {
            ViewObject vo = itrBinding.getViewObject();
            //Row[] rows = vo.getAllRowsInRange();
            RowSetIterator rows = vo.createRowSetIterator(null); 

            while (rows.hasNext()) {
               Row row = rows.next();
                row.remove();
            }
            rows.closeRowSetIterator();
            if (collabList != null && collabList.size() > 0) {
                for (Number collabId : collabList) {
                    Row row = vo.createRow();
                    row.setAttribute("CenterId", collabId);
                    vo.insertRow(row);
                    //vo.setCurrentRow(row);

                }
            }
        }
    }

    public void setCollabList(List<Number> collabList) {
        this.collabList = collabList;
    }

    public List<Number> getCollabList() {
        return collabList;
    }

    public void populateCollaboration() {
        //collabList = new ArrayList<Number>();
        _logger.info("Entering populateCollaboration");
        Object requestId =
            AdfFacesContext.getCurrentInstance().getPageFlowScope().get("requestId");


        OperationBinding getCollab =
            ADFUtils.findOperation("getCollaborationList");
        getCollab.getParamsMap().put("requestId", requestId);
        collabList = (ArrayList<Number>)getCollab.execute();

        if (((String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("comingFrom")).equalsIgnoreCase("backToSummary") ||
            "search".equalsIgnoreCase((String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("copyFrom"))) {
            DCIteratorBinding itrBinding =
                ADFUtils.findIterator("AapCollaborationViewIterator");

            if (itrBinding != null) {
                ViewObject vo = itrBinding.getViewObject();
                Row[] rows = vo.getAllRowsInRange();

                for (Row row : rows) {
                    row.remove();
                }

                for (Number collabId : collabList) {
                    Row row = vo.createRow();
                    row.setAttribute("CenterId", collabId);
                    vo.insertRow(row);
                    vo.setCurrentRow(row);

                }
            }

        }
    }

    public void setRequestTypeToFundingObject() {
        
        _logger.info("Entering setRequestTypeToFundingObject");
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
            OperationBinding setBindTypeToSlaCutoffDate =
                ADFUtils.findOperation("setBindTypeToSlaCutoffDate");
            ;


            setBindTypeToSlaCutoffDate.getParamsMap().put("actionType",
                                                          reqType);
            setBindTypeToSlaCutoffDate.execute();
        }

    }

    public void refreshDasboardByPoll(PollEvent pollEvent) {
        // Add event code here...
        _logger.info("Entering refreshDasboardByPoll");
        //System.err.println("poll listener");
        DCIteratorBinding dasboardIterator =
            ADFUtils.findIterator("AapNotificationVOIterator");
        ViewObject dashboardVO = dasboardIterator.getViewObject();
        dashboardVO.executeQuery();

        AdfFacesContext.getCurrentInstance().addPartialTarget(dashboardTable);
    }

    public void setDashboardTable(RichTable dashboardTable) {
        this.dashboardTable = dashboardTable;
    }

    public RichTable getDashboardTable() {
        return dashboardTable;
    }

    public void initializeAAPSearch() {
        _logger.info("Entering initializeAAPSearch");
        OperationBinding initializeSearch =
            ADFUtils.findOperation("initializeAAPSearch");
        initializeSearch.getParamsMap().put("value",
                                            ADFUtils.deriveCenterFromRole());
    }
    
    public boolean isCollaborationEnabled(){
        _logger.info("Entering isCollaborationEnabled");
        boolean collabDisabled = true;
        
        String fromCreate = (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("fromCreate");
        String createType = (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("createType");
        String submitFlag = (String)AdfFacesContext.getCurrentInstance().getPageFlowScope().get("submitFlag");
        
        if ("Y".equals(fromCreate) || "create".equals(createType) || "clone".equals(createType) || "I".equals(submitFlag)){
                return false;    
        }
        
        boolean isLeadBudgetUser = false;
        DCIteratorBinding submittedRequestIterator = ADFUtils.findIterator("SubmittedRequestsViewIterator");
        Row currentRow = submittedRequestIterator.getViewObject().getCurrentRow();
        if (currentRow != null) {
            String center = (String)currentRow.getAttribute("CenterId");
            boolean hasBudgetApprovalRole =
                ADFContext.getCurrent().getSecurityContext().isUserInRole("AAPBUDGETAPPROVE");

            String userCenter = ADFUtils.deriveCenterFromRole();
            if (center.equalsIgnoreCase(userCenter)) {
                isLeadBudgetUser = true;
            }
            AdfFacesContext.getCurrentInstance().getPageFlowScope().put("isMainCenterBudgetUser",
                                                                        isLeadBudgetUser);
            if (isLeadBudgetUser && hasBudgetApprovalRole) {
                return false;
            }
            if (!hasBudgetApprovalRole){
                return false;    
            }
        }
        
        return collabDisabled;
    }
    
    public boolean isLeadCenterBudgetUser(){
        _logger.info("Entering isLeadCenterBudgetUser");
        boolean isLeadBudgetUser = false;
        DCIteratorBinding submittedRequestIterator = ADFUtils.findIterator("SubmittedRequestsViewIterator");
        Row currentRow = submittedRequestIterator.getViewObject().getCurrentRow();
        String center = (String)currentRow.getAttribute("CenterId");
        boolean hasBudgetApprovalRole = ADFContext.getCurrent().getSecurityContext().isUserInRole("AAPBUDGETAPPROVE");
        
        String userCenter = ADFUtils.deriveCenterFromRole();
        if (center.equalsIgnoreCase(userCenter)){
            isLeadBudgetUser = true;
        }
        AdfFacesContext.getCurrentInstance().getPageFlowScope().put("isMainCenterBudgetUser", isLeadBudgetUser);
        return !(isLeadBudgetUser && hasBudgetApprovalRole);
    }

    public void setFiscalYearList(List<SelectItem> fiscalYearList) {
        this.fiscalYearList = fiscalYearList;
    }

    public List<SelectItem> getFiscalYearList() {
        _logger.info("Entering getFiscalYearList");
        if (fiscalYearList == null){
            DCIteratorBinding iter = ADFUtils.findIterator("StartDateSetupVOIterator");
            Row row = iter.getCurrentRow();
            Date startDate = (Date)row.getAttribute("EffectiveEndDate");
            int year = DateUtils.getJboDateYear(startDate);
            
            fiscalYearList = new ArrayList<SelectItem>();
            if (year > 0){
                
                SelectItem selectItem = new SelectItem(Integer.toString(year),Integer.toString(year));
                fiscalYearList.add(0, selectItem);
                int year1 = year + 1;
                selectItem = new SelectItem(Integer.toString(year1),Integer.toString(year1));
                fiscalYearList.add(1, selectItem);
                int year2 = year1 + 1;
                selectItem = new SelectItem(Integer.toString(year2),Integer.toString(year2));
                fiscalYearList.add(2, selectItem);
            }
            
        }
        return fiscalYearList;
    }
    
    public void onRequiredAwardDateChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        _logger.info("Entering onRequiredAwardDateChanged");
        System.err.println(valueChangeEvent.getNewValue());
        ADFUtils.setValueToEL("#{bindings.RequiredAwardDate.inputValue}",
                              valueChangeEvent.getNewValue());
        calculateDueDate();   
    }

    public void onGrantsTypeChanged(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        _logger.info("Entering onGrantsTypeChanged");
        ADFUtils.setValueToEL("#{bindings.GrantsCommodity.inputValue}",
                              valueChangeEvent.getNewValue());
        calculateDueDate();
    }

    public void validatePopStartDate(FacesContext facesContext,
                                     UIComponent uIComponent, Object object) {
        // Add event code here...
        _logger.info("Entering validatePopStartDate");
        Date popStartDate = (Date)object;
        System.err.println(popStartDate);
        Date awardDate = (Date)JSFUtils.resolveExpression("#{bindings.RequiredAwardDate.inputValue}");
        Date endDate = (Date)JSFUtils.resolveExpression("#{bindings.ProjectPopEndDate.inputValue}");
        if (awardDate != null){
            if (popStartDate.compareTo(awardDate) == -1) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              "PoP Star Date must be on or later than Award Date",
                                                              null));
            }
        }
        if (endDate != null){
            if (popStartDate.compareTo(endDate) == 1) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              "PoP End Date cannot be earlier than Pop Start Date",
                                                              null));
            }
        }
    }
}
