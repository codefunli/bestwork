package com.nineplus.bestwork.model.enumtype;

import lombok.Getter;

@Getter
public enum ActionType {
	VIEW(0), ADD(1), EDIT(2), DELETE(3);

	private int value;

	private ActionType(int value) {
		this.value = value;
	}

	public static Enum<ActionType> getActionType(int val) {
		return switch (val) {
		case 1 -> ActionType.ADD;
		case 0 -> ActionType.VIEW;
		case 2 -> ActionType.EDIT;
		case 3 -> ActionType.DELETE;
		default -> null;
		};
	}
}
