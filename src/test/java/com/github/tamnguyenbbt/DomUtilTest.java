package com.github.tamnguyenbbt;

import com.github.tamnguyenbbt.dom.DomUtil;
import com.github.tamnguyenbbt.dom.ElementInfo;
import com.github.tamnguyenbbt.exception.*;
import com.xiaoleilu.hutool.http.HttpUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.seimicrawler.xpath.JXDocument;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DomUtilTest
{
    private String url;
    private DomUtil domUtil;

    @Before
    public void init()
    {
        url = "https://accounts.google.com/signup/v2/webcreateaccount?hl=en&flowName=GlifWebSignIn&flowEntry=SignUp";
        domUtil = new DomUtil();
    }

    @Test
    public void getElementsByTagNameContainingOwnText() throws IOException
    {
        //Arrange
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        List<Element> elements = domUtil.getElementsByTagNameContainingOwnText(document, "div", "Username");

        //Assert
        Assert.assertTrue(elements != null);
        Assert.assertTrue(elements.size()==1);
        Assert.assertTrue(elements.get(0).ownText().contains("Username"));
    }

    @Test
    public void getElementsByTagNameMatchingOwnText() throws IOException
    {
        //Arrange
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        List<Element> elements = domUtil.getElementsByTagNameMatchingOwnText(document, "div", "Username");

        //Assert
        Assert.assertTrue(elements != null);
        Assert.assertTrue(elements.size()==1);
        Assert.assertTrue(elements.get(0).ownText().contains("Username"));
    }

    @Test
    public void getElementsMatchingOwnText() throws IOException
    {
        //Arrange
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        List<Element> elements = domUtil.getElementsMatchingOwnText(document, "Username");

        //Assert
        Assert.assertTrue(elements != null);
        Assert.assertTrue(elements.size()==1);
        Assert.assertTrue(elements.get(0).ownText().contains("Username"));
    }

    @Test
    public void getXPaths() throws IOException, AmbiguousAnchorElementsException
    {
        //Arrange
        String expectedXPath = "//div[div[contains(text(),\"Username\")]]/input[@id=\"username\"][@jsname=\"YPqjbf\"][@name=\"Username\"]";



        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        //String xpath1 = domUtil.getXpaths(document, "div", "Username", "input").get(0);
        List<String> xpath = domUtil.getXpaths(document, "div", "Username", "input");
        for(String x :xpath){
            System.out.println(x );
        }

        System.out.println("====================================");
        //xpath1 = xpath1.replace("\"","'");

        String xpath2 = domUtil.getXpaths(document, "Username", "input").get(0);
        xpath2 = xpath2.replace("\"","'");


       // System.out.println(xpath1);
        System.out.println(xpath2);

        //Assert
       // Assert.assertEquals(expectedXPath, xpath1);
        //Assert.assertEquals(expectedXPath, xpath2);
    }
    private static ClassLoader loader = DomUtilTest.class.getClassLoader();


    @Test
    public void getTaoBaoXPaths() throws IOException, AmbiguousAnchorElementsException
    {


        String content = HttpUtil.get("https://www.baidu.com");



        //Arrange
        Document document = domUtil.getDocument(content);

        //Act
        String xpath1 = domUtil.getXpaths(document,  "div","", "input").get(0);
        //xpath1 = xpath1.replace("\"","'");

        System.out.println(xpath1);



    }


    @Test
    public void checkXpath() throws Exception{
        URL t = loader.getResource("google-signup.html");
        File dBook = new File(t.toURI());
        String context = FileUtils.readFileToString(dBook, Charset.forName("utf8"));
        JXDocument doc = JXDocument.create(context);

        try {
            List<Object> objects = doc.sel("//div/input[@id='username'][@jsname='YPqjbf'][@name='Username']");
            for(Object o:objects){
                System.out.println(o);
                System.out.println("============================");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Test
    public void removeTagsFromDocument() throws IOException, AmbiguousAnchorElementsException
    {
        //Arrange
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("type", "hidden"));
        document = domUtil.removeTagsByAnyMatchedAttribute(document, attributes);

        //Act
        List<String> xpaths = domUtil.getXpaths(document, "button", "Hidden", "button");

        //Assert
        Assert.assertEquals(0, xpaths.size());
    }

    @Test
    public void getXPaths_with_AnchorElementInfo() throws AnchorIndexIfMultipleFoundOutOfBoundException, IOException, AmbiguousAnchorElementsException
    {
        //Arrange
        String expectedXPath = "//div[div[contains(text(),\"Username\")]]/input[@id=\"username\"][@jsname=\"YPqjbf\"][@name=\"Username\"]";
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        ElementInfo anchorElementInfo = new ElementInfo();
        anchorElementInfo.ownText = "userna";
        anchorElementInfo.tagName = "div";
        anchorElementInfo.indexIfMultipleFound = 0;
        anchorElementInfo.condition.whereIgnoreCaseForOwnText = true;
        anchorElementInfo.condition.whereOwnTextContainingPattern = true;
        String xpath1 = domUtil.getXpaths(document, anchorElementInfo, "input").get(0);
        String xpath2 = domUtil.getXpaths(document, "Username", "input").get(0);

        //Assert
        Assert.assertEquals(expectedXPath, xpath1);
        Assert.assertEquals(expectedXPath, xpath2);
    }

    @Test
    public void getClosestElementsFromAnchorElement() throws IOException, AmbiguousAnchorElementsException
    {
        //Arrange
        String expectedJSNameValue = "YPqjbf";
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        Element userNameTextBox1 = domUtil.getElements(document, "div", "Username", "input").get(0);
        Element userNameTextBox2 = domUtil.getElements(document, "Username", "input").get(0);

        //Assert
        Assert.assertEquals(expectedJSNameValue, userNameTextBox1.attr("jsname"));
        Assert.assertEquals(expectedJSNameValue, userNameTextBox2.attr("jsname"));
    }

    @Test
    public void getXPath() throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException
    {
        //Arrange
        String expectedXPath = "//div[div[contains(text(),\"Username\")]]/input[@id=\"username\"][@jsname=\"YPqjbf\"][@name=\"Username\"]";
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        Document document = domUtil.getActiveDocument(driver);
        driver.quit();

        //Act
        String xpath = domUtil.getXpath(document, "div", "Username", "input");

        //Assert
        Assert.assertEquals(expectedXPath, xpath);
    }

    @Test
    public void getXPath_self() throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException, IOException
    {
        //Arrange
        String expectedXPath = "//button[contains(text(),\"Next\")]";
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);

        //Act
        String xpath = domUtil.getXpath(document, "button", "Next", "button");

        //Assert
        Assert.assertEquals(expectedXPath, xpath);
    }

    @Test
    public void sampleSeleniumTest_using_getXpath()
            throws AmbiguousAnchorElementsException, AmbiguousFoundXpathsException, InterruptedException
    {
        //Arrange
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        //Act
        Document document = domUtil.getActiveDocument(driver);
        String xpath = domUtil.getXpath(document, "div", "Username", "input");
        WebElement userName = driver.findElement(By.xpath(xpath));
        userName.sendKeys("Happy Testing!");
        Thread.sleep(1000);
        driver.quit();
    }

    @Test
    public void sampleSeleniumTest_using_findElement_NoAnchorElementFoundException()
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, InterruptedException
    {
        //Arrange
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        String uuid = UUID.randomUUID().toString().substring(0, 20);

        //Act
        domUtil.getWebElement(driver, "First name", "input").sendKeys(uuid);
        domUtil.getWebElement(driver, "Last name", "input").sendKeys(uuid);
        domUtil.getWebElement(driver, "Username", "input").sendKeys(uuid);
        domUtil.getWebElement(driver, "Password", "input").sendKeys(uuid);
        domUtil.getWebElement(driver, "Confirm", "input").sendKeys(uuid);
        domUtil.getWebElement(driver, "Next", "span").click();
        WebElement input = domUtil.getWebElement(driver, "invalid anchor", "input");
        driver.quit();

        //Assert
        Assert.assertEquals(null, input);

    }

    @Test
    public void sampleSeleniumTest_using_findElement_no_element_found()
            throws AmbiguousAnchorElementsException, AmbiguousFoundWebElementsException, InterruptedException
    {
        //Arrange
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        //Act
        WebElement firstName = domUtil.getWebElement(driver, "First name", "label");
        Thread.sleep(1000);
        driver.quit();

        //Assert
        Assert.assertNull(firstName);
    }
}