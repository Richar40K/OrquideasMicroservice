package com.orquideas.microservice_payment.repository;

import com.orquideas.microservice_payment.entities.Pago;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PagoRepository extends CrudRepository<Pago,Long>
{
    Optional<Pago> findByMpPreferenceId(String mpPreferenceId);
    Optional<Pago> findByMpPaymentId(Long mpPaymentId);


}
