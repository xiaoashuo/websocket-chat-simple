package com.lovecyy.web.socket.utils;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;


/***
 * 根据不同的数据源id获取不同的jdbcTemplate
 * @author qsq
 *
 */
public class DBUtility {
	
	
	private static HashMap<String, JdbcTemplate> templateCache = new HashMap<String, JdbcTemplate>();
    private static Object syncobj = new Object();
    
	
	public static JdbcTemplate getJdbcTemplate(String dataSource){
		
		synchronized (syncobj) {
			if (dataSource == null) dataSource = "dataSource";
            if (templateCache.containsKey(dataSource)) {
                return templateCache.get(dataSource);
            }
            if(!SpringUtils.containsBean(dataSource)){
                dataSource = "dataSource";
                if (templateCache.containsKey(dataSource)) {
                    return templateCache.get(dataSource);
                }
            }
            DataSource ds = (DataSource) SpringUtils.getBean(dataSource);
            JdbcTemplate tp = null;
            try {
                tp = new JdbcTemplate();
                tp.setDataSource(ds);
            } catch (Exception e) {
                e.printStackTrace();
                return tp;
            }
            templateCache.put(dataSource, tp);
            return tp;
        }
	}
	
	public static JdbcTemplate getJdbcTemplate(){
		return getJdbcTemplate(null);
	}

}
