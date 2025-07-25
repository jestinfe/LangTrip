package kr.co.sist.e_learning.payment;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {
    void insertPayment(PaymentRequestDTO dto);
} 
  
