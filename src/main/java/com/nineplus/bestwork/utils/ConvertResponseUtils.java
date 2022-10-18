package com.nineplus.bestwork.utils;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class ConvertResponseUtils {

	public String convertResponseCompany(String item) {

		HashMap<String, String> itemCompany = new HashMap<>();
		String columnMapped = "";
		itemCompany.put("companyName", "company_name");
		itemCompany.put("city", "province_city");
		itemCompany.put("district", "district");
		itemCompany.put("ward", "ward");
		itemCompany.put("street", "street");
		itemCompany.put("telNo", "tel_no");
		itemCompany.put("startDate", "start_date");
		itemCompany.put("expireDate", "expired_date");
		itemCompany.put("status", "is_expired");
		if (itemCompany.containsKey(item)) {
			columnMapped = itemCompany.get(item);
		}
		return columnMapped;
	}

}
