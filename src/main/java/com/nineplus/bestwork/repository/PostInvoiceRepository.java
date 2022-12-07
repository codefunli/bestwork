package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.PostInvoice;

@Repository
public interface PostInvoiceRepository extends JpaRepository<PostInvoice, Long> {
	List<PostInvoice> findByAirWayBill(long awbId);

	PostInvoice findByIdAndAirWayBill(Long postInvoiceId, String airWayBill);

	@Query(value = "SELECT path_file_server FROM FILE_STORAGE WHERE id = :fileId AND post_invoice_id = :invoicePostId", nativeQuery = true)
	String getPathFileServer(long invoicePostId, long fileId);

	@Query(value = "SELECT  aw.id as awbId, aw.code as code, ft.post_invoice_id as postInvoiceId, ft.id as fileId, ft.type as type, ft.name as name, ft.path_file_server as pathFileServer "
			+ "FROM FILE_STORAGE ft "
			+ "JOIN POST_INVOICE pi ON ft.post_invoice_id = pi.id "
			+ "JOIN AIRWAY_BILL aw ON  aw.code = pi.airway_bill "
			+ "WHERE aw.id = :awbId AND ft.is_choosen = 1", nativeQuery = true)
	List<InvoiceFileProjection> getClearanceInfo(long awbId);
}