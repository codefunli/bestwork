package com.nineplus.bestwork.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Component
public class BestWorkBeanUtils {
	/**
	 * Copy properties from source to target
	 * 
	 * @param List<S>  sources
	 * @param Class<T> targetType
	 * @return List<T>
	 */
	public <S, T> List<T> copyListToList(List<S> sources, Class<T> targetType) {
		List<T> targets = new ArrayList<>();

		for (S s : sources) {
			T t = BeanUtils.instantiateClass(targetType);
			BeanUtils.copyProperties(s, t);
			targets.add(t);
		}

		return targets;
	}

	/**
	 * Copy properties from source to target
	 * 
	 * @param S        sources
	 * @param Class<T> targetType
	 * @return T
	 */
	public <S, T> T copyToObject(S sources, Class<T> targetType) {

		T t = BeanUtils.instantiateClass(targetType);
		BeanUtils.copyProperties(sources, t);

		return t;
	}

	public String turnJsonStringAddressToString(String jsonAddress) {
		String returnVal = "";
		if (!StringUtils.isEmpty(jsonAddress)) {
			JSONObject address = new JSONObject(jsonAddress);
			returnVal += (StringUtils.isEmpty(address.getString("details")) ? "" : address.getString("details") + ",")
					+ (StringUtils.isEmpty(address.getJSONObject("ward").getString("name")) ? ""
							: address.getJSONObject("ward").getString("name") + ",")
					+ (StringUtils.isEmpty(address.getJSONObject("district").getString("name")) ? ""
							: address.getJSONObject("district").getString("name") + ",")
					+ (StringUtils.isEmpty(address.getJSONObject("city").getString("name")) ? ""
							: address.getJSONObject("city").getString("name"));
		}
		return returnVal;
	}

	public String objectToJsonString(Object object) {
		String returnVal = "";
		if (object != null) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				returnVal = ow.writeValueAsString(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnVal;
	}

	public <T> List<T> convertToList(Set<T> set) {
		return set.stream().collect(Collectors.toList());
	}

}
