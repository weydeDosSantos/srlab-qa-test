package br.com.southrocklab.resources;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.com.southrocklab.ApplicationTests;
import io.restassured.http.ContentType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerResourceTest extends ApplicationTests {

	protected static String CONTA_NAME = "conta" + System.nanoTime();
	protected static String CONTA_SOBRENAME = "sobrenome" + System.nanoTime();
	protected static Integer CONTA_ID; 

	@Test
	public void t01_deve_salvar_novo_customer_no_sistema() {	
	CONTA_ID = given()
					.log().all()
			        .contentType(ContentType.JSON)
			        .body("{ \"birthDate\": \"1992-08-15\", \"lastName\": \""+CONTA_SOBRENAME+"\", \"name\": \""+ CONTA_NAME +"\"}")
			   .when()
			      	.post("http://localhost:9090/customer")
			   .then()
			      	.log().all()
			  		.statusCode(200)
			  		.extract().path("id");  		
	}

	@Test
	public void t02_deve_retornar_status_400_quando_salvar_customer_ja_salvo() {
		
			given()
				.log().all()
				.contentType(ContentType.JSON)
				.body("{ \"birthDate\": \"1992-08-15\", \"lastName\": \""+CONTA_SOBRENAME+"\", \"name\": \""+CONTA_NAME+"\"}")
			.when()
				.post("http://localhost:9090/customer")
			.then()
				.log().all()
				.statusCode(400)
				.body("message", is("Customer already exists"));
	}

	@Test
	public void t03_deve_retornar_status_400_quando_salvar_customer_com_birth_date_maior_que_hoje() {
		
			given()
				.log().all()
				.contentType(ContentType.JSON)
				.body("{ \"birthDate\": \"2023-08-13\", \"lastName\": \""+CONTA_SOBRENAME+"\", \"name\": \""+CONTA_NAME+"\"}")
			.when()
				.post("http://localhost:9090/customer")
			.then()
				.log().all()
				.statusCode(400)
				.body("birthDate", is("Birth date must be smaller than today"));
	}

	@Test
	public void t04_deve_procurar_customer_pelo_name_e_last_name() {
		
			given()
				.log().all()
				.contentType(ContentType.JSON)
			.when()
				.get("http://localhost:9090/customer/"+CONTA_NAME+"/"+CONTA_SOBRENAME+"")
			.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	public void t05_deve_retornar_status_404_quando_buscar_customer_por_name_e_last_name_inexistente() {
			given()
				.log().all()
				.contentType(ContentType.JSON)
			.when()
				.get("http://localhost:9090/customer/nomeinexistente/sobrenomeinexistente")
			.then()
				.log().all()
				.statusCode(404)
				.body("message", is("Customer not found"));
	}
	
	@Test
	public void t06_deve_atualizar_o_last_name_de_um_customer() {
			given()
				.log().all()
				.contentType(ContentType.JSON)
				.body("{ \"birthDate\": \"1992-08-14\", \"lastName\": \"bimarques\", \"name\": \""+CONTA_NAME+"alterado\"}")
				.pathParam("id", CONTA_ID)
			.when()
				.put("http://localhost:9090/customer/{id}")
			.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	public void t07_deve_retornar_status_404_ao_atualizar_um_customer_com_id_nao_salvo_no_sistema() {
			given()
				.log().all()
				.contentType(ContentType.JSON)
				.body("{ \"birthDate\": \"1992-08-14\", \"lastName\": \"bimarques\", \"name\": \""+CONTA_NAME+"alterado\"}")
			.when()
				.put("http://localhost:9090/customer/200")
			.then()
				.log().all()
				.statusCode(404)
				.body("message", is("Customer not found"));	
	}

	@Test
	public void t08_deve_deletar_um_customer_salvo_no_sistema() {
			given()
				.log().all()
				.contentType(ContentType.JSON)
				.pathParam("id", CONTA_ID)
			.when()
				.delete("http://localhost:9090/customer/{id}")
			.then()
				.log().all()
				.statusCode(204);
	}
	@Test
	public void t09_deve_salvar_novo_customer_no_sistema() {	
CONTA_ID = given()
				.log().all()
		        .contentType(ContentType.JSON)
			    .body("{ \"birthDate\": \"1992-08-15\", \"lastName\": \""+CONTA_SOBRENAME+"\", \"name\": \""+ CONTA_NAME +"\"}")
		   .when()
		      	.post("http://localhost:9090/customer")
		   .then()
		      	.log().all()
		  		.statusCode(200)
			  	.extract().path("id");  		
	}
	@Test
	public void t09_deve_retornar_status_404_ao_deletar_um_customer_com_id_nao_salvo_no_sistema() {
			given()
				.log().all()
				.contentType(ContentType.JSON)
			.when()
				.delete("http://localhost:9090/customer/200")
			.then()
				.log().all()
				.statusCode(404)
				.body("message", is("Customer not found"))
				
		;
	}
}