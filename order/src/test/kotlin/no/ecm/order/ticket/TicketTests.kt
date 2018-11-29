package no.ecm.order.ticket

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import no.ecm.utils.dto.order.TicketDto
import no.ecm.utils.response.TicketResponseDto
import org.hamcrest.CoreMatchers
import org.junit.Test

class TicketTests : TicketTestBase() {
	
	@Test
	fun getAllTicketsTest() {
		val size = RestAssured.given().accept(ContentType.JSON).get()
			.then()
			.statusCode(200)
			.extract()
			.`as`(TicketResponseDto::class.java).data!!.list.size
		
		assertResultSize(size)
	}
	
	@Test
	fun createTicketAndGetByIdTest() {
		val size = RestAssured.given().accept(ContentType.JSON).get()
			.then()
			.statusCode(200)
			.extract()
			.`as`(TicketResponseDto::class.java).data!!.list.size
		
		val price = 200.5
		val seat = "A2"
		
		val id = createTicket(price, seat)
		
		assertResultSize(size + 1)
		
		RestAssured.given()
			.get("/$id")
			.then()
			.statusCode(200)
			.body("data.list[0].id", CoreMatchers.equalTo(id.toString()))
			//FIXME .body("data.list[0].price", CoreMatchers.equalTo(price.toString()))
			.body("data.list[0].seat", CoreMatchers.equalTo(seat))
	}
	
	@Test
	fun cachingTest() {
		
		val etag =
			given()
				.accept(ContentType.JSON)
				.get()
				.then()
				.statusCode(200)
				.header("ETag", CoreMatchers.notNullValue())
				.extract().header("ETag")
		
		given()
			.accept(ContentType.JSON)
			.header("If-None-Match", etag)
			.get()
			.then()
			.statusCode(304)
			.content(CoreMatchers.equalTo(""))
	}
	
	@Test
	fun createTicketWithInvalidSeatTest() {
		
		val list: MutableList<TicketDto> = mutableListOf()
		
		list.add(TicketDto(null, 200.1, "AA"))
		list.add(TicketDto(null, 200.1, "*5"))
		list.add(TicketDto(null, 200.1, "abc123"))
		list.add(TicketDto(null, 200.1, "A!"))
		list.add(TicketDto(null, 200.1, "A123"))
		list.add(TicketDto(null, 200.1, "AA12"))
		
		list.forEach {
			given().contentType(ContentType.JSON).body(it).post().then().statusCode(400)
		}
	}
	
	@Test
	fun deleteUnusedTicketTest() {
		
		val price = 200.5
		val seat = "A2"
		
		val id = createTicket(price, seat)
		
		given()
			.delete("/$id")
			.then()
			.statusCode(204)
		
		given()
			.get("/$id")
			.then()
			.statusCode(404)
	}
	
	@Test
	fun updateSeatNumber() {
		
		val price = 200.5
		val seat = "A8"
		val updatedSeat = "C3"
		
		val id = createTicket(price, seat)
		
		given().contentType("application/merge-patch+json")
			.body("{\"seat\": \"$updatedSeat\"}")
			.patch("/$id")
			.then()
			.statusCode(204)
		
		given()
			.get("/$id")
			.then()
			.statusCode(200)
			.body("data.list[0].seat", CoreMatchers.equalTo(updatedSeat))
	}
	
	@Test
	fun updateSeatNumberWithInvalidInformation() {
		
		val price = 200.5
		val seat = "A8"
		val updatedSeat = "C3"
		
		val id = createTicket(price, seat)
		
		//Invalid JSON Merge Patch syntax
		given().contentType("application/merge-patch+json")
			.body("{seat: \"$updatedSeat\"}")
			.patch("/$id")
			.then()
			.statusCode(409)
		
		//Update with id in JSON Merge Patch body
		given().contentType("application/merge-patch+json")
			.body("{\"id\": $id,\"seat\": \"$updatedSeat\"}")
			.patch("/$id")
			.then()
			.statusCode(400)
		
		//Update with invalid update value
		given().contentType("application/merge-patch+json")
			.body("{\"abc\": 123}")
			.patch("/$id")
			.then()
			.statusCode(400)
		
		//Update non existing ticket
		given().contentType("application/merge-patch+json")
			.body("{\"abc\": 123}")
			.patch("/666")
			.then()
			.statusCode(404)
	}
}