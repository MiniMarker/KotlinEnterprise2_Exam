package no.ecm.order.repository.ticket

import no.ecm.order.model.entity.Ticket
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository: CrudRepository<Ticket, Long>, TicketRepositoryCustom {

    fun findBySeat(seat: String): Ticket
}