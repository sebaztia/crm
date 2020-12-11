package com.steelerose.crm.service;

import com.steelerose.crm.model.CallList;
import com.steelerose.crm.model.Client;
import com.steelerose.crm.web.dto.CallListDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CallListService {
    List<CallList> getAllCallLists();
    List<CallList> getAllArchiveList();
    void saveCallList(CallListDto dto);
    CallListDto getCallListById(Long id);
    void deleteCallListById(long id);

    void archiveCallListById(long id);

    void rollbackCallListById(long id);

    void emailActionDone(long id);

    void callActionDone(long id);
    /*Page<CallList> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);*/

    /*Page<CallList> findPaginatedByRefNumberIsLike(int pageNo, int pageSize, String sortField, String sortDirection, String refNumber);*/

}
