package com.hotel.Hotel.service;

import com.hotel.Hotel.domain.*;
import com.hotel.Hotel.dto.request.CrearReservaRequest;
import com.hotel.Hotel.exception.RecursoNoEncontradoException;
import com.hotel.Hotel.exception.ReglaNegocioException;
import com.hotel.Hotel.mapper.ReservaMapper;
import com.hotel.Hotel.repository.ClienteRepository;
import com.hotel.Hotel.repository.HabitacionRepository;
import com.hotel.Hotel.repository.ReservaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceCrearTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaService reservaService;

    // PRUEBA 4 (con mocks)
    @Test
    @DisplayName("No debe crear la reserva si el cliente está inactivo")
    void noDebeCrearReservaSiClienteInactivo() {
        // Arrange
        UUID clienteId = UUID.randomUUID();
        UUID habitacionId = UUID.randomUUID();

        Cliente clienteInactivo = new Cliente("Pedro Ruiz", "pedro@correo.com");
        clienteInactivo.registrarPenalizacion();
        clienteInactivo.registrarPenalizacion();
        clienteInactivo.registrarPenalizacion();

        Habitacion habitacion = new HabitacionEstandar("305", 2, 150.0, 2);

        CrearReservaRequest request = new CrearReservaRequest(
                clienteId, habitacionId,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3));

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteInactivo));
        when(habitacionRepository.findById(habitacionId)).thenReturn(Optional.of(habitacion));

        // Act + Assert
        assertThrows(ReglaNegocioException.class, () -> reservaService.crear(request));

        // Nunca se debe guardar la reserva
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    // PRUEBA 5 (con mocks)
    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el cliente no existe")
    void debeLanzarExcepcionSiClienteNoExiste() {
        // Arrange
        UUID clienteId = UUID.randomUUID();
        CrearReservaRequest request = new CrearReservaRequest(
                clienteId, UUID.randomUUID(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        // Act + Assert
        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class,
                () -> reservaService.crear(request));

        assertTrue(ex.getMessage().contains(clienteId.toString()));
        verifyNoInteractions(habitacionRepository, reservaRepository, reservaMapper);
    }
}