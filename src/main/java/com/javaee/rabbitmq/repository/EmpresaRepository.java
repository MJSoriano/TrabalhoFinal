package com.javaee.rabbitmq.repository;

import org.springframework.data.repository.CrudRepository;
import com.javaee.rabbitmq.domain.Empresa;

public interface EmpresaRepository extends CrudRepository<Empresa, Long>{

}
