package com.nineplus.bestwork.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.PackagePostReqDto;
import com.nineplus.bestwork.dto.PackagePostResDto;
import com.nineplus.bestwork.entity.PackagePost;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IPackagePostService {
	PackagePost savePackagePost(PackagePostReqDto packagePostReqDto, String airWayBillCode)
			throws BestWorkBussinessException;

	Optional<PackagePost> getPackagePost(Long packagePostId) throws BestWorkBussinessException;

	void updatePackagePost(List<MultipartFile> mFiles, PackagePostReqDto packagePostReqDto, String airWayCode)
			throws BestWorkBussinessException;

	public PackagePostResDto getDetailPackage(Long packagePostId) throws BestWorkBussinessException;
	
	List<PackagePostResDto> getAllPackagePost(String airWayBillCode) throws BestWorkBussinessException;
}
