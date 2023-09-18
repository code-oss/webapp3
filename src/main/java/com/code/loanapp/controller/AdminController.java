package com.code.amortization.controller;



import com.code.amortization.dataExports.domain_clients.ClientExportService;
import com.code.amortization.dataExports.domain_transactions.TransactionExportService;
import com.code.amortization.model.AdminModel;
import com.code.amortization.repository.AdminModelRep;
import com.code.amortization.service.AdminModelserv;
import com.code.amortization.service.AmosetService;
import com.code.amortization.service.ClientService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.IOException;
import java.util.List;
@Controller
public class AdminController {
    @Autowired
    private AdminModelRep adminModelRep;

    @Autowired
    private AdminModelserv adminModelserv;

    @Autowired
    private AmosetService amosetService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientExportService clientExportService;

    @Autowired
    private TransactionExportService transactionExportService;

    @GetMapping("/")
    public String LoginHome(){
        return "Enter";
    }

    @GetMapping("/home/dashboard")
    public String viewIndex(Model model) {
        List<AdminModel>adminModels= adminModelserv.getAdminModels();
        model.addAttribute("adminModels" ,adminModels);
        return "index";
    }

    @GetMapping("/Signin")
    public String Login(@RequestParam("username")String username, @RequestParam("password")String password , HttpSession
            session, ModelMap modelMap){
        AdminModel adminModel = adminModelRep.findAdminByUsernamePassword(username,password);

        if (adminModel !=null){
            String adminUser = adminModel.getUsername();
            String adminPass = adminModel.getPassword();

            if (username.equalsIgnoreCase(adminUser) && password.equalsIgnoreCase(adminPass)){
                session.setAttribute(username, "username");
                return "redirect:/home/dashboard";
            } else {
                modelMap.put("error", "details Un recorgnised");
                return "ErrorPa";
            }
        } else {
            modelMap.put("error", "details Un recorgnized");
            return "ErrorPa";
        }
    }


    @GetMapping(value = "/home/logout")
    public String logout_user(HttpSession session)
    {
        session.removeAttribute("username");
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/export-to-excel-clients")
    public String exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Clients_Information.xlsx";
        response.setHeader(headerKey, headerValue);
        clientExportService.exportCToExcel(response);
        return "/home/dashboard";
    }

    @GetMapping("/export-to-excel-transactions")
    public String exportToExcel2(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Transactions_Information.xlsx";
        response.setHeader(headerKey, headerValue);
        transactionExportService.exportCToExcel2(response);
        return "/home/dashboard";
    }
}
