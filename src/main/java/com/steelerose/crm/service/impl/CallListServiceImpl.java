package com.steelerose.crm.service.impl;

import com.steelerose.crm.model.CallList;
import com.steelerose.crm.model.Client;
import com.steelerose.crm.repository.CallListRepository;
import com.steelerose.crm.service.CallListService;
import com.steelerose.crm.service.ClientService;
import com.steelerose.crm.web.dto.CallListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CallListServiceImpl implements CallListService {

    @Autowired
    CallListRepository callListRepository;

    @Override
    public List<CallList> getAllCallLists() {

        return callListRepository.findAllByArchiveFalseOrArchiveNull();
    }

    @Override
    public List<CallList> getAllArchiveList() {
        return callListRepository.findAllByArchiveTrue();
    }

    @Override
    public void saveCallList(CallListDto dto) {
        CallList callList = new CallList();

        if (dto.getId() > 0) {
            callList.setId(dto.getId());
        }
        callList.setContactName(dto.getContactName());
        callList.setQuery(dto.getQuery());
        callList.setRecordDate(Date.from(dto.getRecordDate().atZone(ZoneId.systemDefault()).toInstant()));
        callList.setContactNumber(dto.getContactNumber());
      //  callList.setUserId(1L);
        callList.setRefNumber(dto.getReference());
        callList.setStaffName(dto.getStaffName());

        callList.setEmailCheck(dto.isEmailCheck());
        callList.setCallCheck(dto.isCallCheck());
        callList.setEmailDone(dto.isEmailDone());
        callList.setCallDone(dto.isCallDone());

        this.callListRepository.save(callList);
    }

    @Override
    public CallListDto getCallListById(Long id) {
        Optional< CallList > optional = callListRepository.findById(id);
        CallList callList;
        if (optional.isPresent()) {
            callList = optional.get();
        } else {
            throw new RuntimeException(" CallList not found for id :: " + id);
        }
        return toCallListDto(callList);
    }

    private CallListDto toCallListDto(CallList callList) {

        CallListDto callListDto = new CallListDto();

        callListDto.setId(callList.getId());
        callListDto.setContactName(callList.getContactName());
        callListDto.setQuery(callList.getQuery());
        if (null != callList.getRecordDate())
            callListDto.setRecordDate(callList.getRecordDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        callListDto.setContactNumber(callList.getContactNumber());
        //  callListDto.setUserId(1L);
        callListDto.setReference(callList.getRefNumber());
        callListDto.setStaffName(callList.getStaffName());
        callListDto.setEmailCheck(callList.getEmailCheck() ==  null ? false : callList.getEmailCheck());
        callListDto.setCallCheck(callList.getCallCheck() ==  null ? false : callList.getCallCheck());
        callListDto.setEmailDone(callList.getEmailDone() ==  null ? false : callList.getEmailDone());
        callListDto.setCallDone(callList.getCallDone() ==  null ? false : callList.getCallDone());

        return callListDto;
    }

    @Override
    public void deleteCallListById(long id) { this.callListRepository.deleteById(id);
    }
    @Override
    public void archiveCallListById(long id) {
        Optional< CallList > optional = callListRepository.findById(id);
        CallList callList;
        if (optional.isPresent()) {
            callList = optional.get();
        } else {
            throw new RuntimeException(" CallList not found for id :: " + id);
        }
        callList.setArchive(true);

        this.callListRepository.save(callList);
    }
    @Override
    public void rollbackCallListById(long id) {
        Optional< CallList > optional = callListRepository.findById(id);
        CallList callList;
        if (optional.isPresent()) {
            callList = optional.get();
        } else {
            throw new RuntimeException(" CallList not found for id :: " + id);
        }
        callList.setArchive(false);

        this.callListRepository.save(callList);
    }

    @Override
    public void emailActionDone(long id) {
        Optional< CallList > optional = callListRepository.findById(id);
        CallList callList;
        if (optional.isPresent()) {
            callList = optional.get();
        } else {
            throw new RuntimeException(" CallList not found for id :: " + id);
        }
        callList.setEmailDone(true);

        this.callListRepository.save(callList);
    }

    @Override
    public void callActionDone(long id) {
        Optional< CallList > optional = callListRepository.findById(id);
        CallList callList;
        if (optional.isPresent()) {
            callList = optional.get();
        } else {
            throw new RuntimeException(" CallList not found for id :: " + id);
        }
        callList.setCallDone(true);

        this.callListRepository.save(callList);
    }

   /* @Override
    public Page<CallList> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.callListRepository.findAll(pageable);
    }*/

    /*@Override
    public Page<CallList> findPaginatedByRefNumberIsLike(int pageNo, int pageSize, String sortField, String sortDirection, String refNumber) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.callListRepository.findAllByRefNumberIsLike(refNumber, pageable);
    }*/
}
