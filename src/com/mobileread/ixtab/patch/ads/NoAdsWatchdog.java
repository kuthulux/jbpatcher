package com.mobileread.ixtab.patch.ads;

import java.util.Timer;
import java.util.TimerTask;

import com.amazon.kindle.util.lipc.LipcEvent;
import com.amazon.kindle.util.lipc.LipcEventListener;
import com.amazon.kindle.util.lipc.LipcException;
import com.amazon.kindle.util.lipc.helper.DelegatingLipcTarget;
import com.mobileread.ixtab.jbpatch.Log;

public class NoAdsWatchdog extends DelegatingLipcTarget implements
		LipcEventListener {

	/*
	 * This delay seems to be needed. In rare cases (e.g., "restart framework"
	 * while showing the screensaver), it may lead to a situation where the ads
	 * are showing up for short time, before being replaced by the "pristine"
	 * screensaver. Then again, simply setting this to 0 (sometimes) causes
	 * weird errors, resulting in a restart of the framework. So the former
	 * seems to be the preferrable case.
	 */
	private static final int FIXUP_DELAY_MS = 1000;

	private static final String PROPERTY_LOAD = "load";
	private static final String PROPERTY_UNLOAD = "unload";

	private static final String MODULE_ADS = "ad_screensaver";
	private static final String MODULE_NORMAL = "screensaver";

	private static final String NOADS_POWERD_LIPC_FAILED = "NOADS_POWERD_LIPC_FAILED";

	private static NoAdsWatchdog instance;

	private final SingleTaskSchedule checker = new SingleTaskSchedule();

	static synchronized void enable() {
		if (instance == null) {
			instance = new NoAdsWatchdog();
		}
	}

	private static com.amazon.ebook.util.log.Log amazonLog = null;

	private static com.amazon.ebook.util.log.Log getAmazonLog() {
		if (amazonLog == null) {
			synchronized (NoAdsWatchdog.class) {
				if (amazonLog == null) {
					amazonLog = com.amazon.ebook.util.log.Log
							.getInstance("NoAdsWatchdog");
				}
			}
		}
		return amazonLog;
	}

	private NoAdsWatchdog() {
		super("com.lab126.blanket");
		try {
			addLipcEventListener(this);
			checkInitialState();
		} catch (LipcException e) {
			log("E: No-ads watchdog failed to initialize:");
			e.printStackTrace(Log.INSTANCE);
		}
	}

	private void checkInitialState() throws LipcException {
		String state = getStringProperty(PROPERTY_LOAD);
		if (state == null) {
			log("W: Unexpected state in No-ads watchdog: \"" + PROPERTY_LOAD
					+ "\" property returned null");
			return;
		}
		if (state.indexOf(MODULE_ADS) != -1) {
			scheduleCheck();
		} else if (state.indexOf(MODULE_NORMAL) == -1) {
			// should not happen, but just in case.
			scheduleCheck();
		}
	}

	private void scheduleCheck() {
		scheduleCheckIn(FIXUP_DELAY_MS);
	}

	private void scheduleCheckIn(long delay) {
		// log("I: Scheduling check at "+ new
		// Date(System.currentTimeMillis()+delay));
		checker.schedule(new ModuleReplacerTask(), delay);
	}

	public void onLipcEvent(LipcEvent e) {
		scheduleCheck();
	}

	private void log(String msg) {
		Log.INSTANCE.println(msg);
	}

	private static class SingleTaskSchedule extends Timer {
		private TimerTask currentTask = null;

		public synchronized void schedule(TimerTask task, long delay) {
			// Log.INSTANCE.println("I: No-ads watchdog scheduled check at "+
			// new Date(System.currentTimeMillis()+delay));
			if (currentTask != null) {
				currentTask.cancel();
			}
			currentTask = task;
			super.schedule(task, delay);
		}

	}

	private class ModuleReplacerTask extends TimerTask {

		public void run() {
			try {
				// log("I: No-ads watchdog attempting to fix screensaver");

				if (getStringProperty(PROPERTY_LOAD).indexOf(MODULE_ADS) != -1) {
					com.amazon.ebook.util.log.Log log = getAmazonLog();
					boolean sleeping = com.amazon.kindle.restricted.ad.e.K(log,
							NOADS_POWERD_LIPC_FAILED);
					if (sleeping) {
						// wake up
						com.amazon.kindle.restricted.ad.e.B(log,
								NOADS_POWERD_LIPC_FAILED);
					}

					setProperty(PROPERTY_LOAD, MODULE_NORMAL);
					setProperty(PROPERTY_UNLOAD, MODULE_ADS);

					if (sleeping) {
						// put back to sleep
						com.amazon.kindle.restricted.ad.e.D(log,
								NOADS_POWERD_LIPC_FAILED);
					}
				} else {
					// log("I: replacement thread was pointless, ad screensaver isn't active.");
				}
			} catch (Throwable e) {
				log("E: No-ads watchdog failed to substitute ads screensaver:");
				e.printStackTrace(Log.INSTANCE);
			}
		}
	}

}
