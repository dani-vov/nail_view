package NailShop.Event;

import NailShop.AbstractEvent;
import NailShop.AllStat;
import NailShop.AllStatRepository;
import NailShop.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class EventListener {

    @Autowired
    AllStatRepository allStatRepository;

    /**
     * 각종 이벤트 발생시 저장을 하는 공간
     * @param message
     */
    @StreamListener(KafkaProcessor.INPUT)
    public void onMessage(@Payload String message) {
        System.out.println(message);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        AbstractEvent event = null;
        try {
            event = objectMapper.readValue(message, AbstractEvent.class);

            /**
             * 예약 완료 이벤트
             */
            if( event.getEventType().equals(ReservationReserved.class.getSimpleName())){
                ReservationReserved reservationReserved = objectMapper.readValue(message, ReservationReserved.class);

                AllStat allStat = new AllStat();
                allStat.setReservationId(reservationReserved.getId());
                allStat.setReservatorName(reservationReserved.getReservatorName());
                allStat.setReservationDate(reservationReserved.getReservationDate());
                allStat.setPhoneNumber(reservationReserved.getPhoneNumber());
                allStat.setReservationStatus("예약 완료");

                allStatRepository.save(allStat);
            }
            /**
             * 예약 취소 이벤트
             */
            else if( event.getEventType().equals(ReservationCanceled.class.getSimpleName())) {
                ReservationCanceled reservationCanceled = objectMapper.readValue(message, ReservationCanceled.class);

                AllStat allStat = new AllStat();
                allStat.setReservationId(reservationCanceled.getId());
                allStat.setReservatorName(reservationCanceled.getReservatorName());
                allStat.setReservationDate(reservationCanceled.getReservationDate());
                allStat.setPhoneNumber(reservationCanceled.getPhoneNumber());
                allStat.setReservationStatus("예약 취소");

                allStatRepository.save(allStat);
            }

            /**
             * 예약 변경 이벤트
             */
            else if( event.getEventType().equals(ReservationChanged.class.getSimpleName())) {

                ReservationChanged reservationChanged = objectMapper.readValue(message, ReservationChanged.class);

                AllStat allStat = new AllStat();
                allStat.setReservationId(reservationChanged.getId());
                allStat.setReservatorName(reservationChanged.getReservatorName());
                allStat.setReservationDate(reservationChanged.getReservationDate());
                allStat.setPhoneNumber(reservationChanged.getPhoneNumber());
                allStat.setReservationStatus("예약 변경");

                allStatRepository.save(allStat);

            }
            /**
             * 네일 완료 이벤트
             */
            else if( event.getEventType().equals(NailFinished.class.getSimpleName())) {
                NailFinished nailFinished = objectMapper.readValue(message, NailFinished.class);

                AllStat allStat = new AllStat();
                allStat.setReservationId(nailFinished.getReservationId());
                allStat.setEmployee(nailFinished.getEmployee());
                allStat.setDescription(nailFinished.getDescription());
                allStat.setFee(nailFinished.getFee());
                allStat.setReservatorName(nailFinished.getReservatorName());
                allStat.setPhoneNumber(nailFinished.getPhoneNumber());
                allStat.setReservationDate(nailFinished.getReservationDate());
                allStat.setReservationStatus("네일 완료");

                allStatRepository.save(allStat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}