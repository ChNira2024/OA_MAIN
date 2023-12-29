package com.mashreq.oa.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mashreq.oa.dao.DocumentDetailsDao;
import com.mashreq.oa.entity.DocumentDetails;
import com.mashreq.oa.entity.NameValueDoc;

@Service
public class DocumentDetailsServiceImpl implements DocumentDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentDetailsServiceImpl.class);
	
	@Autowired
	private DocumentDetailsDao  documentsDetailsDao;

	private static final String ALGORITHM = "RSA";

	private final static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvcSjalnThxc5gt5A39aqL1UhxMko4Z9E5L3K3nc/i4AzohSDi3oSI7d3hMb2Rf/H9iEDN7KASVzL79vlK7PfaWQJkks7GC4n/C6YqDo8bJJla4tTkPBluC8DPaboqaIjMUQ1cDu5SHMAG6SS2i06PyLZSrbGEYnxFIZucJo7JWpzGCb++5oGqvD3kuiSCeFqp0i8EtnmakGCJtJlhvnpB/9idrtiPOX1WY+/XC62ZUbpQa7e54c00/Lu6KYaKv6jAhbhl87HWmp7U8utNsTTk9pMY/4MhyN9QzFMVGKdMRyv27LbvHQIFFLbpYwAYFTbFV3NAiN0sfi2oNLk1DUORwIDAQAB";
	private final static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC9xKNqWdOHFzmC3kDf1qovVSHEySjhn0Tkvcredz+LgDOiFIOLehIjt3eExvZF/8f2IQM3soBJXMvv2+Urs99pZAmSSzsYLif8LpioOjxskmVri1OQ8GW4LwM9puipoiMxRDVwO7lIcwAbpJLaLTo/ItlKtsYRifEUhm5wmjslanMYJv77mgaq8PeS6JIJ4WqnSLwS2eZqQYIm0mWG+ekH/2J2u2I85fVZj79cLrZlRulBrt7nhzTT8u7ophoq/qMCFuGXzsdaantTy602xNOT2kxj/gyHI31DMUxUYp0xHK/bstu8dAgUUtuljABgVNsVXc0CI3Sx+Lag0uTUNQ5HAgMBAAECggEBAInsMRlK0AKPTq1e+6e0TVy5cyGjUqMpLtlRV/D4mqa5Ns3GOxVUU3rCDYvjT3rwvFSXCc+hXLv1RgO+voFU6jufCZXaN8kLQuR2uV0Ldn8yp6PST5o4HrYO9TwJ42/m980G1hAMWE3fx2RP6KvJ01uv6F31GWAF8cIJMpuEfRhjVV7kUocJaZ1H1blwvxj5nF8p4vMHB2irXIoSfp+iuXD3xdD+kbxq/ouqtuxjfO/QPIzdCKbqBpCBipcdI8n9WzZK7mYP6X+dy8cccgIHNybBQStqOQUftqxD4KOY55uIHn0BgOG1+w0MI/Qi0vRmsNg0WUcAPaTD7e74D++3+QECgYEA4rER6geNklYMlsANsoLJN6W8+TSdwiKsOEvhjM0McO79gZ4vksRwYcIWMQYQJWjJXC0MtyQ5OZUEkX4JCWMobkm1STl5nZQ2Rf1+636tYggEfZLaaUO48mEgNyczNhTD/v1l9a1yEZ5dZDMPyiV07xyCf2IqQMyrip7hLlON8X8CgYEA1k19jiRP9IK352Ut+2xN1HP37bWBvVkcWtpCEBgVEBze18UAkt9o9nKIw0UJcrL7YLlYAANaztEpNdA3Gw+4bK6LiMxpi7jr/9LF3w0AsTERW7DUrDXITAtJVqmms0a+BbcsfaV1NdIwtcpVThjbE/bv2PU6Gigx+9YORLFINzkCgYEAieAQeTqmzH4xoe6lZhFNuN7BFQD/gnf8LzFXuX9tNbLl1NQVMzru70ZQoPiDEX2uGrX7qdgKRg9we90gOelpScriy+p9IW5npCIN88VURu+Ba67J0IQ0FJcmNOVOrHHs00XjoY0gd77OJoc370bg3B2G8VsPP+I740/GvZpsFpkCgYAP44DqmAChlUuDSXomSPpgRRTdt/ZdjozOo54ASXjOUAWpo76OJShIFWfUanrv2RtQKY2/un/yE4nlpoFfbUP0MuC/jMBKjrRYrEzlY7ZobMXnsW2jMv2dvbx7Q2rLofQWwmT3D9xn2CSqZcz7VFZx3X4c7NmY9N/31wAJ0ccT6QKBgHnFWU/pudJwQ1fnYr/LZF554mFGjAz67JzG6KIudP/G8y6OwF7mjV+QFkhs/iyb9WYm5HxMH12oMVpkn/uE2ErnwNTtdj1ujNyC5L6eqL5PIqwWLzPSlEmEEOyI7JPAoytcLpa2FFQLT5RsEGsQktINJh1Lyyh4kNmuT9aFxsVA";
	
	@Override
	public List<DocumentDetails> getDocumentDetails() {
		logger.info("calling getDocumentDetails service method");
		return documentsDetailsDao.getDocumentDetails();
	}

	@Override
	public List<DocumentDetails> getDocumentPymt(int paymentReqID) {
		logger.info("calling getDocumentPymt service method");
		List<DocumentDetails> docDetails = documentsDetailsDao.getDocumentPymt(paymentReqID);
		
//		for(DocumentDetails details : docDetails){
//			String rsaPath = processRequest(details.getDocumentPath(), servletPath);
//			details.setNameValue(new NameValueDoc(details.getDocumentId(),rsaPath));
//		}
		return docDetails;
	}
	
	public String encoder(String param) {
		String encoded="";
		if(param != ""|| param !=null) {
			byte[] encodedbyte= Base64.encodeBase64(param.getBytes());
			encoded=new String(encodedbyte);
		}
		return encoded;
		
	}
