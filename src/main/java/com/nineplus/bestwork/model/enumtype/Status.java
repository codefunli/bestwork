package com.nineplus.bestwork.model.enumtype;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

@Getter
public enum Status {
	DELETE(0), DISABLE(1), ACTIVE(2);

	private static Logger log = LoggerFactory.getLogger(Status.class);
	private static Map<Integer, Status> lookup;
	private Integer value;

	Status(Integer value) {
		this.value = value;
	}

	static {
		try {
			Status[] vals = Status.values();
			lookup = new HashMap<Integer, Status>(vals.length);

			for (Status rpt : vals)
				lookup.put(rpt.getValue(), rpt);
		} catch (Exception e) {
			// Careful, if any exception is thrown out of a static block, the class
			// won't be initialized
			log.error("Unexpected exception initializing " + Status.class, e);
		}
	}

	public static Status fromValue(Integer value) {
		return lookup.get(value);
	}

	public static Integer getStatusEnum(String val) {
		return switch (val) {
		case "DISABLE" -> 1;
		case "DELETE" -> 0;
		case "ACTIVE" -> 2;
		default -> null;
		};
	}

	public Integer getValue() {
		return this.value;
	}

}
