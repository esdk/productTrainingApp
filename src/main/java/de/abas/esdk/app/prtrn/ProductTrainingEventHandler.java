package de.abas.esdk.app.prtrn;

import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.FieldEventHandler;
import de.abas.erp.axi2.type.FieldEventType;
import de.abas.erp.db.schema.custom.producttraining.ProductTrainingEditor;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.referencetypes.Person;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;

@EventHandler(head = ProductTrainingEditor.class, row = ProductTrainingEditor.Row.class)
@RunFopWith(EventHandlerRunner.class)
public class ProductTrainingEventHandler {

	@FieldEventHandler(field = "yprtrnproduct", type = FieldEventType.EXIT)
	public void productExit(ProductTrainingEditor productTrainingEditor) {
		Product product = productTrainingEditor.getYprtrnproduct();
		productTrainingEditor.setSwd("T_" + product.getSwd());
		productTrainingEditor.setDescr("Training for " + product.getDescr());
	}

	@FieldEventHandler(field = "yprtrnparticipant", table = true, type = FieldEventType.EXIT)
	public void participantExit(ProductTrainingEditor.Row row) {
		Person participant = row.getYprtrnparticipant();
		row.setYprtrnaddress(participant.getAddr());
		row.setYprtrnzipcode(participant.getZipCode());
		row.setYprtrntown(participant.getTown());
		row.setYprtrncountry(participant.getStateOfTaxOffice());
	}

}
