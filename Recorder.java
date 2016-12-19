import java.io.FileNotFoundException;
import java.util.Scanner;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.hatt.page.PageMonitor;
import com.hatt.recorder.UserEventTracer;

public class Recorder {

	private RemoteWebDriver driver = null;

	public static void main(String[] args) {
		new Recorder();
	}

	public Recorder() {
		try {
			record("http://codestroll.com");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void record(String url) throws FileNotFoundException {
		process(url);
	}

	public void process(String url) throws FileNotFoundException {
		openBrowser(url);

		UserEventTracer tracer = new UserEventTracer(driver, "script", 8188);
		tracer.start();

		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		scanner.close();
		PageMonitor.release();
		closeBrowser();
	}

	private void closeBrowser() {
		if (driver != null) {
			driver.close();
		}
	}

	public void openBrowser(String url) throws FileNotFoundException {
		System.setProperty("webdriver.gecko.driver", "F:/selenium/geckodriver-v0.11.1-win64/geckodriver.exe");
		System.setProperty("Webdriver.firefox.marionette", "true");
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		//firefoxProfile.setPreference("security.fileuri.strict_origin_policy", "false");
		driver = new FirefoxDriver(firefoxProfile);
		driver.get(url);
		//ChromeOptions options = new ChromeOptions();
        //options.addArguments("test-type");
        //options.addArgument("--start-maximized");
        //options.addArguments("--disable-web-security");
	}

}
