package com.global.ecommerce.Entity;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity <ID>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ID id;
}
