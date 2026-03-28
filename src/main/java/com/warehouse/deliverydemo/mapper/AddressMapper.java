package com.warehouse.deliverydemo.mapper;

import com.warehouse.deliverydemo.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {
    List<Address> findByUserId(@Param("userId") Long userId);
    Address findById(@Param("id") Long id);
    void insert(Address address);
    void update(Address address);
    void delete(@Param("id") Long id);
    void updateDefaultByUserId(@Param("userId") Long userId);
    Address findDefaultByUserId(@Param("userId") Long userId);
}