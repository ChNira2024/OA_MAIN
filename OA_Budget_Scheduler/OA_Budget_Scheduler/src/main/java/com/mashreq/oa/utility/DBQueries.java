package com.mashreq.oa.utility;

import org.springframework.beans.factory.annotation.Value;

public class DBQueries {

    private DBQueries() {

    }
    @Value("${oa-mollakdb-package}")
	private static String mollakDBPackage;
    
    public static final String UpdateSchLockTableQuery ="update "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS set LAST_UPDATED_TIMESTAMP=systimestamp ,status=? where LAST_UPDATED_TIMESTAMP=TO_TIMESTAMP(?,'YYYY-MM-DD HH24: MI:SS:FF') and log_id=?";

    public static final String UpdateSchLockTableQueryUsingLogID ="update "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS set LAST_UPDATED_TIMESTAMP=systimestamp ,status=? where log_id=?";

}
