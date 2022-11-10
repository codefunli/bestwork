package com.nineplus.bestwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.PostInvoice;

@Repository
public interface PostInvoiceRepository extends JpaRepository<PostInvoice, Long> {

}