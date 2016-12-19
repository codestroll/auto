package com.hatt.page;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.hatt.recorder.UserEventTracer;

public class PageMonitor extends Thread {

	private String previousURL;
	private String currentURL;
	private String previousDOM;
	private String currentDOM;
	
	private RemoteWebDriver driver;
	private UserEventTracer tracer;

	public static boolean stop = false;
	public static int SLEEP = 2;
	
	public PageMonitor(RemoteWebDriver driver, UserEventTracer tracer) {
		this.previousURL = driver.getCurrentUrl();
		this.previousDOM = driver.getPageSource();
		this.driver = driver;
		this.tracer = tracer;
	}
	
	@Override
	public void run() {
		stop = false;
		System.out.println("PageMonitor thread started..");
		boolean pageChanged = false;
		while (!stop) {
			try {
				if (this.driver == null) {
					stop = true;
					pageChanged = false;
				} else {
					Thread.sleep(SLEEP * 1000);
					pageChanged = isPageChanged();
					if (pageChanged) {
						stop = true;
					}
				}
			} catch (InterruptedException e) {
				stop = true;
				pageChanged = false;
			}
		}

		if (pageChanged) {
			System.out.println("Page change detected.  Now testing time-to-time to know whether the new page is stable. ");
			//waitTillPageLoadingIsStable(driver);
			
			try {
				tracer.triggerPageChange();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isPageChanged() {
		boolean urlChanged = false;
		
		try {
			currentURL = this.driver.getCurrentUrl();
			currentDOM = this.driver.getPageSource();
			urlChanged = !previousURL.equalsIgnoreCase(this.currentURL);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		Set<String> oldTokens = new HashSet<String>();
		Set<String> newTokens = new HashSet<String>();
		oldTokens.addAll(Arrays.asList(previousDOM.split(" ")));
		newTokens.addAll(Arrays.asList(currentDOM.split(" ")));
		newTokens.retainAll(oldTokens);
		int percentMatch = newTokens.size() * 100 / oldTokens.size();
		if (percentMatch < 90) {
			urlChanged = true;
		}

		
		return urlChanged;
	}

	public static void release() {
		stop = true;
	}

}
