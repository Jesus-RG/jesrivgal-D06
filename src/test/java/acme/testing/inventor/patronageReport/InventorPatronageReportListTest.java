package acme.testing.inventor.patronageReport;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class InventorPatronageReportListTest extends TestHarness {
	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/patronageReport/list.csv", encoding ="utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positiveTest(final int recordIndex, final String sequenceNumber, final String creationMoment, final String memorandum, final String moreInfo) {
		super.signIn("inventor1", "inventor1");
		super.clickOnMenu("Inventor", "List my patronage reports");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, sequenceNumber);
		super.checkColumnHasValue(recordIndex, 1, creationMoment);
		super.checkColumnHasValue(recordIndex, 2, memorandum);
		
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("sequenceNumber", sequenceNumber);
		super.checkInputBoxHasValue("creationMoment", creationMoment);
		super.checkInputBoxHasValue("memorandum", memorandum);
		super.checkInputBoxHasValue("moreInfo", moreInfo);
		
		super.signOut();		
	}
}



