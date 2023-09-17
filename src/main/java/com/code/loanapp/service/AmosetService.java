package com.code.loanapp.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;

import com.code.loanapp.model.Amoset;

public interface AmosetService {
	List<Amoset> getAllAmoset();
	void saveAmoset(Amoset amoset);
	Amoset getAmosetById(String code);
	void deleteAmosetById(String code);
	Page<Amoset> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	public BigDecimal sumLoan();
	public BigDecimal sumtotal();
}
