package com.macro.parking.crawler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.enums.MessageType;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class WebCrawler {
	//WebDriver 설정
	private WebDriver driver;
	private WebElement element;
	
	private String iParkUrl;
	
	
	private String moduId;
	private String moduPw;
	private String moduUrl;
	
	private Map<String, By> infoMap;
	
	public  String webDriverId= "webdriver.chrome.driver";
	public  String webDriverPath = "/usr/local/bin/chromedriver.exe";
		
	

	public void setting() {
        //System Property SetUp
        System.setProperty(webDriverId, webDriverPath);

        //Driver SetUp
        ChromeOptions options = new ChromeOptions();
        options.setCapability("ignoreProtectedModeSettings", true);
        driver = new ChromeDriver(options);
    }

	  public List<CarInfoDto> getDataFromModu() {
          List<CarInfoDto> parkingLotDtos = new LinkedList<>();

	        try {
	            infoMap = new HashMap<String, By>();
	            infoMap.put("id", By.xpath("/html/body/form/div[1]/input"));
	            infoMap.put("pw", By.xpath("/html/body/form/div[2]/input"));
	            infoMap.put("btn", By.xpath("/html/body/form/button"));
	            load(moduUrl);
	            login(moduId, moduPw, infoMap);
	            //오늘 날짜만 보기
	            driver.findElement(By.xpath("/html/body/div/ui-view/partner/div[1]/div[1]/button[3]")).click();
	            Thread.sleep(2000);
	            List<WebElement> elements = null;
	            List<WebElement> btns = null;
	            WebElement nextBtn = null;
	            WebElement btn = null;
	            String startPage, prePage = "-1";
	            int cnt = 1;
	            parkingLotDtos = new LinkedList<>();
	            do{
	                btns = driver.findElements(By.cssSelector("nav > ul > li.ng-scope"));
	                nextBtn = driver.findElement(By.cssSelector("body > div > ui-view > partner > pagination > nav > ul > li:nth-child(7) > span"));
	                startPage = btns.get(0).findElement(By.tagName("a")).getText();
	                if(startPage.equals(prePage)) {
	                    break;
	                }

	                for(int idx = 0, fin = btns.size(); idx < fin; idx++) {
	                    btn = btns.get(idx);
	                    elements = driver.findElements(By.xpath("/html/body/div/ui-view/partner/table[2]/tbody/tr"));
	                    for(WebElement e :elements) {
	                        CarInfoDto dto = new CarInfoDto();
	                        
	        
	                    	//날짜
	                        String time =e.findElement(By.xpath("td[1]/div/span")).getText().replaceAll("\\n", " ");
	                        LocalDateTime localDateTime = LocalDateTime.parse(time, 
	     	                        		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	     	                dto.setDate(localDateTime);
	     	                
	     	                //장
	     	                String parkingLotName = e.findElement(By.xpath("td[2]/div/span/a")).getText();
	     	                dto.setParkingLotName(parkingLotName);
	                        
	     	               //차번호
	                        String[] carInfo = e.findElement(By.xpath("td[3]/div/span")).getText().split("\\n");
	                        String carNum = carInfo[0];
	                        dto.setCarNum(carNum);
	                        
	     	                //주차권
	     	                String ticketName = e.findElement(By.xpath("td[4]/div[2]/div[1]/span")).getText();
	     	                dto.setTicket(ticketName);
	     	                parkingLotDtos.add(dto);
	                    }
	                    if(idx + 1 < fin ) {
	                        //next page (idx page -> idx + 1 page)
	                        btns.get(idx + 1).findElement(By.tagName("a")).click();
	                        Thread.sleep(3000);
	                    }
	                }
	                // >> 버튼 클릭
	                prePage = startPage;
	                nextBtn.click();
	                Thread.sleep(1000);

	                //무한 루프 방지
	                cnt++;
	            }while(cnt < 100000);

	        } catch (Exception e) {
	            e.printStackTrace();
				return parkingLotDtos;
	        } finally {
	            driver.close();
	        }
	        
			return parkingLotDtos;
	    }
	   
	    public void addTicketByParkingLot(List<CarInfoDto> CarInfoList, String id, String pwd){
	    	try{
	            Map<String, By> infoMap = new HashMap<String, By>();
	            infoMap.put("id", By.id("id"));
	            infoMap.put("pw", By.id("password"));
	            infoMap.put("btn", By.id("login"));
	            load(iParkUrl);

	            driver.findElement(By.className("btn-skip")).click();
	            Thread.sleep(500);
	            driver.findElement(By.className("btn-menu-close")).click();
	            Thread.sleep(500);
	            String siteId = id;
	            String sitePw = pwd;
	            login(siteId, sitePw, infoMap);
	            
	            for(int idx = 0, fin = CarInfoList.size(); idx < fin; idx++) {
	            
	            	CarInfoDto carInfo  = CarInfoList.get(idx);
	            	//popp 제거
	            	deletePopUp();
	            
		            String carNum = carInfo.getCarNum();
		           if(isCarInParkingLot(carNum)) {
		               //System.out.println("차 있음");
		               driver.findElement(By.id("next")).click();
		               Thread.sleep(10000);

		               //주차권 구매 페이지
		               List<WebElement> buyedTickets = driver.findElements(By.xpath("//*[@id=\"myDcList\"]/tr"));
		               if(buyedTickets.size() > 0) {
		                   //System.out.println("이미 구매 했음");
		            	   carInfo.setMessage(MessageType.TICKET_EXIST_ERROR.getMessage());
		               } else {
		                   //주차권 구매
		                   List<WebElement> saleTickets = driver.findElements(By.xpath("//*[@id=\"productList\"]/tr["));
		                   for(WebElement ticket :saleTickets) {
		                       String ticketName = ticket.findElement(By.xpath("td[1]")).getText();
		                       System.out.println(ticketName);

		                       //주차권 구입 버튼
		                       WebElement buyBtn = ticket.findElement(By.xpath("td[3]/button"));
		                       buyBtn.click();

		                       //최종 확인 pop 승락
		                       Thread.sleep(1000);
		                       driver.findElement(By.id("popupOk")).click();
			            	   carInfo.setMessage(MessageType.SUCCESS.getMessage());

		                       break;
		                   }
		               }
		               //검색창 가기
		               driver.findElement(By.id("goMain")).click();
		               Thread.sleep(1000);
		           } else {
		        	   //치 없
	            	   carInfo.setMessage(MessageType.NO_CAR_ERROR.getMessage());
		               //검색창으로 되돌아 가기
		               driver.findElement(By.xpath("//*[@id=\"headerTitle\"]")).click();
		               Thread.sleep(2000);
		           }
	            }
	            

	        } catch(Exception e ) {
	            e.printStackTrace();
	        } finally {
	        	driver.close();
	        }
	    }

	    private void deletePopUp() throws InterruptedException {
	    	driver.findElement(By.xpath("//*[@id=\"gohome\"]")).click();
            Thread.sleep(500);

            driver.findElement(By.id("start")).click();
            Thread.sleep(500);
	    }

	    private boolean isCarInParkingLot(String carNum) throws Exception{
	        boolean flag = false;
	        //뒤에 번호 4자리만...
	        String fourNumOfCar = carNum.substring(carNum.length() - 4, carNum.length());
	        
	        driver.findElement(By.id("carNumber")).sendKeys(fourNumOfCar);
	        Thread.sleep(500);
	        driver.findElement(By.xpath("//*[@id=\"container\"]/section[2]/div[2]/div/button")).click();
	        Thread.sleep(4000);
	        
	        List<WebElement> trTags = driver.findElements(By.xpath("//*[@id=\"carList\"]/tr"));
	        for(WebElement trTag :trTags) {
	            String parkingCarNum = trTag.findElement(By.xpath("td[2]")).getText();
	            if(carNum.equals(parkingCarNum)) {
	                flag = true;
	                break;
	            }
	        }
	        return flag;
	    }

	    private void load(String url) {
	        driver.get(url);
	    }
	    private void login(String id, String pw, Map<String, By> infoMap) throws Exception{
	        //아이디 입력
	        // id 값으로도 찾을 수 있습니다.

	        element = driver.findElement(infoMap.get("id"));
	        // 크롤링으로 text를 입력하면 굉장히 빠릅니다, 인식하지 못한 상태에서 이벤트를 발생시키면, 제대로 작동하지 않기 때문에 thread sleep으로 기다려줍니다.
	        Thread.sleep(500);
	        element.sendKeys(id);
	        // 유저 정보를 담은 객체에서 아이디값을 가져와서 넣어주면 되겠죠-

	        //패스워드 입력 - 아이디와 마찬가지입니다.
	        element = driver.findElement(infoMap.get("pw"));
	        element.sendKeys(pw);


	        //로그인 버튼 클릭
	        element = driver.findElement(infoMap.get("btn"));
	        element.click();

	        Thread.sleep(6000);
	    }
}
