package com.mashreq.oa.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mashreq.oa.dao.DocumentDetailsDao;
import com.mashreq.oa.entity.DocumentDetails;
import com.mashreq.oa.service.DocumentDetailsService;
import com.mashreq.oa.service.TokenService;

@RestController
@CrossOrigin
public class DocumentsDetailsController {

	@Value("${attachment.extensions}")
	private String listOfExt;
	
	@Autowired
	private DocumentDetailsDao  documentsDetailsDao;
	
	@Autowired
	public TokenService tokenService; 

	private static final Logger logger = LoggerFactory.getLogger(DocumentsDetailsController.class);
	
	@Autowired
	private DocumentDetailsService documentDetailsService;

	@GetMapping("/documents")
	public List<DocumentDetails> list() {
		
		logger.info("calling list() in DocumentDetails controller");
		
		try{
			List<DocumentDetails> docDetails = documentDetailsService.getDocumentDetails();
			return docDetails;
		}
		catch(Exception e)
		{
			logger.info("Exception in list() in DocumentDetails controller  "+e.getCause());
			return null;
		}
	}

	@GetMapping("/documents/pymtReqId/{paymentReqID}")
	public List<DocumentDetails> getDocumentPymt(@PathVariable int paymentReqID,@RequestHeader Map<String,String> headers) {
		
//		if(!tokenService.validateToken(headers)){
//			return null;
//		}
		try{
		logger.info("Calling getDocumentPymt() in DocumentDetailsController");
		List<DocumentDetails> docDetails = documentDetailsService.getDocumentPymt(paymentReqID);
		return docDetails;
		}
		catch(Exception e)
		{
			logger.info("Exception in getDocumentPymt() in DocumentDetails controller  "+e.getCause());
			return null;
		}
	}

	@GetMapping("/documents/{documentId}")
	public DocumentDetails getDocumentPath(@PathVariable String documentId) {
		
		try{
		logger.info("Calling getDocumentPath() in DocumentDetailsController");
		DocumentDetails path = documentDetailsService.getDocumentPath(Long.parseLong(documentId));
		return path;
		}
		catch(Exception e)
		{
			logger.info("Exception in getDocumentPath() in DocumentDetails controller  "+e.getCause());
			return null;
		}
	}

	@PostMapping("/saveDocument/{paymentReqID}")
	public String saveDocument(@RequestParam("file") MultipartFile file, @PathVariable int paymentReqID,@RequestHeader Map<String,String> headers)
			throws IOException {

		logger.info("calling saveDocument controller"+FilenameUtils.getExtension(file.getOriginalFilename()));
		logger.info("Documents paymentReqID :" + paymentReqID);
		if(!tokenService.validateToken(headers)){
			return null;
		}
		String status = documentsDetailsDao.getStatusByPaymentreqID(paymentReqID);
		logger.info("Status for sec in Document Service.."+status);
		
		if(!"EXCEPTION".equals(status) && !"PENDING".equals(status) && !"IN-PROGRESS".equals(status)){
			return "Invalid Data!";
		}

        try {
               boolean extFlag=false;
               String docExt=FilenameUtils.getExtension(file.getOriginalFilename());
               String[] extention=listOfExt.split(",");
               for(String ext: extention){
                     if(ext.equalsIgnoreCase(docExt)){
                            extFlag=true;
                     }
               }
               if(extFlag){
                     documentDetailsService.saveDocument(file, paymentReqID);
                     return "File Successfully uploaded";
               }else{
                     return "Please provide valied file!";
               }
        }catch (Exception e) {
			logger.info("Exception raise while uploading document:: "+e.getCause());
			return "Error: Document upload failed! please try again after sometime.";
		}
		
	}
}
