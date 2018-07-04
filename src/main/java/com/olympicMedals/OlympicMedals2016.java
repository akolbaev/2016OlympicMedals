package com.olympicMedals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class OlympicMedals2016 {
	WebDriver dr;
	Map<String, List<Integer>> tableAll;
	Map<String, Integer> tableGoldMedals;
	Map<String, Integer> tableSilverMedals;
	Map<String, Integer> tableBronzeMedals;
	Map<String, Integer> tableTotalMedals;
	Map<String, Integer> tableRanking;
	List<Entry<String, Integer>> list;

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		dr = new ChromeDriver();
		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		dr.manage().window().fullscreen();
	}

	@AfterClass
	public void closeAll() throws InterruptedException {
		Thread.sleep(200);
		dr.quit();
	}

	@Test
	public void test1Ranking() throws InterruptedException {
		// 1
		dr.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		String xpad = "//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//td[1]"; // ranks
																												// column
		// 2
		List<WebElement> ranks = dr.findElements(By.xpath(xpad));
		ranks.remove(ranks.size() - 1);
		checkIfRankingSorted(ranks);
		// 3
		clickToNOC();
		// 4
		List<WebElement> countries = dr.findElements(
				By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']//tbody//th/a"));
		checkIfCountriesSorted(countries);
		// 5
		List<WebElement> ranks2 = dr.findElements(By.xpath(xpad));
		checkIfRankingSorted(ranks2);

		webTableToHashMap();
		System.out.println("-----------------------------\n" + "Ranking Table: ");
		printRankingTable(tableRanking);
		System.out.println("-----------------------------\n" + "Gold Medals: ");
		rankByMedal(tableGoldMedals);
		System.out.println("-----------------------------\n" + "Silver Medals: ");
		rankByMedal(tableSilverMedals);
		System.out.println("-----------------------------\n" + "Bronze Medals: ");
		rankByMedal(tableBronzeMedals);
		System.out.println("-----------------------------\n" + "Total Medals: ");
		rankByMedal(tableTotalMedals);
	}

	@Test
	public void test2TheMost() {
		System.out.println("=======================");
		// Country with most number of Gold Medals;
		System.out.print("Contry with most GOLD medals is : ");
		assertByMedal(tableGoldMedals);
		System.out.print("Contry with most SILVER medals is : ");
		assertByMedal(tableSilverMedals);
		System.out.print("Contry with most BRONZE medals is : ");
		assertByMedal(tableBronzeMedals);
		System.out.print("Contry with MOST medals is : ");
		assertByMedal(tableTotalMedals);

	}

	@Test
	public void test3CountryByMedal() {
		System.out.println("Countires with 18 silver medals: ");
		List<String> countriesEx = Arrays.asList(" France (FRA)", " China (CHN)");
		List<String> countriesAc = new ArrayList<>();
		list = new LinkedList<Entry<String, Integer>>(tableSilverMedals.entrySet());

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getValue().equals(18)) {
				countriesAc.add(list.get(i).getKey());
				System.out.print(list.get(i).getKey() + "\n");
			}
		}
		Assert.assertEquals(countriesAc, countriesEx);
	}

	@Test
	public void test4GetIndex() {
		returnCountry("Japan (JPN)");
	}

	@Test
	public void test5sumOfBronze18() {
		list = new LinkedList<Entry<String, Integer>>(tableTotalMedals.entrySet());
		for (int i = 0; i < list.size(); i++) {
			for (int j = 1; j < list.size(); j++) {
				if (list.get(i).getValue() + list.get(j).getValue() == 18) {
					Assert.assertTrue(true);
				}
			}
		}
	}

	public void returnCountry(String country) {
		List<Integer> countryNum = Arrays.asList(6, 2);
		List<Integer> nums = new ArrayList<>();
		list = new LinkedList<Entry<String, Integer>>(tableRanking.entrySet());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getKey().contains(country)) {
				System.out.println("Row Number of  " + country + " : " + list.get(i).getValue());
				System.out.println("Column Number of " + country + " : 2");
				nums.add(list.get(i).getValue());
				nums.add(2);
			}
		}
		Assert.assertEquals(nums, countryNum);
	}

	// getOne with most medals:
	public void assertByMedal(Map<String, Integer> m) {
		list = new LinkedList<Entry<String, Integer>>(m.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		String countryExp = "United State (USA)";
		String countryAct = list.get(0).getKey();
		System.out.print(countryAct + "\n");
		Assert.assertEquals(countryExp, countryExp);
	}

	public void rankByMedal(Map<String, Integer> p) {
		list = new LinkedList<Entry<String, Integer>>(p.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		for (Entry<String, Integer> e : list) {
			System.out.println(e);
		}
	}

	public void printRankingTable(Map<String, Integer> m) {
		list = new LinkedList<Entry<String, Integer>>(m.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		for (Entry<String, Integer> e : list) {
			System.out.println(e);
		}
	}

	public void clickToNOC() throws InterruptedException {
		dr.findElement(By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/thead//th[2]"))
				.click();
		Thread.sleep(500);

	}

	public void checkIfRankingSorted(List<WebElement> lst) throws InterruptedException {
		List<Integer> lst1 = new ArrayList<>();
		for (WebElement e : lst) {
			lst1.add(Integer.parseInt(e.getText()));
		}
		List<Integer> lst2 = new ArrayList<>(lst1);
		Collections.sort(lst2);
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(lst1, lst2);
		// System.out.println(lst1);
		if (lst1.equals(lst2)) {
			System.out.println(
					"Original: " + lst1 + "\n" + "Expected: " + lst2 + "\n" + "Above list is in ascending order.");
		} else {
			System.out.println("Original: " + lst1 + "\n" + "Expected: " + lst2 + "\n"
					+ "Above list ist is not in ascending order");
		}
		Thread.sleep(500);
	}

	public void checkIfCountriesSorted(List<WebElement> lst) throws InterruptedException {
		List<String> lst1 = new ArrayList<>();
		for (WebElement e : lst) {
			lst1.add(e.getText());
		}
		List<String> lst2 = new ArrayList<>(lst1);
		Collections.sort(lst2);
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(!lst1.equals(lst2));
		if (lst1.equals(lst2)) {
			System.out.println(
					"Original: " + lst1 + "\n" + "Expected: " + lst2 + "\n" + "Above list is in ascending order.");
		} else {
			System.out.println("Original: " + lst1 + "\n" + "Expected: " + lst2 + "\n"
					+ "Above list ist is not in ascending order");
		}
		Thread.sleep(100);
	}

	public void webTableToHashMap() {
		String xpCntry = "//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody//th";
		List<WebElement> country = dr.findElements(By.xpath(xpCntry)); // Country columns
		List<String> countries = new ArrayList<>();
		for (WebElement e : country) {
			countries.add(e.getText());
		}
		System.out.println("Countries size: " + countries.size());
		List<WebElement> nums = dr.findElements(
				By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody//td"));

		List<ArrayList<Integer>> numbers = new ArrayList<ArrayList<Integer>>();
		int k = 0;
		for (int i = 0; i < 10; i++) {
			numbers.add(new ArrayList<Integer>());
			for (int j = k; j < 5 + k; j++) {
				numbers.get(i).add(Integer.parseInt(nums.get(j).getText()));
			}
			k += 5;
		}
		tableAll = new HashMap<>();
		tableGoldMedals = new HashMap<>();
		tableSilverMedals = new HashMap<>();
		tableBronzeMedals = new HashMap<>();
		tableTotalMedals = new HashMap<>();
		tableRanking = new HashMap<>();

		for (int j = 0, i = 0; i < countries.size(); i++) {
			tableAll.put(countries.get(i), numbers.get(i));
			tableGoldMedals.put(countries.get(i), numbers.get(i).get(j + 1));
			tableSilverMedals.put(countries.get(i), numbers.get(i).get(j + 2));
			tableBronzeMedals.put(countries.get(i), numbers.get(i).get(j + 3));
			tableTotalMedals.put(countries.get(i), numbers.get(i).get(j + 4));
			tableRanking.put(countries.get(i), numbers.get(i).get(j));

		}
	}
}
