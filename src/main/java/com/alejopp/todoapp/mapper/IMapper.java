package com.alejopp.todoapp.mapper;

public interface IMapper <I, O>{ // Datos <entrada, salida>
    public O map(I in);
}
