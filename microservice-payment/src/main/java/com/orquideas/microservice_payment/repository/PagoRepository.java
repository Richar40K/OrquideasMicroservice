package com.orquideas.microservice_payment.repository;

import com.orquideas.microservice_payment.entities.Pago;
import org.springframework.data.repository.CrudRepository;

public interface PagoRepository extends CrudRepository<Pago,Long> {
}
