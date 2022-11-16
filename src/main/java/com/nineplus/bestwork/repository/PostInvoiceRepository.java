package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.PostInvoice;

@Repository
public interface PostInvoiceRepository extends JpaRepository<PostInvoice, Long> {
	List<PostInvoice> findByAirWayBill(String airWayBill);

	@Query(value = "SELECT path_file_server FROM FILE_STORAGE WHERE id = :fileId AND post_invoice_id = :invoicePostId", nativeQuery = true)
	String getPathFileServer(long invoicePostId, long fileId);
}