package com.steelerose.crm.controller;

import com.steelerose.crm.model.CallList;
import com.steelerose.crm.model.Client;
import com.steelerose.crm.service.*;
import com.steelerose.crm.web.dto.CallListDto;
import com.steelerose.crm.web.dto.ClientDto;
import com.steelerose.crm.web.dto.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class CallListController {

    @Autowired
    private CallListService callListService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("listCalls", callListService.getAllCallLists());
        return "home";
      //  return findPaginated(1, "contactName", "asc", model);
    }

   /* @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 10;

        Page<CallList> page = callListService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List< CallList > listCalls = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listCalls", listCalls);
        return "home";
    }*/

   /* @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("listCalls", callListService.getAllClients());
        return "home";
    }*/

    @GetMapping("/showNewCallListForm")
    public String showNewCallListForm(Model model) {
        // create model attribute to bind form data
        CallListDto callListDto = new CallListDto();
        callListDto.setRecordDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
      //  List<Staff> staffList = staffService.getAllStaffs();

       // callListDto.setStaffList(staffList);
      //  model.addAttribute("staffName", staffList);
        model.addAttribute("call_list", callListDto);
        return "new_call_list";
    }
    @GetMapping("/showArchive")
    public String showArchive(Model model) {

        model.addAttribute("archiveList", callListService.getAllArchiveList());
        return "archive_list";
    }

    @PostMapping("/saveCallList")
    public String saveCallList(@ModelAttribute("call_list") @Valid CallListDto callListDto, BindingResult result) {

        if (result.hasErrors()) {
            return "new_call_list";
        }
        callListService.saveCallList(callListDto);
        Long callId = callListDto.getId();
        String reference = callListDto.getReference();

        if ((null != reference && !reference.equals("")) && (null != callId && !callId.equals(""))) {

            log.info(reference + ", ===== the callId : " + callId);
            ClientDto clientDto = clientService.getClientByCallListId(callId);

            if (clientDto == null) {
                clientDto = new ClientDto();
                clientDto.setCallId(callListDto.getId());
            }
            clientDto.setFullName(callListDto.getContactName());
            clientDto.setCallSheet(callListDto.getQuery());
            clientDto.setRecordDate(callListDto.getRecordDate());
            clientDto.setRefNumber(callListDto.getReference());

            clientService.saveClient(clientDto);
        }

        return "redirect:/";
    }

    @GetMapping("/showCallListForUpdate/{id}")
    public String showCallListForUpdate(@PathVariable(value = "id") long id, Model model) {

        CallListDto callListDto = callListService.getCallListById(id);

        model.addAttribute("call_list", callListDto);
        return "update_call_list";
    }

    @GetMapping("/deleteCallList/{id}")
    public String deleteCallList(@PathVariable(value = "id") long id) {

        this.callListService.deleteCallListById(id);
        return "redirect:/showArchive";
    }
    @GetMapping("/archiveCallList/{id}")
    public String archiveCallList(@PathVariable(value = "id") long id) {

        this.callListService.archiveCallListById(id);
        return "redirect:/";
    }
    @GetMapping("/restoreCallList/{id}")
    public String restoreCallList(@PathVariable(value = "id") long id) {

        this.callListService.rollbackCallListById(id);
        return "redirect:/showArchive";
    }

    @GetMapping("/emailActionDone/{id}")
    public String emailActionDone(@PathVariable(value = "id") long id) {

        this.callListService.emailActionDone(id);
        return "redirect:/";
    }
    @GetMapping("/callActionDone/{id}")
    public String callActionDone(@PathVariable(value = "id") long id) {

        this.callListService.callActionDone(id);
        return "redirect:/";
    }

    @GetMapping("/showCallListEmailForm/{id}")
    public String showCallListEmailForm(@PathVariable(value = "id") long id, Model model) {

        CallListDto callListDto = callListService.getCallListById(id);

        EmailDto emailDto = new EmailDto();
        emailDto.setCallListDto(callListDto);
        emailDto.setSubject("CRM Call List Information");
        emailDto.setText("Hi, \nPlease find the below call sheet details." +
                "\n\nContact Name: " + callListDto.getContactName() +
                            "\nContact Number: " + callListDto.getContactNumber() +
                "\nQuery: " + callListDto.getQuery()+
                "\nStaff Name: " +callListDto.getStaffName());

        model.addAttribute("email_list", emailDto);
        return "email_call_list";
    }
    @PostMapping("/sendEmailCallList")
    public String sendEmailCallList(@ModelAttribute("email_list") @Valid EmailDto emailDto, BindingResult result) {

        if (result.hasErrors()) {
            return "email_call_list";
        }

        String subject = emailDto.getSubject();
        String text = emailDto.getText();
        if (null == subject || subject.equals("")) {
            subject = "CRM Call List Information";
        }
        if (null == text || text.equals("")) {
            text = "Please find the below call sheet details.";
        }

        log.info("====-=---- email service:" + emailService);
        emailService.sendSimpleMessage(emailDto.getTo(), subject, text);

        return "redirect:/";
    }

}
