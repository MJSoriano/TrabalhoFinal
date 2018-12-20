package com.javaee.rabbitmq.repository;

import org.springframework.data.repository.CrudRepository;
import com.javaee.rabbitmq.domain.Comprador;

public interface CompradorRepository extends CrudRepository<Comprador, Long> {

}
