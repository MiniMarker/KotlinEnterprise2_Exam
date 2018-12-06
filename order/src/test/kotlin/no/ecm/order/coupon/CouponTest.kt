package no.ecm.order.coupon

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import no.ecm.utils.dto.order.CouponDto
import no.ecm.utils.response.CouponResponseDto
import org.hamcrest.CoreMatchers
import org.junit.Test

class CouponTest : CouponTestBase() {
	
	//TODO Fix all sizes stuff after DELETE is done
	/*
	@Test
	fun testCleanDb() {
		assertResultSize(0)
	}
	*/
	
	@Test
	fun getAllCouponsTest() {
		val size = given().accept(ContentType.JSON).get()
			.then()
			.statusCode(200)
			.extract()
			.`as`(CouponResponseDto::class.java).data!!.list.size
		
		assertResultSize(size)
	}
	
	@Test
	fun createCouponAndGetByIdTest() {
		val size = given().accept(ContentType.JSON).get()
			.then()
			.statusCode(200)
			.extract()
			.`as`(CouponResponseDto::class.java).data!!.list.size
		
		val code = "1234559221"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val percentage = 10
		
		val id = createCoupon(code, description, expireAt, percentage)
		
		given()
			.get("/$id")
			.then()
			.statusCode(200)
			.body("data.list[0].id", CoreMatchers.equalTo(id.toString()))
			.body("data.list[0].code", CoreMatchers.equalTo(code))
			.body("data.list[0].description", CoreMatchers.equalTo(description))
	}
	
	@Test
	fun createWithInvalidDataTest() {
		val size = given().accept(ContentType.JSON).get()
			.then()
			.statusCode(200)
			.extract()
			.`as`(CouponResponseDto::class.java).data!!.list.size
		val code = "1234554321"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val percentage = 10
		
		createInvalidCoupon("", description, expireAt, percentage, 400)
		assertResultSize(size)
		createInvalidCoupon("", description, expireAt, percentage, 400)
		assertResultSize(size)
		createInvalidCoupon("", description, expireAt, percentage, 400)
		assertResultSize(size)
		
	}
	
	@Test
	fun updateWithInvalidDataTest() {
		
		val code = "1234914321"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val percentage = 10
		
		val updatedCode = "0987654321"
		val updatedDescription = "UpdatedDescription"
		val updatedExpireAt = "2018-12-24 20:30:30"
		
		val id = createCoupon(code, description, expireAt, percentage)
		val etag = getEtagFromId(id.toString())
		
		given()
			.contentType(ContentType.JSON)
			.pathParam("id", id)
			.header("If-Match", etag)
			.body(CouponDto(null, code, updatedDescription, updatedExpireAt, percentage))
			.put("/{id}")
			.then()
			.statusCode(400)
		
		given()
			.contentType(ContentType.JSON)
			.pathParam("id", id)
			.header("If-Match", etag)
			.body(CouponDto(id.toString(), null, updatedDescription, updatedExpireAt, percentage))
			.put("/{id}")
			.then()
			.statusCode(400)
		
		given()
			.contentType(ContentType.JSON)
			.pathParam("id", id)
			.header("If-Match", etag)
			.body(CouponDto(id.toString(), updatedCode, null, updatedExpireAt, percentage))
			.put("/{id}")
			.then()
			.statusCode(400)
		
		given()
			.contentType(ContentType.JSON)
			.pathParam("id", id)
			.header("If-Match", etag)
			.body(CouponDto(id.toString(), updatedCode, updatedDescription, null, percentage))
			.put("/{id}")
			.then()
			.statusCode(400)
		
	}
	
	@Test
	fun getWithInvalidIdTest() {
		
		val code = "1234567899"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val percentage = 10
		
		createCoupon(code, description, expireAt, percentage)
		
		given()
			.get("/x")
			.then()
			.statusCode(400)
			.body("message", CoreMatchers.notNullValue())
			.body("status", CoreMatchers.equalTo("ERROR"))
			.body("page", CoreMatchers.nullValue())
	}
	
	@Test
	fun getByCodeTest() {
		
		val code = "1209348756"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		
		val percentage = 10
		
		val id = createCoupon(code, description, expireAt, percentage)
		
		given()
			.param("code", code)
			.get()
			.then()
			.statusCode(200)
			.body("data.list[0].id", CoreMatchers.equalTo(id.toString()))
			.body("data.list[0].code", CoreMatchers.equalTo(code))
			.body("data.list[0].description", CoreMatchers.equalTo(description))
	}
	
	@Test
	fun updateCouponTest() {
		
		val code = "0987654321"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val percentage = 10
		
		val id = createCoupon(code, description, expireAt, percentage)
		
		val updatedCode = "0987654321"
		val updatedDescription = "UpdatedDescription"
		val updatedExpireAt = "2018-12-24 20:30:30"
		
		val etag = getEtagFromId(id.toString())
		
		given()
			.contentType(ContentType.JSON)
			.pathParam("id", id)
			.header("If-Match", etag)
			.body(CouponDto(id.toString(), updatedCode, updatedDescription, updatedExpireAt, percentage))
			.put("/{id}")
			.then()
			.statusCode(204)
		
		given()
			.get("/$id")
			.then()
			.statusCode(200)
			.body("data.list[0].id", CoreMatchers.equalTo(id.toString()))
			.body("data.list[0].code", CoreMatchers.equalTo(updatedCode))
			.body("data.list[0].description", CoreMatchers.equalTo(updatedDescription))
	}
	
