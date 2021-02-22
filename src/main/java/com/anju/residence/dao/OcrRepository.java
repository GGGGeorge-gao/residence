package com.anju.residence.dao;

import com.anju.residence.entity.Ocr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cygao
 * @date 2021/2/22 3:18 下午
 **/
@Repository
public interface OcrRepository extends JpaRepository<Ocr, Integer> {

}
