package com.orquideas.microservice_parcels.service;

import com.orquideas.microservice_parcels.DTO.CreateEncomiendaDTO;
import com.orquideas.microservice_parcels.DTO.ResponseEncomiendaDTO;
import com.orquideas.microservice_parcels.DTO.UserDTO;
import com.orquideas.microservice_parcels.client.UserClient;
import com.orquideas.microservice_parcels.entities.Encomienda;
import com.orquideas.microservice_parcels.enums.Paquete;
import com.orquideas.microservice_parcels.enums.State;
import com.orquideas.microservice_parcels.repository.EncomiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EncomiendaServiceImpl implements IEncomeindaService{

    @Autowired
    private EncomiendaRepository encomiendaRepository;

    @Autowired
    private UserClient userClient;


    @Override
    @Transactional
    public ResponseEncomiendaDTO iniciarPagoViaje(CreateEncomiendaDTO dto) throws Exception {
        Paquete tipo;
        try {
            tipo = Paquete.valueOf(dto.getTipo());
        } catch (IllegalArgumentException e) {
            throw new Exception("Tipo de paquete inválido: " + dto.getTipo());
        }

        // Crear nueva encomienda
        Encomienda encomienda = new Encomienda();
        encomienda.setUserId(dto.getUserId());
        encomienda.setTipo(tipo);
        encomienda.setOrigen(dto.getOrigen());
        encomienda.setDestino(dto.getDestino());
        encomienda.setDniDestino(dto.getDniDestino());
        encomienda.setNombreDestino(dto.getNombreDestino());
        encomienda.setApellidoDestino(dto.getApellidoDestino());
        encomienda.setEstado(State.PENDIENTE);
        encomienda.setPrecio(calcularPrecioPorTipo(tipo));

        encomienda = encomiendaRepository.save(encomienda);

        encomienda.setCodigo(encomienda.generateCodigo(encomienda.getId()));
        encomienda.setClave(Encomienda.generateClaveConId(encomienda.getId()));

        encomienda = encomiendaRepository.save(encomienda);

        UserDTO user = userClient.getUserById(encomienda.getUserId());

        return toResponseDTO(encomienda, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResponseEncomiendaDTO> findById(Long id) {
        return encomiendaRepository.findById(id)
                .map(encomienda -> {
                    UserDTO userDTO = userClient.getUserById(encomienda.getUserId());
                    return toResponseDTO(encomienda,userDTO);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseEncomiendaDTO> findAll() {
        List<Encomienda> encomiendas = (List<Encomienda>) encomiendaRepository.findAll();

        return encomiendas.stream()
                .map(encomienda -> {
                    UserDTO user = userClient.getUserById(encomienda.getUserId());
                    return toResponseDTO(encomienda, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        encomiendaRepository.deleteById(id);
    }



    @Override
    @Transactional
    public Optional<Encomienda> update(Encomienda encomienda, Long id) {
        Optional<Encomienda> existingEncomienda = encomiendaRepository.findById(id);

        if (existingEncomienda.isPresent()) {
            Encomienda actual = existingEncomienda.get();

            // Actualiza los campos necesarios
            actual.setEstado(encomienda.getEstado());
            // ... otros campos relevantes

            // Guarda y devuelve la encomienda actualizada
            Encomienda updated = encomiendaRepository.save(actual);
            return Optional.of(updated);
        } else {
            // No se encontró la encomienda con el ID proporcionado
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResponseEncomiendaDTO> findByCodigo(String codigo) {
        return encomiendaRepository.findByCodigo(codigo)
                .map(encomienda -> {
                    UserDTO userDTO = userClient.getUserById(encomienda.getUserId());
                    return toResponseDTO(encomienda, userDTO);
                });
    }
    @Override
    public ResponseEncomiendaDTO confirmarPago(Long id) throws Exception {
        return null;
    }


    //new :,
    @Override
    @Transactional
    public ResponseEncomiendaDTO actualizarEstado(Long id, State nuevoEstado) throws Exception {
        Encomienda encomienda = encomiendaRepository.findById(id)
                .orElseThrow(() -> new Exception("Encomienda no encontrada"));
        encomienda.setEstado(nuevoEstado);
        encomiendaRepository.save(encomienda); // Guardar los cambios
        // Convertir a DTO y devolver
        return toResponseDTO(encomienda);
    }
    private ResponseEncomiendaDTO toResponseDTO(Encomienda encomienda) {
        ResponseEncomiendaDTO dto = new ResponseEncomiendaDTO();
        // Mapeo de campos...
        return dto;
    }

    //hasta aca jack
    private Double calcularPrecioPorTipo(Paquete tipo) {
        return switch (tipo) {
            case SOBRE_A4 -> 10.0;
            case CAJA_S -> 20.0;
            case CAJA_M -> 27.0;
            case CAJA_XX -> 34.0;
            case CAJA_XXL -> 40.0;
        };
    }
    private ResponseEncomiendaDTO toResponseDTO(Encomienda e, UserDTO user) {
        ResponseEncomiendaDTO dto = new ResponseEncomiendaDTO();
        dto.setId(e.getId());
        dto.setUser(user);
        dto.setTipo(e.getTipo().name());
        dto.setOrigen(e.getOrigen());
        dto.setDestino(e.getDestino());
        dto.setDniDestino(e.getDniDestino());
        dto.setNombreDestino(e.getNombreDestino());
        dto.setApellidoDestino(e.getApellidoDestino());
        dto.setEstado(e.getEstado().name());
        dto.setCodigo(e.getCodigo());
        dto.setPrecio(e.getPrecio());
        dto.setClave(e.getClave());
        return dto;
    }
}
