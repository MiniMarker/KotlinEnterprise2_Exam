package no.ecm.order.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import no.ecm.order.service.CouponService
import no.ecm.utils.dto.order.CouponDto
import no.ecm.utils.hal.PageDto
import no.ecm.utils.response.WrappedResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(value = "/coupons", description = "API for Coupon entity")
@RequestMapping(
	path = ["/coupons"],
	produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class CouponController {
	
	@Autowired
	private lateinit var service: CouponService
	
	@ApiOperation("Get coupon")
	@GetMapping
	fun get(
		@ApiParam("Code of the coupon to be returned")
		@RequestParam("code", required = false)
		code: String?,
		//
		@ApiParam("Offset in the list of coupons")
		@RequestParam("offset", defaultValue = "0")
		offset: Int,
		//
		@ApiParam("Limit of coupons in a single retrieved page")
		@RequestParam("limit", defaultValue = "10")
		limit: Int
	): ResponseEntity<WrappedResponse<CouponDto>> {
		
		return service.get(code, null, offset, limit)
	}
	
	@ApiOperation("Get a coupon by its ID")
	@GetMapping(path = ["/{id}"])
	fun getById(
		@ApiParam("Id of the coupon to be returned")
		@PathVariable("id", required = true)
		id: String,
		//
		@ApiParam("Offset in the list of coupons")
		@RequestParam("offset", defaultValue = "0")
		offset: Int,
		//
		@ApiParam("Limit of coupons in a single retrieved page")
		@RequestParam("limit", defaultValue = "10")
		limit: Int
	): ResponseEntity<WrappedResponse<CouponDto>> {
		
		return service.get(null, id, offset, limit)
	}
	
	@ApiOperation("Create a new coupon")
	@PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
	fun createCoupon(
		@ApiParam("Dto of a coupon: code, description, expireAt")
		@RequestBody dto: CouponDto
	) : ResponseEntity<WrappedResponse<CouponDto>> {
		
		return service.create(dto)
		
	}
	
	/*
		api/coupons (under invoice modul men egen url) Enkel (MARKER) (Skriver tester selv)
		GET -> alle coupons
			?code=code -> henter coupons
		GET /{id} -> henter coupon basert på id
		POST -> Opprette coupon
		DELETE /{id} -> Slette en invoice
	*/
	
	
}