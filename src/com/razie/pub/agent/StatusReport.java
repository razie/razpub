package com.razie.pub.agent;

import razie.base.ActionItem;
import razie.draw.Drawable;
import razie.draw.widgets.NavLink;

/** status report */
public class StatusReport {
	public static enum Status {
		UNKNOWN, GREEN, YELLOW, RED
	};

	public StatusReport.Status status = Status.UNKNOWN;
	public Drawable details = null;
	public Throwable lastError = null;

	public StatusReport() {
	}

	public StatusReport(StatusReport.Status st) {
		this.status = st;
	}

	public Drawable drawBrief() {
		// TODO 2 PRES send to the detail pages - example of status redirect
		return new NavLink(new ActionItem(status.toString(), "STATUS_"
				+ status.toString()), ".");
	}

	public void ok() {
		lastError = null;
		status = Status.GREEN;
	}
}