import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
 
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
 
public class Class1 {
    public static void main(String[] args) throws InterruptedException {
        // Set up WebDriver
        WebDriver driver = new ChromeDriver();
 
        try {
            // Navigate to the login page
            driver.get("https://kommune.mainmanager.is/mmv2/MMV2Login.aspx");
 
            // Perform login
            driver.findElement(By.id("lgnUserLogin_UserName")).sendKeys("NAV");
            driver.findElement(By.id("lgnUserLogin_Password")).sendKeys("Testing@!123");
            driver.findElement(By.id("lgnUserLogin_Login")).click();
 
            // Wait for login to complete
            Thread.sleep(5000);
 
            // Fetch cookies
            Set<Cookie> cookies = driver.manage().getCookies();
 
            // Print specific cookie value
            String cookieName = "ASP.NET_SessionId"; // Specify the cookie name you're looking for
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    System.out.println("Exact Cookie (" + cookieName + "): " + cookie.getValue());
                    break;
                }
            }
 
            // Save cookies to a file
            // Change the path to ensure write permissions
            String cookiesFilePath = "C:\\Users\\suhsh\\eclip\\Project1\\src\\cookies.json";  // Changed to target file path
            saveCookiesToFile(cookies, cookiesFilePath);
 
            System.out.println("Cookies have been saved to cookies.json");
 
            // Commit and push the changes to Git
            commitAndPushToGit(cookiesFilePath);
 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Quit the driver
            driver.quit();
        }
    }
 
    /**
     * Saves cookies to a JSON file.
     *
     * @param cookies the set of cookies to save
     * @param filePath the path to save the JSON file
     */
    private static void saveCookiesToFile(Set<Cookie> cookies, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write("[");
 
            int count = 0;
            for (Cookie cookie : cookies) {
                fileWriter.write("{");
                fileWriter.write("\"name\": \"" + cookie.getName() + "\", ");
                fileWriter.write("\"value\": \"" + cookie.getValue() + "\", ");
                fileWriter.write("\"domain\": \"" + cookie.getDomain() + "\", ");
                fileWriter.write("\"path\": \"" + cookie.getPath() + "\", ");
                fileWriter.write("\"secure\": " + cookie.isSecure() + ", ");
                fileWriter.write("\"httpOnly\": " + cookie.isHttpOnly());
                if (cookie.getExpiry() != null) {
                    fileWriter.write(", \"expiry\": \"" + cookie.getExpiry().toString() + "\"");
                }
                fileWriter.write("}");
                if (++count < cookies.size()) {
                    fileWriter.write(", ");
                }
            }
 
            fileWriter.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * Automates the process of committing and pushing changes to Git.
     *
     * @param filePath the path to the file that was updated
     */
    private static void commitAndPushToGit(String filePath) {
        try {
            // Repository directory path (Ensure this points to the correct repo path)
            String repoDirectory = "C:\\Users\\suhsh\\eclip\\Project1\\src";  // Adjust the repo directory
            // Ensure safe directory configuration for Git
            ProcessBuilder configSafeDir = new ProcessBuilder(
                    "cmd", "/c", "git config --global --add safe.directory \"" + repoDirectory + "\"");
            configSafeDir.inheritIO().start().waitFor();
 
            // Navigate to the repository and add the updated cookies file
            ProcessBuilder addProcess = new ProcessBuilder(
                    "cmd", "/c", "cd \"" + repoDirectory + "\" && git add \"" + filePath + "\"");  // Specified cookies.json file
            addProcess.inheritIO().start().waitFor();
 
            // Commit the changes
            ProcessBuilder commitProcess = new ProcessBuilder(
                    "cmd", "/c", "cd \"" + repoDirectory + "\" && git commit -m \"Updated cookies.json with new cookie value\"");
            commitProcess.inheritIO().start().waitFor();
 
            // Push the changes to the repository
            ProcessBuilder pushProcess = new ProcessBuilder(
                    "cmd", "/c", "cd \"" + repoDirectory + "\" && git push origin main");
            pushProcess.inheritIO().start().waitFor();
 
            System.out.println("Changes have been committed and pushed to the Git repository.");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}