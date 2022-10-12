package com.nineplus.bestwork.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.nineplus.bestwork.dto.PageResponseDTO;
import com.nineplus.bestwork.dto.RPageDTO;

@Component
public class PageUtils {
	@Autowired
	BestWorkBeanUtils bestWorkBeanUtils;

	/**
	 * convert page to page response DTO
	 * 
	 * @param page
	 * @param dtoClass
	 * 
	 * @return PageResponseDTO<DTO>
	 */
	public <D, E> PageResponseDTO<D> convertPageEntityToDTO(Page<E> page, Class<D> dtoClass) {
		PageResponseDTO<D> pageResDTO = new PageResponseDTO<>();
		RPageDTO rPageDTO = new RPageDTO();
//		rPageDTO.setPageable(page.getPageable());
//		rPageDTO.setLast(page.isLast());
		rPageDTO.setTotalPages(page.getTotalPages());
		rPageDTO.setTotalElements(page.getTotalElements());
		rPageDTO.setSize(page.getSize());
		rPageDTO.setNumber(page.getNumber());
//		rPageDTO.setSort(page.getSort());
//		rPageDTO.setFirst(page.isFirst());
//		rPageDTO.setNumberOfElements(page.getNumberOfElements());
//		rPageDTO.setEmpty(page.isEmpty());

		pageResDTO.setMetaData(rPageDTO);
		pageResDTO.setContent(bestWorkBeanUtils.copyListToList(page.getContent(), dtoClass));

		return pageResDTO;
	}

}
