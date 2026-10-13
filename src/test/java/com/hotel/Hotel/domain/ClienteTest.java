package com.hotel.Hotel.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    // PRUEBA 1 (normal, sin mocks)
    @Test
    @DisplayName("El cliente se desactiva al acumular 3 penalizaciones")
    void debeDesactivarClienteConTresPenalizaciones() {
        Cliente cliente = new Cliente("Ana Pérez", "ana@correo.com");

        // Act
        cliente.registrarPenalizacion();
        cliente.registrarPenalizacion();
        cliente.registrarPenalizacion();

        // Assert
        assertEquals(3, cliente.getPenalizaciones());
        assertFalse(cliente.isActivo());
        assertFalse(cliente.puedeRealizarReservas());
    }

    // PRUEBA 2 (normal, sin mocks)
    @Test
    @DisplayName("No se puede crear un cliente con el nombre vacío")
    void debeLanzarExcepcionSiNombreVacio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Cliente("   ", "ana@correo.com"));

        assertEquals("El nombre es obligatorio", ex.getMessage());
    }
}