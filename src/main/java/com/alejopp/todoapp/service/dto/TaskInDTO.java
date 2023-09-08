package com.alejopp.todoapp.service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskInDTO { // Datos de entrada para la creacion de taks

    private String title;
    private String description;
    private LocalDateTime eta; // Fecha de finalizacion estimada
}
