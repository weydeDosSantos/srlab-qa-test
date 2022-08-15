package br.com.southrocklab.resources;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.com.southrocklab.ApplicationTests;
import io.restassured.http.ContentType;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CardResourceTest extends ApplicationTests {

	protected static Integer CARD_ID;
	 
	CustomerResourceTest customer = new CustomerResourceTest();
	
	@Test
	    public void t01_deve_salvar_novo_card_no_sistema() {  
	CARD_ID =  given()
					.log().all()
					.contentType(ContentType.JSON)
					.body("{ \"brand\": \"VISA\", \"cvc\": \"111\", \"expirationMoth\": 2, \"expirationYear\": 2025, "
							+ "\"holderName\": \""+CustomerResourceTest.CONTA_NAME+"\", \"number\": \"1111111111111112\", \"customerId\": "+CustomerResourceTest.CONTA_ID+"}")
				.when()
					.post("http://localhost:9090/card")
				.then()
					.log().all()
					.statusCode(200)
					.extract().path("id"); 
	}

	@Test
		public void t02_deve_retornar_status_400_quando_salvar_card_com_cvv_maior_que_999() {
				given()
					.log().all()
					.contentType(ContentType.JSON)
					.body("{ \"brand\": \"VISA\", \"cvc\": \"1000\", \"expirationMoth\": 2, \"expirationYear\": 2025, "
							+ "\"holderName\": \"LOREM arnaldo\", \"number\": \"1111111111111112\", \"customerId\": "+CustomerResourceTest.CONTA_ID+"}")
				.when()
					.post("http://localhost:9090/card")
				.then()
					.log().all()
					.statusCode(400)
					.body("cvc", is("cvc must be between 001 to 999"));;
  }

    @Test
    	public void t03_deve_retornar_status_400_quando_salvar_card_com_expiration_month_maior_que_12() {
    			given()
    				.log().all()
    				.contentType(ContentType.JSON)
    				.body("{ \"brand\": \"VISA\", \"cvc\": \"111\", \"expirationMoth\": 13, \"expirationYear\": 2025, "
    						+ "\"holderName\": \"LOREM arnaldo\", \"number\": \"1111111111111112\", \"customerId\": "+CustomerResourceTest.CONTA_ID+"}")
    			.when()
    				.post("http://localhost:9090/card")
   				.then()
   					.log().all()
   					.statusCode(400)
   					.body("expirationMoth", is("Expiration month must be less then 12"));
    }

    @Test
    	public void t04_deve_retornar_status_400_quando_salvar_card_com_expiration_year_menor_que_2022() {
    			given()
    				.log().all()
    				.contentType(ContentType.JSON)
    				.body("{ \"brand\": \"VISA\", \"cvc\": \"111\", \"expirationMoth\": 2, \"expirationYear\": 2021, "
    						+ "\"holderName\": \"LOREM arnaldo\", \"number\": \"1111111111111112\", \"customerId\": "+CustomerResourceTest.CONTA_ID+"}")
    			.when()
    				.post("http://localhost:9090/card")
  				.then()
   					.log().all()
   					.statusCode(400)
   					.body("expirationYear", is("Expiration year must be greater than the current one"));
    }

	@Test
		public void t05_deve_retornar_status_400_quando_salvar_card_com_number_de_15_digitos() {
    			given()
    				.log().all()
    				.contentType(ContentType.JSON)
    				.body("{ \"brand\": \"VISA\", \"cvc\": \"111\", \"expirationMoth\": 2, \"expirationYear\": 2025, \"holderName\":"
    						+ " \"LOREM arnaldo\", \"number\": \"11111111111112\", \"customerId\": "+CustomerResourceTest.CONTA_ID+"}")
    			.when()
    				.post("http://localhost:9090/card")
   				.then()
   					.log().all()
   					.statusCode(400)
   					.body("number", is("Number must have a 16 numbers"));
    	  
    }

	 @Test
   		public void t06_deve_deletar_um_card_salvo_no_sistema() {
		 		given()
		  			.log().all()
		  			.contentType(ContentType.JSON)
		  			.body("{ \"brand\": \"VISA\", \"cvc\": \"111\", \"expirationMoth\": 2, \"expirationYear\": 2025, \"holderName\": "
		  					+ "\"LOREM arnaldo\", \"number\": \"1111111111111112\", \"customerId\": "+CustomerResourceTest.CONTA_ID+"}")
		  			.pathParam("id", CARD_ID)
		  		.when()
		  			.delete("http://localhost:9090/card/{id}")
		  		.then()
		  			.log().all()
		  			.statusCode(204);

   }

    @Test
    	public void  st07_deve_retornar_status_404_ao_deletar_um_card_com_id_nao_salvo_no_sistema() {
    			given()
					.log().all()
					.contentType(ContentType.JSON)
					.body("{ \"brand\": \"VISA\", \"cvc\": \"111\", \"expirationMoth\": 2, \"expirationYear\": 2025, \"holderName\": "
							+ "\"LOREM arnaldo\", \"number\": \"1111111111111112\", \"customerId\": "+CustomerResourceTest.CONTA_ID+"}")
					.pathParam("id", customer.CONTA_ID)
				.when()
					.delete("http://localhost:9090/card/{id}")
				.then()
					.log().all()
					.statusCode(404)
					.body("message", is("Card not found"));
    	 
    }
}
