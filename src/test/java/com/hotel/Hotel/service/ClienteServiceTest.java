package com.hotel.Hotel.service;

import com.hotel.Hotel.domain.Cliente;
import com.hotel.Hotel.dto.request.CrearClienteRequest;
import com.hotel.Hotel.dto.response.ClienteResponse;
import com.hotel.Hotel.mapper.ClienteMapper;
import com.hotel.Hotel.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    // PRUEBA 3 (con mocks)
    @Test
    @DisplayName("Debe crear un cliente, guardarlo en el repositorio y devolver el response")
    void debeCrearClienteExitosamente() {
        // Arrange
        CrearClienteRequest request = new CrearClienteRequest("Luis Torres", "luis@correo.com");
        ClienteResponse responseEsperado = new ClienteResponse(
                UUID.randomUUID(), "Luis Torres", "luis@correo.com", true, 0);

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));
        when(clienteMapper.toResponse(any(Cliente.class))).thenReturn(responseEsperado);

        // Act
        ClienteResponse resultado = clienteService.crear(request);

        // Assert
        assertEquals("Luis Torres", resultado.nombre());
        assertTrue(resultado.activo());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(clienteMapper, times(1)).toResponse(any(Cliente.class));
    }
}