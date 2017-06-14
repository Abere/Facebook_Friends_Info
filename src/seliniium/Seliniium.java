
package seliniium;
import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * @authors Chala,Abere,Getasew
 */
public class Seliniium extends JFrame{
     private final String PROFILE_DESTINATION_PATH = "C:\\Users\\Chala\\Documents\\NetBeansProjects\\Seliniium\\photo";
    WebDriver Wdname;
    ChromeOptions options;
    List<String> friend_list;
    List<String> image_list = new ArrayList<>();
    JButton blogin = new JButton("Login");
    JPanel panel = new JPanel();
    JLabel username = new JLabel("Username:");
    JLabel password1 = new JLabel("Password:");
    JTextField txtadmin = new JTextField(15);
    JPasswordField pass = new JPasswordField(15);
    

    Seliniium(){
        super("Login to facebook");
        setSize(400,300);
        setLocation(500,280);
        setResizable(false);
        panel.setLayout(null);

        username.setBounds(80, 65, 100, 20);
        password1.setBounds(80, 110, 100, 20);
        txtadmin.setBounds(155, 65, 150, 20);
        pass.setBounds(155, 110, 150, 20);
        blogin.setBounds(160, 180, 80, 20);

        panel.add(blogin);
        panel.add(username);
        panel.add(password1);
        panel.add(txtadmin);
        panel.add(pass);
        setVisible(true);
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Chala\\Desktop"
            + "\\3rd_second_sem\\SE\\new files\\lab materials\\chromedriver.exe");
        options = new ChromeOptions();
        options.addArguments("--disable-notifications");
         //options.addExtensions(new File("ultraChrome.crx"));
        Wdname = new ChromeDriver(options);
        friend_list = new ArrayList<>();
      
    }
   public void method4(String value1,String value2){
        Wdname.get("https://www.facebook.com");
        
         try{
            Thread.sleep(2000);
        }catch(Exception e){
        }
        
        WebElement phone = Wdname.findElement(By.id("email"));
        WebElement password = Wdname.findElement(By.id("pass"));
        WebElement button = Wdname.findElement(By.id("loginbutton"));
        phone.sendKeys(value1);
        password.sendKeys(value2);
        button.click();
        
        try{
            Thread.sleep(2000);
        }catch(Exception e){
       }
        
       WebElement profile =Wdname.findElement(By.xpath("//a[@title='Profile']"));
       profile.click();
       
        try{
            Thread.sleep(2000);
        }catch(Exception e){
       }
         
       WebElement friends =Wdname.findElement(By.xpath("//a[@data-tab-key='friends']"));
       friends.click();
       
       
      try{
            Thread.sleep(1000);
        }catch(Exception e){
       }
      //link of all friends 
       List<WebElement> friends1 = Wdname.findElements(By.xpath("//div[@class='fsl fwb fcb']/a"));
       
      
       System.out.println("Total friends is = " +friends1.size());
       int newFriends = friends1.size();
       Actions act = new Actions(Wdname);
       String s;
       while(newFriends != 20){
           WebElement lastFriend = friends1.get(friends1.size()-1);
           act.moveToElement(lastFriend).build().perform();
            try{
               Thread.sleep(1000);
            }catch(Exception e){ 
           }
           friends1 = Wdname.findElements(By.xpath("//div[@class='fsl fwb fcb']/a"));
           newFriends = friends1.size();
           System.out.println("Total friends is = " +friends1.size());
       }
       
        for(int i=0;i<friends1.size();i++){
          s = friends1.get(i).getAttribute("href");
          friend_list.add(s);       
       }
        
        PrintWriter pw2 = null;
        String name[];
        StringBuilder friend_detail = new StringBuilder();
        friend_detail.append("Name, \tBirthday,\t Gender,\t  InterestedIn,\t  Language,\t  Religion,\t  Politics\n");
        for (int i=0;i<friend_list.size();i++) {
            Map<String, String> friend_info = SingleProfile(friend_list.get(i));
            //System.out.println("profile link: " + friend_info.get("profile_link"));
            try{
                name = friend_info.get("name").split("\n");
                saveImage2(friend_info.get("profile_link"),name[0]);
            }catch(Exception e){
            
            }
            if(friend_info.get("profile_link") instanceof String)
                image_list.add(friend_info.get("profile_link"));
            try {
            pw2 = new PrintWriter(new File("FriendsInfo.csv"));
           }catch (FileNotFoundException e) {
              e.printStackTrace();
           }
            name = friend_info.get("name").split("\n");
            friend_detail.append(name[0]);
            friend_detail.append("\t");
            friend_detail.append(friend_info.get("all_info"));
            friend_detail.append("\n");
           
        }
        System.out.println(friend_detail.toString());
        pw2.write(friend_detail.toString());
        pw2.close();
        //This is path for the friends in the chat box, but its saying not able to find element
      System.out.println("FINAL Total friends is = " +friends1.size());          
   }
   protected Map<String, String> SingleProfile(String account_link){
        //WebDriver Wdname = new ChromeDriver();
        Wdname.get(account_link);
        Map<String, String> profile_info =  new HashMap<>();   
        
        profile_info.put("name", Wdname.findElement(By.cssSelector("#fb-timeline-cover-name")).getText());
        profile_info.put("profile_link", Wdname.findElement(By.cssSelector("img.profilePic.img")).getAttribute("src"));
        
        StringBuilder add_info = new StringBuilder();
        //WebElement friends =Wdname.findElement(By.xpath("//a[@data-tab-key='friends']"));
        try{
               Thread.sleep(1000);
            }catch(Exception e){ 
           }
       Wdname.findElement(By.xpath("//a[@data-tab-key='about']")).click();
        try{
               Thread.sleep(1000);
            }catch(Exception e){ 
           }
        Wdname.findElement(By.xpath("//a[@data-testid='nav_contact_basic']")).click();
        try{
               Thread.sleep(2000);
            }catch(Exception e){ 
           }
        List<WebElement> additional_info = Wdname.findElements(By.cssSelector("._4bl7 #u_jsonp_2_0 #pagelet_basic ._4qm1 .uiList.fbProfileEditExperiences._4kg._4ks li "
                + ".clearfix._ikh ._4bl7._pt5 .clearfix div ._50f4"));
        try{
               Thread.sleep(1000);
            }catch(Exception e){ 
           }
        additional_info.forEach((webElement) -> {
            add_info.append(",").append(webElement.getText().replaceAll(","," ") );
        });
               
        profile_info.put("all_info", add_info.toString());
        return profile_info;
    }
 
  public void saveImage2(String link,String name) throws MalformedURLException, IOException{
        URL url = new URL(link);
        BufferedImage img = ImageIO.read(url);
        File file = new File(PROFILE_DESTINATION_PATH+"\\"+name+".jpg");
        ImageIO.write(img, "jpg", file);
  }
   
    public static void main(String[] args) throws InterruptedException, IOException, MalformedURLException, AWTException{
        // TODO code application logic here
        try{
       Seliniium sel = new Seliniium();
       sel.setVisible(true);
       sel.buttonAction();
       
     }
        catch(Exception e){
            
        }
    }
   
   public void buttonAction(){
   blogin.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                 String value1=txtadmin.getText();
                 String value2=pass.getText();;
                  Seliniium sel = new Seliniium();
                 sel.method4(value1,value2);
                 //setVisible(false);
            }
            });
   }
}