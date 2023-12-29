package com.mashreq.oa.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.AuditTrail;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;
import com.mashreq.oa.entity.User;

@Repository
public class IndividualPayementsDAOImpl implements IndividualPayementsDAO {

	private static final Logger logger = LoggerFactory.getLogger(IndividualPayementsDAOImpl.class);

	@Value("${DBAPPEND}")
	private String DBAPPEND;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJDBCTemplate;

	@Override
	public List<PaymentData> getIndividualPayementsList() {

		try {

			logger.info("calling getIndividualPayementsList() in IndividualPayementsDAOImpl");

			String query = "SELECT PYMT_REQ_ID, MATRIX_REF_NO, SUB_PRODUCT, DEBIT_ACCOUNT_NUMBER_DESC, BENEFICIARY_NAME, "
					+ " INITIATOR_DATE, PAYMENT_CURRENCY, INVOICE_AMOUNT, CUSTOMER_REFERENCE, INITIATOR_NAME_DATE_TIME, "
					+ "STATUS, REMARKS FROM " + DBAPPEND
					+ "oa_payment_requests WHERE payment_type='INDIVIDUAL' AND status='EXCEPTION'";

			List<PaymentData> indivDetails = jdbcTemplate.query(query,
					BeanPropertyRowMapper.newInstance(PaymentData.class));

			logger.info("Individual list is:: " + indivDetails);

			return indivDetails;
		} catch (Exception e) {
			logger.info("Exception in getIndividualPayementsList() in IndividualPayementsDAOImpl " + e.getCause());
			return null;
		}
	}

