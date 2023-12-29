package com.mashreq.oa.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashreq.oa.dao.BulkPayementsDAO;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;



@Service
public class BulkPayementsServiceImpl implements BulkPayementsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BulkPayementsServiceImpl.class);
	
	@Autowired
	private BulkPayementsDAO bulkPayementsDAO;

//	@Override
//	public List<PaymentData> getBulkPayementsList() {
//		LOGGER.info("calling getBulkPaymentsList service method");
//		List<PaymentData> list=bulkPayementsDAO.getBulkPayementsList();
//		for(PaymentData pymdata:list) {
//			pymdata.setNamevalue(new NameValue(pymdata.getMatrixRefNo(),pymdata.getPymtReqId()));
//		}
//		
//		return list;
//	}
	
	
	@Override
	public List<PaymentData> getBulkPayementsList() {
		LOGGER.info("calling getBulkPaymentsList service method");
		List<PaymentData> list=bulkPayementsDAO.getBulkPayementsList();
		
		for(PaymentData pymtreq:list) {
		try {
		LOGGER.info("pymtreq.getInitiatorDate()"+pymtreq.getInitiatorDate());
		if(pymtreq.getInitiatorDate()!=null) {
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			String convertedDate=sdf.format(pymtreq.getInitiatorDate());
			LOGGER.info("Converted Format is:::"+convertedDate);
			pymtreq.setDisplayInitiatorDate(convertedDate);
		}
		}catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.info("Exception in getListData() ::: "+e.getCause());
			return null;
		}
			
	}
	return list;
	
}
	
	@Override
	public List<PaymentData> getBulkExceptions(PaymentSearchInput pymtSerachInput) {
		try{
			LOGGER.info("Calling getBulkExceptions() in BulkPayementsServiceImpl");	
			List<PaymentData> bulkList=bulkPayementsDAO.getBulkExceptions(pymtSerachInput);
			
			if(bulkList != null && bulkList.size()>0){
			for(PaymentData pymtData : bulkList){
				
			LOGGER.info("pymtData.getInitiatorDate()"+pymtData.getInitiatorDate());
			if(pymtData.getInitiatorDate()!=null) {
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			String convertedDate=sdf.format(pymtData.getInitiatorDate());
			LOGGER.info("Converted Format is:::"+convertedDate);
			pymtData.setDisplayInitiatorDate(convertedDate);
				
			}
			
		}
			return bulkList;
		}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.info("Exception in getBulkExceptions() in BulkPayementsServiceImpl ::: "+e.getCause());
			return null;
		}
		return null;
	}
	
}
