package com.steelerose.crm.repository;

import com.steelerose.crm.model.CallList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallListRepository extends JpaRepository<CallList, Long> {


  //  Page<CallList> findAllByRefNumberIsLike(String refNumber, Pageable pageable);
    List<CallList> findAllByArchiveFalseOrArchiveNull();
    List<CallList> findAllByArchiveTrue();
}
