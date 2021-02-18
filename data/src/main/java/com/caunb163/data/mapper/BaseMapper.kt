package com.caunb163.data.mapper

interface BaseMapper<M, E> {

    fun toModel(entity: E): M
    fun toEntity(model: M): E
}