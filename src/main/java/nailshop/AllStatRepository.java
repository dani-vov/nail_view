package nailshop;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AllStatRepository extends CrudRepository<AllStat, Long> {
    Optional<AllStat> findByReservationId(Long reservationId);
}
