package com.nineplus.bestwork.utils;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class ConvertResponseUtils {

	public String convertResponseCompany(String item) {
		HashMap<String, String> itemCompany = new HashMap<>();
		String columnMapped = "";
		itemCompany.put("id", "id");
		itemCompany.put("companyName", "company_name");
		itemCompany.put("city", "province_city");
		itemCompany.put("district", "district");
		itemCompany.put("ward", "ward");
		itemCompany.put("street", "street");
		itemCompany.put("telNo", "tel_no");
		itemCompany.put("startDate", "start_date");
		itemCompany.put("expireDate", "expired_date");
		itemCompany.put("status", "is_expired");
		itemCompany.put("email", "email");
		if (itemCompany.containsKey(item)) {
			columnMapped = itemCompany.get(item);
		}
		return columnMapped;
	}
	
	public String convertResponseUser(String item) {
		HashMap<String, String> itemUser = new HashMap<>();
		String columnMapped = "";
		itemUser.put("createBy", "create_by");
		itemUser.put("updateBy", "update_by");
		itemUser.put("firstName", "first_name");
		itemUser.put("lastName", "last_name");
		itemUser.put("userName", "user_name");
		itemUser.put("countLoginFailed", "count_login_failed");
		itemUser.put("telNo", "tel_no");
		if (itemUser.containsKey(item)) {
			columnMapped = itemUser.get(item);
		}
		return columnMapped;
	}

}
