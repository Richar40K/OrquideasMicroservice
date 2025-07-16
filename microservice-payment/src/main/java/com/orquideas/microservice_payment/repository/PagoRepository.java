package com.orquideas.microservice_payment.repository;

import com.orquideas.microservice_payment.entities.Pago;
import com.orquideas.microservice_payment.enums.PagoEstado;
import com.orquideas.microservice_payment.enums.PagoTipo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends CrudRepository<Pago,Long>
{
    Optional<Pago> findByMpPreferenceId(String mpPreferenceId);
    Optional<Pago> findByMpPaymentId(Long mpPaymentId);
    List<Pago> findByTipo(PagoTipo tipo);
    List<Pago> findByUserIdAndTipoAndEstado(Long userId, PagoTipo tipo, PagoEstado estado);

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estado = 'APROBADO'")
    Double calcularTotalPagosAprobados();

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estado = 'APROBADO' AND p.tipo = 'VIAJE'")
    Double calcularTotalPagosViajes();

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estado = 'APROBADO' AND p.tipo = 'ENCOMIENDA'")
    Double calcularTotalPagosEncomiendas();

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estado = 'PENDIENTE'")
    Double calcularTotalPagosPendientes();

    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.estado = 'APROBADO' AND p.fecha BETWEEN :inicio AND :fin")
    Double calcularTotalPagosAprobadosEnRango(LocalDateTime inicio, LocalDateTime fin);


    @Query("SELECT COUNT(p) FROM Pago p WHERE p.tipo = 'VIAJE' AND p.estado = 'APROBADO'")
    Long contarViajesAprobados();

    @Query("SELECT COUNT(p) FROM Pago p WHERE p.tipo = 'ENCOMIENDA' AND p.estado = 'APROBADO'")
    Long contarEncomiendasAprobadas();


}