//	@Value("${documentService.url}")
//	private String serviceUrl;
	 
	@Value("${document.file.localpath}")
	private String dir;
	
	@Value("${document.servlet.openingpath}")
	private String servletPath;
	@Autowired
	public HttpSession session;
	@Override
	public void saveDocument(MultipartFile file, int paymentReqID) {
		
		String status = documentsDetailsDao.getStatusByPaymentreqID(paymentReqID);
		logger.info("Status for sec in Document Service.."+status);
		
		if(!"EXCEPTION".equals(status) && !"PENDING".equals(status) && !"IN-PROGRESS".equals(status)){
			return ;
		}
		
		logger.info("calling saveDocument Service method"+paymentReqID);
		 //Path filepath = Paths.get(dir, file.getOriginalFilename());
		String filePaths=createDirectory(dir,String.valueOf(paymentReqID));
		String filePath = filePaths +"/"+ file.getOriginalFilename();
		File fileObj=new File(filePath);
		 try {
			file.transferTo(fileObj);
		} catch (IllegalStateException e) {
			logger.info("File transfer failed ::"+e.getCause());
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		//LOGGER.info("serviceurl:"+serviceUrl);
		//File File=filepath.toFile();
		String fileName=fileObj.getName();
		String normalPath=fileObj.getAbsolutePath();
		String extension = "";

		  int index = fileName.lastIndexOf('.');
		  if (index > 0) {
		      extension = fileName.substring(index + 1);
		  }
		  logger.info("normalPath:"+normalPath);
		 // String path=serviceUrl+encoder(normalPath);
		  String path=servletPath+normalPath;
		  logger.info("path:"+path);
		  path = path.replace('\\', '/');
				 
		  logger.info("fn:"+fileName+"   path:"+path+"  extension:"+extension);
		 
		documentsDetailsDao.saveDocument(fileName,extension,path,paymentReqID);
		
	}
	
	@Override
	public DocumentDetails getDocumentPath(long documentId) {
	logger.info("calling getDocumentPath service method");
	return documentsDetailsDao.getDocumentPath(documentId);
	}

	private String createDirectory(String basepath, String caseRefNo) {
		logger.info("basepath is" + basepath);
		String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		logger.info("currentDate::"+currentDate);
		File file1 = new File(basepath, currentDate);
		file1.mkdirs();
		File file2 = new File(file1.getPath(), caseRefNo);
		file2.mkdirs();
		return file2.getPath();
	}
	
	
	
	public String processRequest(String base64URL, String viewDocumentUrl) {

		String encodePath = base64URL.substring(base64URL.lastIndexOf("?") + 10);
		System.out.println("Encoded Path  is::" + encodePath);
		byte[] decodedByte = org.apache.commons.codec.binary.Base64.decodeBase64(encodePath.getBytes());
		System.out.println("Original Path  is::" + new String(decodedByte));
		byte[] encryptedData = null;
		try {
			encryptedData = encrypt(org.apache.commons.codec.binary.Base64.decodeBase64(publicKey), decodedByte);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String bPath = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(encryptedData);
		System.out.println("Base64 Encoded Path is::" + bPath);
		String finalUrl = viewDocumentUrl + bPath;
		System.out.println("Final URL is::" + finalUrl);
		return finalUrl;

	}
	
	private byte[] encrypt(byte[] publicKey, byte[] inputData) throws Exception {

		PublicKey key = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKey));

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] encryptedBytes = cipher.doFinal(inputData);

		return encryptedBytes;
	}


}
