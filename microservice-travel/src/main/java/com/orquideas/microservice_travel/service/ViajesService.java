package com.orquideas.microservice_travel.service;

import com.orquideas.microservice_travel.DTO.*;
import com.orquideas.microservice_travel.client.BusClient;
import com.orquideas.microservice_travel.client.UserClient;
import com.orquideas.microservice_travel.entities.AsientoOcupado;
import com.orquideas.microservice_travel.entities.Rutas;
import com.orquideas.microservice_travel.entities.Viajes;
import com.orquideas.microservice_travel.enums.State;
import com.orquideas.microservice_travel.repositories.AsientoOcupadoRepository;
import com.orquideas.microservice_travel.repositories.RutasRepository;
import com.orquideas.microservice_travel.repositories.ViajesRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ViajesService implements IViajesService {

    @Autowired
    private ViajesRepository viajesRepository;

    @Autowired
    private RutasRepository rutasRepository;

    @Autowired
    private BusClient busClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private AsientoOcupadoRepository asientoOcupadoRepository;

    // Simula los asientos ocupados (cuando tengas tu repo real, reemplaza esto)
    @Override
    @Transactional
    public void ocuparAsiento(Long viajeId, Integer numero) {
        boolean yaOcupado = asientoOcupadoRepository
                .findByViajeIdAndNumeroAsiento(viajeId, numero)
                .isPresent();
        if (yaOcupado) {
            throw new IllegalStateException("El asiento ya está ocupado.");
        }

        AsientoOcupado nuevo = new AsientoOcupado();
        nuevo.setViajeId(viajeId);
        nuevo.setNumeroAsiento(numero);
        asientoOcupadoRepository.save(nuevo);
    }
    @Override
    @Transactional(readOnly = true)
    public List<AsientoDTO> listarAsientosDeViaje(Long viajeId) {
        Viajes viaje = viajesRepository.findById(viajeId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));
        BusDTO bus = busClient.getBusById(viaje.getBusId());
        int capacidad = bus.getCapacidad().intValue();

        List<Integer> ocupados = asientoOcupadoRepository
                .findByViajeId(viajeId)
                .stream()
                .map(AsientoOcupado::getNumeroAsiento)
                .collect(Collectors.toList());

        List<AsientoDTO> resultado = new ArrayList<>();
        for (int i = 1; i <= capacidad; i++) {
            resultado.add(new AsientoDTO(i, ocupados.contains(i) ? "OCUPADO" : "DISPONIBLE"));
        }
        return resultado;
    }

    // ----- METODOS -----

    @Override
    @Transactional(readOnly = true)
    public List<ViajeRespuestaDTO> findAll() {
        List<Viajes> viajes = (List<Viajes>) viajesRepository.findAll();
        actualizarEstados(viajes);
        return viajes.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViajeRespuestaDTO> listarViajesProgramados() {
        List<Viajes> viajes = viajesRepository.findByEstadoAndFechaSalida(State.PROGRAMADO, LocalDate.now());
        actualizarEstados(viajes);
        return viajes.stream()
                .filter(v -> v.getEstado() == State.PROGRAMADO)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<ViajeRespuestaDTO> listarViajesProgramadosPorFecha(LocalDate fecha) {
        List<Viajes> viajes = viajesRepository.findByEstadoAndFechaSalida(State.PROGRAMADO, fecha);
        actualizarEstados(viajes);
        return viajes.stream()
                .filter(v -> v.getEstado() == State.PROGRAMADO)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ViajeRespuestaDTO> findById(Long id) {
        return viajesRepository.findById(id).map(this::toDto);
    }

    @Override
    @Transactional
    public ViajeRespuestaDTO save(CrearViajeDTO dto) {
        // Validar existencia de Bus, User y Ruta
        BusDTO bus = busClient.getBusById(dto.getBusId());
        UserDTO chofer = userClient.getUserById(dto.getUserId());
        Rutas ruta = rutasRepository.findById(dto.getRutaId())
                .orElseThrow(() -> new RuntimeException("Ruta no existe"));
        if (bus == null) throw new RuntimeException("Bus no existe");
        if (chofer == null) throw new RuntimeException("Chofer no existe");

        Viajes viaje = new Viajes();
        viaje.setBusId(dto.getBusId());
        viaje.setUserId(dto.getUserId());
        viaje.setRutas(ruta);
        viaje.setFechaSalida(dto.getFechaSalida());
        viaje.setHoraSalida(dto.getHoraSalida());
        viaje.setFechaLlegada(dto.getFechaLlegada());
        viaje.setHoraLLegada(dto.getHoraLLegada());
        viaje.setEstado(State.PROGRAMADO);
        viaje.setPrecio(dto.getPrecio());

        return toDto(viajesRepository.save(viaje));
    }

    @Override
    @Transactional
    public Optional<ViajeRespuestaDTO> update(ActualizarViajeDTO dto, Long id) {
        Optional<Viajes> viajeOpt = viajesRepository.findById(id);
        return viajeOpt.map(v -> {
            v.setFechaSalida(dto.getFechaSalida());
            v.setHoraSalida(dto.getHoraSalida());
            v.setFechaLlegada(dto.getFechaLlegada());
            v.setHoraLLegada(dto.getHoraLLegada());
            v.setPrecio(dto.getPrecio());
            return toDto(viajesRepository.save(v));
        });
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        viajesRepository.deleteById(id);
    }



    private void actualizarEstados(List<Viajes> viajes) {
        LocalDateTime ahora = LocalDateTime.now();
        for (Viajes v : viajes) {
            LocalDateTime salida = LocalDateTime.of(
                    v.getFechaSalida(), v.getHoraSalida().toLocalTime());
            LocalDateTime llegada = LocalDateTime.of(
                    v.getFechaLlegada(), v.getHoraLLegada().toLocalTime());

            if (ahora.isBefore(salida)) {
                v.setEstado(State.PROGRAMADO);
            } else if (!ahora.isBefore(salida) && ahora.isBefore(llegada)) {
                v.setEstado(State.EN_CURSO);
            } else if (!ahora.isBefore(llegada)) {
                v.setEstado(State.COMPLETADO);
            }

        }
    }

    private ViajeRespuestaDTO toDto(Viajes viaje) {
        UserDTO chofer = userClient.getUserById(viaje.getUserId());
        ViajeRespuestaDTO dto = new ViajeRespuestaDTO();
        dto.setId(viaje.getId());
        dto.setOrigen(viaje.getRutas().getOrigen());
        dto.setDestino(viaje.getRutas().getDestino());
        dto.setFechaSalida(viaje.getFechaSalida());
        dto.setHoraSalida(viaje.getHoraSalida());
        dto.setFechaLlegada(viaje.getFechaLlegada());
        dto.setHoraLLegada(viaje.getHoraLLegada());
        dto.setBusId(viaje.getBusId());
        dto.setUserId(viaje.getUserId());
        dto.setEstado(viaje.getEstado());
        dto.setPrecio(viaje.getPrecio());
        if (chofer != null) {
            dto.setNombreChofer(chofer.getName());
            dto.setApellidoChofer(chofer.getLastName());
        }
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerRutasPopulares() {
        List<Viajes> viajes =  StreamSupport
                .stream(viajesRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        Map<String, Long> conteoPorRuta = viajes.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getRutas().getOrigen() + " - " + v.getRutas().getDestino(),
                        Collectors.counting()
                ));

        long total = conteoPorRuta.values().stream().mapToLong(Long::longValue).sum();

        return conteoPorRuta.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue())) // ordenar de mayor a menor
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("route", entry.getKey());
                    map.put("trips", entry.getValue());
                    map.put("percentage", (int) ((entry.getValue() * 100.0) / total));
                    return map;
                })
                .collect(Collectors.toList());
    }

}
