package com.orquideas.microservice_payment.repository;

import com.orquideas.microservice_payment.entities.Pago;
import com.orquideas.microservice_payment.enums.PagoTipo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends CrudRepository<Pago,Long>
{
    Optional<Pago> findByMpPreferenceId(String mpPreferenceId);
    Optional<Pago> findByMpPaymentId(Long mpPaymentId);
    List<Pago> findByTipo(PagoTipo tipo);


}
