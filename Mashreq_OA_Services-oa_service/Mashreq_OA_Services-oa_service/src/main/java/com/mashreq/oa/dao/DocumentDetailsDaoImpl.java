package com.mashreq.oa.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.DocumentDetails;
import com.mashreq.oa.utils.StringUtils;
@Repository
public class DocumentDetailsDaoImpl implements DocumentDetailsDao {
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentDetailsDaoImpl.class);
	
	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	@Value("${document.uploadedBy}")
	private String uploadedBy;
	
	@Value("${document.expireson}")
	private String expiresOn;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private StringUtils stringUtil;

	@Override
	public List<DocumentDetails> getDocumentDetails() {
		
		logger.info("calling getDocumentDetails() in DocumentDetailsDaoImpl");
		
		try{
		List<DocumentDetails> list=jdbcTemplate.query("SELECT * FROM "+DBAPPEND+"OA_DOCUMENTS",
			BeanPropertyRowMapper.newInstance(DocumentDetails.class));
		
		
		return list;
		}
		catch(Exception e)
		{
			logger.info("Exception in  getDocumentDetails() in DocumentDetailsDaoImpl "+e.getCause());
			return null;
		}
	}

	@Override
	public List<DocumentDetails> getDocumentPymt(int paymentReqID) {
		
		try{
		logger.info("calling getDocumentPymt() in DocumentDetailsDaoImpl");
		String select="SELECT DOCUMENT_ID,DOCUMENT_NAME,DOCUMENT_TYPE,EXPIRES_ON FROM "+DBAPPEND+"OA_DOCUMENTS WHERE PYMT_REQ_ID=?";
		Object[] arg= {paymentReqID};
		@SuppressWarnings("deprecation")
		List<DocumentDetails> doc=jdbcTemplate.query(select, arg,
				BeanPropertyRowMapper.newInstance(DocumentDetails.class));
		return doc;
		}
		catch(Exception e)
		{
			logger.info("Exception in  getDocumentPymt() in DocumentDetailsDaoImpl "+e.getCause());
			return null;
		}
	}

	@Override
	public void saveDocument(String fileName, String extension, String path, int paymentReqID) {
		try{

		logger.info("calling saveDocument() in DocumentDetailsDaoImpl");
		path=stringUtil.encodeData(path);
		String sequence="SELECT LEFT(CAST(RAND()*1000000000+9999999 AS int ),6) ";
		int documentId=jdbcTemplate.queryForObject(sequence, Integer.class);
		logger.info("Document ID is::"+documentId);
		int Document=jdbcTemplate.update("insert into "+DBAPPEND+"OA_DOCUMENTS(DOCUMENT_ID, DOCUMENT_NAME, DOCUMENT_TYPE, EXPIRES_ON, DOCUMENT_PATH,PYMT_REQ_ID,UPLOADED_BY) "
				+ "values("+documentId+",?,?,?,?,?,?)",fileName,extension,expiresOn,path,paymentReqID,uploadedBy);
		
		logger.info("Document:"+Document);
		}
		catch(Exception e)
		{
			logger.info("Exception in  saveDocument() in DocumentDetailsDaoImpl "+e.getCause());
			
		}
	}
	
	@Override
	public DocumentDetails getDocumentPath(long documentId) {

		try{
			logger.info("calling getDocumentPath() in DocumentDetailsDaoImpl");
			String select="SELECT DOCUMENT_PATH FROM "+DBAPPEND+"OA_DOCUMENTS WHERE DOCUMENT_ID=?";
			Object[] arg= {documentId};
			@SuppressWarnings("deprecation")
			DocumentDetails doc=jdbcTemplate.queryForObject(select, arg,
			BeanPropertyRowMapper.newInstance(DocumentDetails.class));
			return doc;
		}
		catch(Exception e)
		{
			logger.info("Exception in  getDocumentPath() in DocumentDetailsDaoImpl "+e.getCause());
			return null;
		}
	}
	
	@Override
	public String getStatusByPaymentreqID(int paymentReqID) {
		try{
			
			logger.info("Calling getStatusByPaymentreqID() in DocumentsDao");
			String query = "SELECT STATUS FROM "+DBAPPEND+"OA_PAYMENT_REQUESTS WHERE PYMT_REQ_ID="+paymentReqID;
			
			String status = jdbcTemplate.queryForObject(query,String.class);
			
			logger.info("Status is>>"+status);
			
			return status;
			
		}
		catch(Exception e)
		{
			logger.info("Exception raised in getStatusByPaymentreqID in DocumentsDao:: "+e.getCause());
			return null;
		}
	}

}