	@Test
	fun updateCouponWithNonMatchingIdInPathAndBody() {
		
		val code = "0987654321"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val percentage = 10
		
		val id = createCoupon(code, description, expireAt, percentage)
		
		val updatedCode = "0987654321"
		val updatedDescription = "UpdatedDescription"
		val updatedExpireAt = "2018-12-24 20:30:30"
		
		val etag = getEtagFromId(id.toString())
		
		given()
			.contentType(ContentType.JSON)
			.pathParam("id", 12345)
			.header("If-Match", etag)
			.body(CouponDto(id.toString(), updatedCode, updatedDescription, updatedExpireAt))
			.put("/{id}")
			.then()
			.statusCode(404)
	}
	
	@Test
	fun createCouponWithGivenIdTest() {
		
		//TODO Expand to more test cases
		
		val code = "45678123"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val percentage = 10
		
		val dto = CouponDto("1234", code, description, expireAt, percentage)
		
		given().contentType(ContentType.JSON)
			.body(dto)
			.post()
			.then()
			.statusCode(400)
			.body("message", CoreMatchers.notNullValue())
			.body("status", CoreMatchers.equalTo("ERROR"))
			.body("page", CoreMatchers.nullValue())
	}
	
	@Test
	fun deleteUnusedCouponTest() {
		
		// This test covers deletion of a coupon that is unused.
		// Which means that no orders have been made using this coupon
		// This will be tested in another test
		
		val code = "123412345"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val percentage = 10
		
		val id = createCoupon(code, description, expireAt, 10)
		
		given()
			.delete("/$id")
			.then()
			.statusCode(200)
		
		given()
			.get("/$id")
			.then()
			.statusCode(404)
		
	}
	
	@Test
	fun cachingGetAllTest() {
		
		val etag = RestAssured.given().accept(ContentType.JSON)
			.get()
			.then()
			.statusCode(200)
			.header("ETag", CoreMatchers.notNullValue())
			.extract().header("ETag")
		
		given().accept(ContentType.JSON)
			.header("If-None-Match", etag)
			.get()
			.then()
			.statusCode(304)
			.content(CoreMatchers.equalTo(""))
	}
	
	@Test
	fun cachingGetByIdTest() {
		
		val code = "123412345"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val percentage = 10
		
		val id = createCoupon(code, description, expireAt, percentage)
		
		val etag = RestAssured.given().accept(ContentType.JSON)
			.get("/$id")
			.then()
			.statusCode(200)
			.header("ETag", CoreMatchers.notNullValue())
			.extract().header("ETag")
		
		given().accept(ContentType.JSON)
			.header("If-None-Match", etag)
			.get("/$id")
			.then()
			.statusCode(304)
			.content(CoreMatchers.equalTo(""))
	}
	
	@Test
	fun updateDescriptionTest() {
		
		val code = "6743903212"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val updatedDescription = "UpdatedDescription"
		val percentage = 10
		
		val id = createCoupon(code, description, expireAt, percentage)
		val etag = getEtagFromId(id.toString())
		
		given().contentType("application/merge-patch+json")
			.header("If-Match", etag)
			.body("{\"description\": \"$updatedDescription\"}")
			.patch("/$id")
			.then()
			.statusCode(204)
		
		given()
			.get("/$id")
			.then()
			.statusCode(200)
			.body("data.list[0].description", CoreMatchers.equalTo(updatedDescription))
	}
	
	@Test
	fun updateDescriptionNumberWithInvalidInformation() {
		
		val code = "98235610362"
		val description = "DefaultDescription"
		val expireAt = "2019-01-01 01:00:00"
		val updatedDescription = "UpdatedDescription"
		val percentage = 10
		
		val id = createCoupon(code, description, expireAt, percentage)
		val etag = getEtagFromId(id.toString())
		
		//Invalid JSON Merge Patch syntax
		given().contentType("application/merge-patch+json")
			.header("If-Match", etag)
			.body("{seat: \"$updatedDescription\"}")
			.patch("/$id")
			.then()
			.statusCode(409)
		
		//Update with id in JSON Merge Patch body
		given().contentType("application/merge-patch+json")
			.header("If-Match", etag)
			.body("{\"id\": \"$id\",\"seat\": \"$updatedDescription\"}")
			.patch("/$id")
			.then()
			.statusCode(400)
		
		//Update with invalid update value
		given().contentType("application/merge-patch+json")
			.header("If-Match", etag)
			.body("{\"abc\": 123}")
			.patch("/$id")
			.then()
			.statusCode(400)
		
		//Update non existing ticket
		given().contentType("application/merge-patch+json")
			.header("If-Match", etag)
			.body("{\"abc\": 123}")
			.patch("/7777")
			.then()
			.statusCode(404)
	}
	
}