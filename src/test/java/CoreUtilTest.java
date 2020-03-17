import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import learning.BDD.utilities.util.CoreUtil;
import learning.BDD.utilities.util.DataUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoreUtilTest {


    @Test(enabled = false)
    public void currencyUtilTest() {
        Assert.assertEquals(CoreUtil.process("[USD~~3999.99]"), "$3,999.99");
        Assert.assertEquals(CoreUtil.process("[USD~~$4,5999.99]"), "$45,999.99");
        Assert.assertEquals(CoreUtil.process("[EURO~~3999.99]"), "£3,999.99");
        Assert.assertEquals(CoreUtil.process("[YEN~~3999.00]"), "￥3,999");          //Yen is not taking decimal values, if we pass 99 it is rounding to next integer
        Assert.assertEquals(CoreUtil.process("[YUAN~~3999.99]"), "￥3,999.99");
    }

    @Test(enabled = false)
    public void phoneNumberUtilTest() {
        Assert.assertEquals(CoreUtil.process("[PHONENUMBER_HYPEN~~2345645120]"), "234-564-5120");
        Assert.assertEquals(CoreUtil.process("[PHONENUMBER_HYPEN_AR~~2345645120]"), "(234) 564-5120");
        Assert.assertEquals(CoreUtil.process("[PHONENUMBER_SPACE~~2345645120]"), "(234) 564 5120");
        Assert.assertEquals(CoreUtil.process("[PHONENUMBER_HYPEN_EXTN~~1234-564;5120]"), "1 (234) 564-5120");
        Assert.assertEquals(CoreUtil.process("[PHONENUMBER_SPACE_EXTN~~12345645120]"), "1 (234) 564 5120");
    }

    @Test(enabled = false)
    public void dateUtilTest() {
        Assert.assertEquals(CoreUtil.process("[TODAY~~dd-MM-yyyy]"), LocalDateTime.now().plusDays(0).plusMonths(0).plusYears(0).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Assert.assertEquals(CoreUtil.process("[TODAY~~dd/MM/yyyy]"), LocalDateTime.now().plusDays(0).plusMonths(0).plusYears(0).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Assert.assertEquals(CoreUtil.process("[YESTERDAY~~MM-dd-yyyy]"), LocalDateTime.now().plusDays(-1).plusMonths(0).plusYears(0).format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        Assert.assertEquals(CoreUtil.process("[TOMORROW~~MM/dd/yyyy]"), LocalDateTime.now().plusDays(1).plusMonths(0).plusYears(0).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        Assert.assertEquals(CoreUtil.process("[NEXT_MONTH~~MM/dd/yyyy]"), LocalDateTime.now().plusDays(0).plusMonths(1).plusYears(0).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        Assert.assertEquals(CoreUtil.process("[PREVIOUS_MONTH~~MM/dd/yyyy]"), LocalDateTime.now().plusDays(0).plusMonths(-1).plusYears(0).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        Assert.assertEquals(CoreUtil.process("[NEXT_YEAR~~MM/dd/yyyy]"), LocalDateTime.now().plusDays(0).plusMonths(0).plusYears(1).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        Assert.assertEquals(CoreUtil.process("[PREVIOUS_YEAR~~MM/dd/yyyy]"), LocalDateTime.now().plusDays(0).plusMonths(0).plusYears(-1).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        //Using Override values
        Assert.assertEquals(CoreUtil.process("[TODAY~~dd MMM yyyy+1M-1D]"), LocalDateTime.now().plusDays(-1).plusMonths(1).plusYears(0).format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        Assert.assertEquals(CoreUtil.process("[TODAY~~dd MMM yyyy1Y+1M-1D]"), LocalDateTime.now().plusDays(-1).plusMonths(1).plusYears(1).format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        Assert.assertEquals(CoreUtil.process("[TODAY~~dd MMM yyyy-100D]"), LocalDateTime.now().plusDays(-100).plusMonths(0).plusYears(0).format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        Assert.assertEquals(CoreUtil.process("[TODAY~~dd MMM yyyy+1M-1D]"), LocalDateTime.now().plusDays(-1).plusMonths(1).plusYears(0).format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        Assert.assertEquals(CoreUtil.process("[TODAY~~dd MMM yyyy+1M-1D]"), LocalDateTime.now().plusDays(-1).plusMonths(1).plusYears(0).format(DateTimeFormatter.ofPattern("dd MMM yyyy")));

        //format Date
        Assert.assertEquals(CoreUtil.process("[1982-02-11~~yyyy-MM-dd~~dd/MM/yyyy]"), "11/02/1982");

        //Matching content with data
        String sTestData = "The Cancel Date should be after %s for testing purpose";
        Assert.assertEquals(CoreUtil.process(String.format(sTestData, "[TODAY~~dd-MM-yyyy]")), String.format(sTestData, LocalDateTime.now().plusDays(0).plusMonths(0).plusYears(0).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        Assert.assertEquals(CoreUtil.process(String.format(sTestData, "[YESTERDAY~~dd-MM-yyyy-1M-1Y]")), String.format(sTestData, LocalDateTime.now().plusDays(-1).plusMonths(-1).plusYears(-1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));

    }

    @Test(enabled = false)
    public void randomUtilTest() {
        System.out.println(CoreUtil.process("[ALPHA~~15]"));
        System.out.println(CoreUtil.process("[NUMERIC~~9]"));
        //DataUtil.store will append threadId also to the key and the same will be used by retrieving the key from dataMap. It is done in parseKey Method
        CoreUtil.process("[ALPHA_NUMERIC~~10~~AlphaNumeric]");
        System.out.println(DataUtil.retrieve("AlphaNumeric"));
        System.out.println(CoreUtil.process("[ALPHA_SPECIAL~~6]"));
        System.out.println(CoreUtil.process("[NUMERIC_SPECIAL~~6]"));
        System.out.println(CoreUtil.process("[ALPHA_NUMERIC_SPECIAL~~12]"));
    }

    @Test
    public void dataUtilAPIHTML() {
        ValidatableResponse response = RestAssured.given().when().get("https://cdc.gov").then().statusCode(200);
        Assert.assertEquals(DataUtil.queryApiResponse("HTML", "**.findAll{it.href == '//fastlane.rubiconproject.com'}"), "");
        System.out.println("test");
    }

}
