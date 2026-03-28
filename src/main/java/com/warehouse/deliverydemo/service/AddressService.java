package com.warehouse.deliverydemo.service;

import com.warehouse.deliverydemo.entity.Address;
import com.warehouse.deliverydemo.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressMapper addressMapper;
    
    public List<Address> findByUserId(Long userId) {
        return addressMapper.findByUserId(userId);
    }
    
    public Address findById(Long id) {
        return addressMapper.findById(id);
    }
    
    @Transactional
    public void save(Address address) {
        if (address.getIsDefault() == 1) {
            // 将该用户的其他地址设为非默认
            addressMapper.updateDefaultByUserId(address.getUserId());
        }
        
        if (address.getId() == null) {
            // 新增地址
            addressMapper.insert(address);
        } else {
            // 更新地址
            addressMapper.update(address);
        }
    }
    
    public void delete(Long id) {
        addressMapper.delete(id);
    }
    
    @Transactional
    public void setDefault(Long id, Long userId) {
        // 将该用户的其他地址设为非默认
        addressMapper.updateDefaultByUserId(userId);
        
        // 将当前地址设为默认
        Address address = addressMapper.findById(id);
        if (address != null) {
            address.setIsDefault(1);
            addressMapper.update(address);
        }
    }
    
    public Address findDefaultByUserId(Long userId) {
        return addressMapper.findDefaultByUserId(userId);
    }
}