	@Override
	public List<AttachmentData> getDetails(Integer pymtReqId) {

		logger.info("Calling getDetails() in IndividualPaymentsDaoImpl");
		List<AttachmentData> indPymt = null;

		try {

			String sql = "SELECT payment.MATRIX_REF_NO as matrixRefNo, payment.MATRIX_REF_NO as strMatrixRefNo,payment.STATUS, payment.BIFURCATION, payment.REMARKS,"
					+ " attachments.ISSUANCE_AUTHORITY , attachments.TRADE_LICENSE_EXP_DATE,"
					+ "attachments.AUTO_RENEWAL,attachments.IS_GOVERNMENT_ENTITY,attachments.IS_INSURANCE_COMPANY,attachments.AGREEMENT_EXP_DATE, attachments.IS_EXCEPTIONAL_APPROVAL, "
					+ " payment.BUDGET_YEAR , attachments.INVOICE_DATE_YEAR , payment.INVOICE_AMOUNT , payment.SERVICE_CODE, "
					+ " payment.BUILDING_ID ,attachments.NO_PROPER_DOCUMENTS , payment.SUPPLIER_ID, "
					+ " payment.MGMT_COMP_ID from " + DBAPPEND + "oa_payment_requests payment , " + DBAPPEND
					+ "oa_attachments_data attachments, " + " " + DBAPPEND + "oa_management_companies management, "
					+ DBAPPEND + "OA_SUPPLIERS suppliers, " + DBAPPEND + "OA_BUILDINGS buildings "
					+ " where payment.ATTACHMENT_DATA_ID=attachments.ATTACHMENT_DATA_ID and payment.MGMT_COMP_ID=management.MGMT_COMP_ID "
					+ "  and payment.SUPPLIER_ID=suppliers.SUPPLIER_ID  "
					+ " and payment.BUILDING_ID= buildings.BUILDING_ID" + " and payment.PYMT_REQ_ID=" + pymtReqId;

			indPymt = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(AttachmentData.class));

			logger.info("Details fetched: " + indPymt);

		} catch (Exception e) {
			logger.info(" Inside catch block of getDetails() in IndividualPaymentsDao");
			logger.info(e.getMessage() + "==" + e.getCause());
		}
		return indPymt;
	}

	// this method belongs to exception update screen
	@Override
	public void updateAttachment(AttachmentData updateData) {

		try {
			logger.info("Calling updateAttachment in IndividualPaymentsDaoImpl");
			logger.info(">>>>>>><<<<<<<" + updateData);

			String sql = "SELECT ATTACHMENT_DATA_ID FROM " + DBAPPEND + "OA_PAYMENT_REQUESTS WHERE PYMT_REQ_ID="
					+ updateData.getPymtReqId() + "";

			Integer attachmentId = jdbcTemplate.queryForObject(sql, Integer.class);

			logger.info("attachmentId:>>>" + attachmentId);

			logger.info("update data mgmtcomid::" + updateData.getMgmtCompId());
			logger.info("update data supplierId::" + updateData.getSupplierId());
			logger.info("update data Bifurcation::" + updateData.getBifurcation());
			logger.info("update data Remarks::" + updateData.getRemarks());
			logger.info("update data ISGov::" + updateData.getIsGovernmentEntity());
			logger.info("update data IsIns::" + updateData.getIsInsuranceCompany());
			logger.info("update data AutoRenewal::" + updateData.getAutoRenewal());

			String updateQuery = "UPDATE " + DBAPPEND + "OA_ATTACHMENTS_DATA SET ISSUANCE_AUTHORITY=?,"
					+ "INVOICE_DATE_YEAR=?,INVOICE_AMOUNT=?,NO_PROPER_DOCUMENTS=?, TRADE_LICENSE_EXP_DATE=?, "
					+ "AGREEMENT_EXP_DATE=?, IS_GOVERNMENT_ENTITY=?, IS_INSURANCE_COMPANY=?, AUTO_RENEWAL=? , IS_EXCEPTIONAL_APPROVAL=? "
					+ "  WHERE ATTACHMENT_DATA_ID='" + attachmentId + "'";

			jdbcTemplate.update(updateQuery, updateData.getIssuanceAuthority(), updateData.getInvoiceDateYear(),
					updateData.getInvoiceAmount(), updateData.getNoProperDocuments(),
					updateData.getTradeLicenseExpDate(), updateData.getAgreementExpDate(),
					updateData.getIsGovernmentEntity(), updateData.getIsInsuranceCompany(), updateData.getAutoRenewal(),
					updateData.getIsExceptionalApproval());

			logger.info("Data Updated Successfully in OA_ATTACHMENT_DATA");

			String updatePymt = "UPDATE " + DBAPPEND
					+ "OA_PAYMENT_REQUESTS SET INVOICE_AMOUNT=? , SERVICE_CODE=?, BUDGET_YEAR=?,"
					+ " STATUS=? , BIFURCATION =?, REMARKS=? WHERE PYMT_REQ_ID=" + updateData.getPymtReqId() + "";

			jdbcTemplate.update(updatePymt, updateData.getInvoiceAmount(), updateData.getServiceCode(),
					updateData.getBudgetYear(), "IN-PROGRESS", updateData.getBifurcation(), updateData.getRemarks());

			logger.info("Data updated successfully in OA_PAYMENT_REQUESTS");
		} catch (Exception e) {
			logger.info("Exception in updateAttachment() in IndividualPaymentsDaoImpl " + e.getCause());
		}

	}

	@Override
	public Integer getAttachmentId(Integer pymtReqId) {

		try {
			logger.info("Inside getAttachmentId() in IndividualPaymentsDaoImpl");

			String sql = "SELECT ATTACHMENT_DATA_ID FROM " + DBAPPEND + "OA_PAYMENT_REQUESTS" + " WHERE PYMT_REQ_ID="
					+ pymtReqId + "";

			Integer attachmentId = jdbcTemplate.queryForObject(sql, Integer.class);
			return attachmentId;
		} catch (Exception e) {
			logger.info("Exception in getAttachmentId() in IndividualPaymentsDaoImpl " + e.getCause());
			return null;
		}
	}

	@Override
	public String getUserName(Integer pymtReqId) {

		try {

			logger.info("Inside getUserName() in IndividualPaymentsDaoImpl");

			String sql = "SELECT UPLOADED_BY FROM " + DBAPPEND + "OA_PAYMENT_REQUESTS" + " WHERE PYMT_REQ_ID="
					+ pymtReqId + "";

			String userName = jdbcTemplate.queryForObject(sql, String.class);
			return userName;
		} catch (Exception e) {
			logger.info("Exception in  getUserName() in IndividualPaymentsDaoImpl " + e.getCause());
			return null;
		}
	}

	@Override
	public void saveAuditDetails(AuditTrail audit) {

		logger.info("Inside saveAuditDetails in Individual Payments Dao");
		String fromscreen = "Updated From Screen";

		try {

			String insertSql = "INSERT INTO " + DBAPPEND
					+ "OA_AUDIT_TRAIL(FIELDNAME,OLDVALUE,NEWVALUE,UPDATEDBY,ID,PYMT_REQ_ID)"
					+ " VALUES(:FIELDNAME, :OLDVALUE, :NEWVALUE, :UPDATEDBY, :ID, :PYMT_REQ_ID)";

			MapSqlParameterSource source = new MapSqlParameterSource();

			source.addValue("FIELDNAME", audit.getFieldName());
			source.addValue("OLDVALUE", audit.getOldValue() != null ? audit.getOldValue() : null);
			source.addValue("NEWVALUE", audit.getNewValue() != null ? audit.getNewValue() : null);
			source.addValue("UPDATEDBY", audit.getUpdatedBy());
			source.addValue("ID", audit.getId());
			source.addValue("PYMT_REQ_ID", fromscreen);

			namedParameterJDBCTemplate.update(insertSql, source);
			logger.info("Data inserted into OA_AUDIT_TRAILS");

		} catch (Exception e) {
			logger.info("inside saveAuditDetails catch in DAOClass");
			logger.info(e.getMessage() + "==" + e.getCause());
		}

	}

	@Override
	public List<PaymentData> getIndividualExceptions(PaymentSearchInput pymtSerachInput) {

		try {
			logger.info("Calling getIndividualExceptions() in IndividualPayementsDAOImpl");

			String query = "SELECT PYMT_REQ_ID, MATRIX_REF_NO as matrixRefNo, MATRIX_REF_NO as strMatrixRefNo, SUB_PRODUCT, DEBIT_ACCOUNT_NUMBER_DESC, BENEFICIARY_NAME, "
					+ " INITIATOR_DATE, PAYMENT_CURRENCY, INVOICE_AMOUNT, CUSTOMER_REFERENCE, INITIATOR_NAME_DATE_TIME, "
					+ "STATUS, REMARKS FROM " + DBAPPEND
					+ "oa_payment_requests WHERE PAYMENT_TYPE='INDIVIDUAL' AND STATUS='EXCEPTION' "
					+ "AND MGMT_COMP_ID=" + pymtSerachInput.getMgmtCompId() + " AND BUILDING_ID="
					+ pymtSerachInput.getBuildingId();

			List<PaymentData> individualList = jdbcTemplate.query(query,
					BeanPropertyRowMapper.newInstance(PaymentData.class));

			logger.info("Individual Payment List fetched from DB:: " + individualList);

			if (individualList != null && individualList.size() > 0) {
				return individualList;
			}

		} catch (Exception e) {
			logger.info("Exception raised in getIndividualExceptions in IndividualPayementsDAOImpl :: " + e.getCause());
			return null;
		}
		return null;

	}

	@Override
	public String getStatusByPaymentreqID(Integer pymtReqId) {

		try {

			logger.info("Calling getStatusByPaymentreqID() in IndividualDao");
			String query = "SELECT STATUS FROM " + DBAPPEND + "OA_PAYMENT_REQUESTS WHERE PYMT_REQ_ID=" + pymtReqId;

			String status = jdbcTemplate.queryForObject(query, String.class);

			logger.info("Status is>>" + status);

			return status;

		} catch (Exception e) {
			logger.info("Exception raised in getStatusByPaymentreqID in IndividualPymtDaoImpl:: " + e.getCause());
			return null;
		}

	}

}
