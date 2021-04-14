package com.macro.parking.crawler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.enums.StatusCodeType;

import lombok.Getter;
import lombok.Setter;

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
    private WebDriverWait wait;
    private String driverName;
    private String path;
    
	public WebCrawler() {
	}
	
	public WebCrawler(WebDriver driver) {
		this.driver = driver;
	}
	
	public void setWebDriver() {
		System.setProperty(driverName, path);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1366,768");
//        options.addArguments("--headless");
        options.setProxy(null);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        try {
            /*
             *
             * @ params
             * option : headless
             *
             */
            driver = new ChromeDriver(capabilities);
            wait = new WebDriverWait(driver, 100);
        } catch (Exception e) {
        	e.printStackTrace();
        }
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
	            //driver.findElement(By.xpath("/html/body/div/ui-view/partner/div[1]/div[1]/button[3]")).click();
	            element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/ui-view/partner/div[1]/div[1]/button[3]")));
	            Thread.sleep(7000);
	            element.click();
	            Thread.sleep(7000);

	            List<WebElement> elements = null;
	            List<WebElement> btns = null;
	            WebElement nextBtn = null;
	            WebElement btn = null;
	            String startPage = "1", prePage = "-1";
	            int cnt = 1;
	            parkingLotDtos = new LinkedList<>();
	            btns = driver.findElements(By.cssSelector("nav > ul > li.ng-scope"));
	            // >> 5 페이지 간격으로 보여주는 단위
	            do{
	                int fin = btns.size();
	                int idx = 0;

	                do {
	                    if(idx < fin ) {
	                        //next page (idx page -> idx + 1 page)
	                        element = wait.until(ExpectedConditions.elementToBeClickable(btns.get(idx).findElement(By.tagName("a"))));
	                        element.click();
	                        Thread.sleep(3000);
	                    }
	                    //elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("/html/body/div/ui-view/partner/table[2]/tbody/tr")));
	                    String trXpath = "/html/body/div/ui-view/partner/table[2]/tbody/tr";
	                    elements = driver.findElements(By.xpath(trXpath));

	                    for(int rowIdx = 0; rowIdx < elements.size(); rowIdx++) {
	                        WebElement e = elements.get(rowIdx);
	                        String state = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(trXpath +"["+(rowIdx + 1) +"]"+"/td[7]"))).getText();


	                        if(state.equals("결제취소")) {
	                            continue;
	                        }
	                        
	                        CarInfoDto dto = new CarInfoDto();

	                        element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(trXpath + "["+(rowIdx + 1) +"]"+"/td[1]/div/span")));
	                        //날짜
	                        String time = element.getText().replaceAll("\\n", " ");
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


	                }while(++idx < fin);

	                //페이지 5 이
	                if(btns.size() < 5) {
	                    break;
	                }
	                nextBtn = driver.findElement(By.cssSelector("body > div > ui-view > partner > pagination > nav > ul > li:nth-last-child(1) > span"));
	                //nextBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("pagination li:nth-last-child(1) > span")));
	                prePage = startPage;

	                // >> 버튼 클릭
	                nextBtn.click();
	                Thread.sleep(3000);
	                btns = driver.findElements(By.cssSelector("nav > ul > li.ng-scope"));
	                startPage = btns.get(0).findElement(By.tagName("a")).getText();
	            }while(!startPage.equals(prePage));

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            driver.quit();
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
                    driver.findElement(By.id("next")).click();
                    
                    readyPageLoad();
                    
                    //주차권 구매 페이지
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("myDcList")));
                    Thread.sleep(1000);
                    List<WebElement> emptyCheck = driver.findElements(By.cssSelector("#myDcList > tr > .empty"));
                    if(emptyCheck.size() == 0) {
                        //구매했을 경
                        carInfo.setCode(StatusCodeType.TICKET_EXIST_ERROR.getCode());
                    } else {
                        //주차권 구매
                        String ticketXpath = "//*[@id=\"productList\"]/tr";
                        List<WebElement> saleTickets = driver.findElements(By.xpath(ticketXpath));
                        for(WebElement ticket :saleTickets) {
//                            String ticketName = ticket.findElement(By.xpath("td[1]")).getText();
//
//                            //주차권 구입 버튼
//                            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ticketXpath + "/td[3]/button"))).click();
////
//
//                            //최종 확인 pop 승락 2번
//                            wait.until(ExpectedConditions.elementToBeClickable(By.id("popupOk"))).click();
//                            wait.until(ExpectedConditions.elementToBeClickable(By.id("popupOk"))).click();
//
//                            carInfo.setCode(StatusCodeType.SUCCESS.getCode());
//
//                            break;
                        }
                    }
                    //검색창 가기
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("goMain")));
                    driver.findElement(By.id("goMain")).click();
                    Thread.sleep(1000);
                } else {
                    //치 없
                    carInfo.setCode(StatusCodeType.NO_CAR_ERROR.getCode());
                    //검색창으로 되돌아 가기
                    driver.findElement(By.xpath("//*[@id=\"headerTitle\"]")).click();
                    Thread.sleep(2000);
                }
            }


        } catch(Exception e ) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

	    private void deletePopUp() throws InterruptedException {
	        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"gohome\"]"))).click();
	        Thread.sleep(500);

	        driver.findElement(By.id("start")).click();
	    }
	    
	    private boolean isCarInParkingLot(String carNum) throws Exception{
	    	boolean flag = false;
	        String fourNumOfCar = carNum.substring(carNum.length() - 4, carNum.length());
	        //차번호 4자리 검색칸에 넣기
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("carNumber"))).sendKeys(fourNumOfCar);

	        //검색 버튼 클릭
	        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"container\"]/section[2]/div[2]/div/button"))).click();
	        
	        readyPageLoad();
	        
	        //다음 페이지에서 겁색된 차량 리스트를 받아오기
	        List<WebElement> trTags = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"carList\"]/tr")));
	        for(WebElement trTag :trTags) {
	            String parkingCarNum = trTag.findElement(By.xpath("td[2]")).getText();
	            if(carNum.equals(parkingCarNum) || 
	            		carNum.indexOf(parkingCarNum) >= 0 || 
	            		parkingCarNum.indexOf(carNum) >= 0) {
	                trTag.click();
	                flag = true;
	                break;
	            }
	        }
	        
	        return flag;
	    }
	    
	    private void readyPageLoad() {
	        wait.until(new ExpectedCondition<Boolean>() {
	            public Boolean apply(WebDriver wdriver) {
	                return ((JavascriptExecutor) driver).executeScript(
	                        "return document.readyState"
	                ).equals("complete");
	            }
	        });
	    }
	    
	    private void load(String url) {
	        driver.get(url);
	    }
	    
	    private void login(String id, String pw, Map<String, By> infoMap) throws Exception{
	    	try {
	    		   //아이디 입력
		        // id 값으로도 찾을 수 있습니다.
	            //driver.findElement(infoMap.get("id")).sendKeys(id);

		        element = wait.until(ExpectedConditions.visibilityOfElementLocated(infoMap.get("id")));
		        // 크롤링으로 text를 입력하면 굉장히 빠릅니다, 인식하지 못한 상태에서 이벤트를 발생시키면, 제대로 작동하지 않기 때문에 thread sleep으로 기다려줍니다.
		        element.sendKeys(id);
		        
		        // 유저 정보를 담은 객체에서 아이디값을 가져와서 넣어주면 되겠죠-
	            //driver.findElement(infoMap.get("pw")).sendKeys(pw + Keys.ENTER);

		        //패스워드 입력 - 아이디와 마찬가지입니다.
		        element = wait.until(ExpectedConditions.visibilityOfElementLocated(infoMap.get("pw")));
		        element.sendKeys(pw + Keys.ENTER);
	            //driver.findElement(infoMap.get("btn")).click();

		        //로그인 버튼 클릭
		        wait.until(ExpectedConditions.visibilityOfElementLocated(infoMap.get("btn"))).click();
	    	} catch(Exception e) {
	    		System.out.println("로그인 에");
	    		
	    	}
	       
	    }
	    
	    
		@Override
		public String toString() {
			return "WebCrawler [driver=" + driver + ", element=" + element + ", iParkUrl=" + iParkUrl + ", moduId="
					+ moduId + ", moduPw=" + moduPw + ", moduUrl=" + moduUrl + ", infoMap=" + infoMap + "]";
		}
		
	    
}
