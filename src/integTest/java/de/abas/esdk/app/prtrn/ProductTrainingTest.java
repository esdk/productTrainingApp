package de.abas.esdk.app.prtrn;

import de.abas.erp.db.schema.custom.producttraining.ProductTrainingEditor;
import de.abas.erp.db.schema.customer.CustomerContact;
import de.abas.erp.db.schema.customer.CustomerContactEditor;
import de.abas.erp.db.schema.customer.CustomerEditor;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.esdk.test.util.EsdkIntegTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static de.abas.esdk.app.prtrn.ProductTrainingTest.Participants.CUSTOMER_CON_1;
import static de.abas.esdk.app.prtrn.ProductTrainingTest.Products.PROD_1;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProductTrainingTest extends EsdkIntegTest {

	@BeforeClass
	public static void createTestData() {
		createParticipantTestData();
		createProductTestData();
	}

	ProductTrainingEditor productTrainingEditor = ctx.newObject(ProductTrainingEditor.class);

	@Test
	public void autoFillsHeadFields() {
		productTrainingEditor.setYprtrnproduct(PROD_1.object);

		assertThat(productTrainingEditor.getSwd(), is("T_" + PROD_1.swd));
		assertThat(productTrainingEditor.getDescr(), is("Training for " + PROD_1.descr));
	}

	@Test
	public void autoFillsTableFields() {
		ProductTrainingEditor.Row row = productTrainingEditor.table().appendRow();
		row.setYprtrnparticipant(CUSTOMER_CON_1.object);

		assertThat(row.getYprtrnaddress(), is(CUSTOMER_CON_1.address));
		assertThat(row.getYprtrnzipcode(), is(CUSTOMER_CON_1.zipCode));
		assertThat(row.getYprtrntown(), is(CUSTOMER_CON_1.town));
		assertThat(row.getYprtrncountry().getSwd(), is(CUSTOMER_CON_1.country));
	}

	@After
	public void tidyUp() {
		productTrainingEditor.abort();
	}

	@AfterClass
	public static void deleteTestData() {
		for (final Participants participant : Participants.values()) {
			participant.object.delete();
		}
		for (final Products product : Products.values()) {
			product.object.delete();
		}
	}

	enum Participants {
		CUSTOMER_CON_1("TESTCUST1", "Gartenstraße 67", "76135", "Karlsruhe", "DEUTSCHLAND"),
		CUSTOMER_CON_2("TESTCUST2", "Pfinztalstraße 105", "76227", "Karlsruhe", "DEUTSCHLAND");

		final String swd;

		final String address;
		final String zipCode;
		final String town;
		final String country;
		CustomerContact object;

		Participants(final String swd, final String address, final String zipCode, final String town, final String country) {
			this.swd = swd;
			this.address = address;
			this.zipCode = zipCode;
			this.town = town;
			this.country = country;
		}

	}

	enum Products {
		PROD_1("TESTPROD1", "Test Artikel 1"),
		PROD_2("TESTPROD2", "Test Artikel 2");

		final String swd;

		final String descr;
		Product object;

		Products(final String swd, final String descr) {
			this.swd = swd;
			this.descr = descr;
		}

	}

	private static void createParticipantTestData() {
		CustomerEditor customerEditor = ctx.newObject(CustomerEditor.class);
		customerEditor.setSwd("TESTCUST");
		customerEditor.commit();
		for (final Participants participant : Participants.values()) {
			CustomerContactEditor customerContactEditor = ctx.newObject(CustomerContactEditor.class);
			try {
				customerContactEditor.setCompanyARAP(customerEditor);
				customerContactEditor.setSwd(participant.swd);
				customerContactEditor.setAddr(participant.address);
				customerContactEditor.setZipCode(participant.zipCode);
				customerContactEditor.setTown(participant.town);
				customerContactEditor.commit();
				participant.object = customerContactEditor;
			} finally {
				if (customerContactEditor != null && customerContactEditor.active()) {
					customerContactEditor.abort();
				}
			}
		}
	}

	private static void createProductTestData() {
		for (final Products product : Products.values()) {
			ProductEditor productEditor = ctx.newObject(ProductEditor.class);
			try {
				productEditor.setSwd(product.swd);
				productEditor.setDescr(product.descr);
				productEditor.commit();
				product.object = productEditor;
			} finally {
				if (productEditor != null && productEditor.active()) {
					productEditor.abort();
				}
			}
		}
	}

}
