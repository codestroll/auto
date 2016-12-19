package com.hatt.recorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.hatt.page.PageMonitor;
import com.hatt.webserver.LightWebServer;

public class UserEventTracer {

	private static LightWebServer server = null;
	private RemoteWebDriver driver;
	private String scriptFolder;
	private int port;
	private PageMonitor monitor;
	
	public UserEventTracer(RemoteWebDriver driver, String scriptFolder, int port) {
		this.driver = driver;
		this.scriptFolder = scriptFolder;
		this.port = port;
	}
	
	public void start() throws FileNotFoundException {
		attachJQuery(driver, scriptFolder);
		
		if (server == null) {
			server = new LightWebServer(port);
		}
		
		monitor = new PageMonitor(driver, this);
		monitor.start();
		
		System.out.println("Tracer component is ready.");
	}

	public void triggerPageChange() throws FileNotFoundException {
		start();
		System.out.println("page changed..");
	}

	public void attachJQuery(RemoteWebDriver driver, String folder) throws FileNotFoundException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		File scriptFolder = new File(folder);
		InputStream stream = new FileInputStream(scriptFolder.getAbsolutePath() + File.separator + "jquery.min.js");
		String jQuery = convertStreamToString(stream) + "\n";
		stream = new FileInputStream(scriptFolder.getAbsolutePath() + File.separator + "client-side.js");
		String brapJS = convertStreamToString(stream) + "\n";
		jse.executeScript(jQuery + brapJS);
		System.out.println("Injected script components..");
	}

	public static String convertStreamToString(InputStream inputStream) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(inputStream);
			return (scanner).useDelimiter("\\A").next();
		} catch (Exception e) {
				System.out.println(e);
		} finally {
			scanner.close();
		}
		return null;
	}

}

