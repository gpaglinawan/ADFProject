package com.fda.aps.model.entity;

import oracle.adf.share.ADFContext;

import oracle.jbo.AttributeList;
import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSet;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityDefImpl;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.TransactionEvent;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Wed May 27 01:29:38 EDT 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class DetailedExpensesEntityImpl extends EntityImpl {
    private static EntityDefImpl mDefinitionObject;

    /**
     * Add locking logic here.
     */
    public void lock() {
        super.lock();
    }

    /**
     * Custom DML update/insert/delete logic here.
     * @param operation the operation type
     * @param e the transaction event
     */
    protected void doDML(int operation, TransactionEvent e) {
//        if (operation == DML_INSERT){
//            ADFContext ctx = ADFContext.getCurrent();
//            EntityDefImpl notificationDef = AapNotificationImpl.getDefinitionObject();
//            AapNotificationImpl newNotification = (AapNotificationImpl)notificationDef.createInstance2(getDBTransaction(), null);
//            newNotification.setRequestId(this.getRequestId());
//            newNotification.setCenterItemNo(this.getCenterItmNo());
//            newNotification.setSubmittedFrom(ctx.getSecurityContext().getUserName());
//            
//            String[] roles = ctx.getSecurityContext().getUserRoles();
//            RowSet securityMappingView = this.getSecurityGroupsMappingROView();
//            
//            
//            for (String role:roles){
//                securityMappingView.setNamedWhereClauseParam("bind_AcquisitionGroup", role);
//                securityMappingView.executeQuery();
//                Row securityMappingRow = securityMappingView.first();
//                if (securityMappingRow != null){
//                    String groupName = (String)securityMappingRow.getAttribute("BudgetGroupName");        
//                    newNotification.setSubmittedTo(groupName);
//                }
//            }
//            
//            
//            newNotification.setWfStatus("NEW");
//        }
        
        super.doDML(operation, e);
    }

    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        ExpensesId {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getExpensesId();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setExpensesId((Number)value);
            }
        }
        ,
        CenterItmNo {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getCenterItmNo();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setCenterItmNo((String)value);
            }
        }
        ,
        FundingObjCode {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getFundingObjCode();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setFundingObjCode((String)value);
            }
        }
        ,
        Fund {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getFund();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setFund((String)value);
            }
        }
        ,
        Allowance {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getAllowance();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setAllowance((String)value);
            }
        }
        ,
        SubAllowance {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getSubAllowance();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setSubAllowance((String)value);
            }
        }
        ,
        CostCenter {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getCostCenter();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setCostCenter((String)value);
            }
        }
        ,
        Cost {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getCost();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setCost((Number)value);
            }
        }
        ,
        RequestId {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getRequestId();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setRequestId((Number)value);
            }
        }
        ,
        AapRequests {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getAapRequests();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setAapRequests((AapRequestsImpl)value);
            }
        }
        ,
        SecurityGroupsMappingROView {
            public Object get(DetailedExpensesEntityImpl obj) {
                return obj.getSecurityGroupsMappingROView();
            }

            public void put(DetailedExpensesEntityImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(DetailedExpensesEntityImpl object);

        public abstract void put(DetailedExpensesEntityImpl object,
                                 Object value);

        public int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        public static final int firstIndex() {
            return firstIndex;
        }

        public static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        public static final AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }


    public static final int EXPENSESID = AttributesEnum.ExpensesId.index();
    public static final int CENTERITMNO = AttributesEnum.CenterItmNo.index();
    public static final int FUNDINGOBJCODE = AttributesEnum.FundingObjCode.index();
    public static final int FUND = AttributesEnum.Fund.index();
    public static final int ALLOWANCE = AttributesEnum.Allowance.index();
    public static final int SUBALLOWANCE = AttributesEnum.SubAllowance.index();
    public static final int COSTCENTER = AttributesEnum.CostCenter.index();
    public static final int COST = AttributesEnum.Cost.index();
    public static final int REQUESTID = AttributesEnum.RequestId.index();
    public static final int AAPREQUESTS = AttributesEnum.AapRequests.index();
    public static final int SECURITYGROUPSMAPPINGROVIEW = AttributesEnum.SecurityGroupsMappingROView.index();

    /**
     * This is the default constructor (do not remove).
     */
    public DetailedExpensesEntityImpl() {
    }


    /**
     * @return the definition object for this instance class.
     */
    public static synchronized EntityDefImpl getDefinitionObject() {
        if (mDefinitionObject == null) {
            mDefinitionObject = EntityDefImpl.findDefObject("com.fda.aps.model.entity.DetailedExpensesEntity");
        }
        return mDefinitionObject;
    }

    /**
     * Gets the attribute value for ExpensesId, using the alias name ExpensesId.
     * @return the ExpensesId
     */
    public Number getExpensesId() {
        return (Number)getAttributeInternal(EXPENSESID);
    }

    /**
     * Sets <code>value</code> as the attribute value for ExpensesId.
     * @param value value to set the ExpensesId
     */
    public void setExpensesId(Number value) {
        setAttributeInternal(EXPENSESID, value);
    }

    /**
     * Gets the attribute value for CenterItmNo, using the alias name CenterItmNo.
     * @return the CenterItmNo
     */
    public String getCenterItmNo() {
        return (String)getAttributeInternal(CENTERITMNO);
    }

    /**
     * Sets <code>value</code> as the attribute value for CenterItmNo.
     * @param value value to set the CenterItmNo
     */
    public void setCenterItmNo(String value) {
        setAttributeInternal(CENTERITMNO, value);
    }

    /**
     * Gets the attribute value for FundingObjCode, using the alias name FundingObjCode.
     * @return the FundingObjCode
     */
    public String getFundingObjCode() {
        return (String)getAttributeInternal(FUNDINGOBJCODE);
    }

    /**
     * Sets <code>value</code> as the attribute value for FundingObjCode.
     * @param value value to set the FundingObjCode
     */
    public void setFundingObjCode(String value) {
        setAttributeInternal(FUNDINGOBJCODE, value);
    }

    /**
     * Gets the attribute value for Fund, using the alias name Fund.
     * @return the Fund
     */
    public String getFund() {
        return (String)getAttributeInternal(FUND);
    }

    /**
     * Sets <code>value</code> as the attribute value for Fund.
     * @param value value to set the Fund
     */
    public void setFund(String value) {
        setAttributeInternal(FUND, value);
    }

    /**
     * Gets the attribute value for Allowance, using the alias name Allowance.
     * @return the Allowance
     */
    public String getAllowance() {
        return (String)getAttributeInternal(ALLOWANCE);
    }

    /**
     * Sets <code>value</code> as the attribute value for Allowance.
     * @param value value to set the Allowance
     */
    public void setAllowance(String value) {
        setAttributeInternal(ALLOWANCE, value);
    }

    /**
     * Gets the attribute value for SubAllowance, using the alias name SubAllowance.
     * @return the SubAllowance
     */
    public String getSubAllowance() {
        return (String)getAttributeInternal(SUBALLOWANCE);
    }

    /**
     * Sets <code>value</code> as the attribute value for SubAllowance.
     * @param value value to set the SubAllowance
     */
    public void setSubAllowance(String value) {
        setAttributeInternal(SUBALLOWANCE, value);
    }

    /**
     * Gets the attribute value for CostCenter, using the alias name CostCenter.
     * @return the CostCenter
     */
    public String getCostCenter() {
        return (String)getAttributeInternal(COSTCENTER);
    }

    /**
     * Sets <code>value</code> as the attribute value for CostCenter.
     * @param value value to set the CostCenter
     */
    public void setCostCenter(String value) {
        setAttributeInternal(COSTCENTER, value);
    }

    /**
     * Gets the attribute value for Cost, using the alias name Cost.
     * @return the Cost
     */
    public Number getCost() {
        return (Number)getAttributeInternal(COST);
    }

    /**
     * Sets <code>value</code> as the attribute value for Cost.
     * @param value value to set the Cost
     */
    public void setCost(Number value) {
        setAttributeInternal(COST, value);
    }

    /**
     * Gets the attribute value for RequestId, using the alias name RequestId.
     * @return the RequestId
     */
    public Number getRequestId() {
        return (Number)getAttributeInternal(REQUESTID);
    }

    /**
     * Sets <code>value</code> as the attribute value for RequestId.
     * @param value value to set the RequestId
     */
    public void setRequestId(Number value) {
        setAttributeInternal(REQUESTID, value);
    }

    /**
     * getAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param attrDef the attribute

     * @return the attribute value
     * @throws Exception
     */
    protected Object getAttrInvokeAccessor(int index,
                                           AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            return AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].get(this);
        }
        return super.getAttrInvokeAccessor(index, attrDef);
    }

    /**
     * setAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param value the value to assign to the attribute
     * @param attrDef the attribute

     * @throws Exception
     */
    protected void setAttrInvokeAccessor(int index, Object value,
                                         AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].put(this, value);
            return;
        }
        super.setAttrInvokeAccessor(index, value, attrDef);
    }

    /**
     * @return the associated entity AapRequestsImpl.
     */
    public AapRequestsImpl getAapRequests() {
        return (AapRequestsImpl)getAttributeInternal(AAPREQUESTS);
    }

    /**
     * Sets <code>value</code> as the associated entity AapRequestsImpl.
     */
    public void setAapRequests(AapRequestsImpl value) {
        setAttributeInternal(AAPREQUESTS, value);
    }


    /**
     * Gets the view accessor <code>RowSet</code> SecurityGroupsMappingROView.
     */
    public RowSet getSecurityGroupsMappingROView() {
        return (RowSet)getAttributeInternal(SECURITYGROUPSMAPPINGROVIEW);
    }

    /**
     * @param expensesId key constituent

     * @return a Key object based on given key constituents.
     */
    public static Key createPrimaryKey(Number expensesId) {
        return new Key(new Object[]{expensesId});
    }

    /**
     * Add attribute defaulting logic in this method.
     * @param attributeList list of attribute names/values to initialize the row
     */
    protected void create(AttributeList attributeList) {
        super.create(attributeList);
    }
}
