package com.nineplus.bestwork.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
public class DateUtils {

	private static final String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();
	public static final String YYYYMMDD = "yyyyMMdd";

	public String formatDate(Integer day, Integer month) {
		String fDay;
		String fMonth;

		if (day < 10) {
			fDay = "0" + day.toString();
		} else {
			fDay = day.toString();
		}
		if (month < 10) {
			fMonth = "0" + month.toString();
		} else {
			fMonth = month.toString();
		}
		return fMonth + fDay;
	}

	/**
	 * convert LocalDateTime to String
	 * 
	 * @param lcDateTime
	 * @return
	 */
	public static String convertLocalDateTimeToString(LocalDateTime lcDateTime) {

		if (ObjectUtils.isEmpty(lcDateTime)) {
			return StringUtils.EMPTY;
		}

		String formatDateTime = Strings.EMPTY;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYYMMDD_HHMMSS);
			formatDateTime = lcDateTime.format(formatter);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return formatDateTime;
	}

	public Long getStartOfDayTime() {
		LocalDate localDate = LocalDate.now();
		LocalDateTime startOfDay = localDate.atStartOfDay();
		Instant instant = startOfDay.atZone(ZoneOffset.UTC).toInstant();
		return instant.getEpochSecond();
	};

	public Long getEndOftDayTime() {
		LocalDate localDate = LocalDate.now();
		LocalDateTime endOfDay = LocalDateTime.of(localDate, LocalTime.MAX);
		Instant instant = endOfDay.atZone(ZoneOffset.UTC).toInstant();
		return instant.getEpochSecond();
	};

	public String convertToUTC(String time) {
		DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss:SSSXXXXX");
		OffsetDateTime odtInstanceAtOffset = OffsetDateTime.parse(time, DATE_TIME_FORMATTER);
		OffsetDateTime odtInstanceAtUTC = odtInstanceAtOffset.withOffsetSameInstant(ZoneOffset.UTC);
		return odtInstanceAtUTC.toInstant().toString();
	}

	public OffsetDateTime convertToOffSetDt(String time) {
		DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSz");
		OffsetDateTime odtInstanceAtOffsetST = OffsetDateTime.parse(time, DATE_TIME_FORMATTER);
		OffsetDateTime odtInstanceAtUTCSt = odtInstanceAtOffsetST.withOffsetSameInstant(ZoneOffset.UTC);
		return odtInstanceAtUTCSt;
	}

}
