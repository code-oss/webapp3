package com.code.amortization.controller;


import java.util.List;

import com.code.amortization.model.AdminModel;
import com.code.amortization.model.Amoset;
import com.code.amortization.model.Clients;
import com.code.amortization.repository.AdminModelRep;
import com.code.amortization.service.AdminService;
import com.code.amortization.service.AmosetAddService;
import com.code.amortization.service.AmosetService;
import com.code.amortization.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ClientController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private AmosetService amosetService;

	@Autowired
	private AmosetAddService amosetAddService;
	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminModelRep adminModelRep;

	@GetMapping("/home/viewClients")
	public String viewHomePage(Model model) {
		return findPaginated(1, "id", "asc", model);
	}
	@GetMapping("/access_admin")
	public String LoginAdmin(){
		return "AdminDataLogin";
	}
	//view admins
	@GetMapping("/home/viewAdmins")
	public String viewAdmins(Model model) {
		return findPaginated4(1, "id", "asc", model);
	}
	@GetMapping("/home/addClient")
	public String showNewClientForm(Model model) {
		Clients client = new Clients();
		model.addAttribute("client", client);
		return "AddClient";
	}
	//admin add page
	@GetMapping("/home/addAdmin")
	public String showNewAdminForm(Model model) {
		AdminModel adminModel = new AdminModel();
		model.addAttribute("admins", adminModel);
		return "AddAdmin";
	}

	@PostMapping("/saveClient")
	public String saveClient(@ModelAttribute("client") Clients client) {
		clientService.saveClient(client);
		return "redirect:/home/viewClients";
	}
	//admin add page action
	@PostMapping("/saveAdmin")
	public String saveAdmin(@ModelAttribute("admins") AdminModel adminModel) {
		adminService.saveAdmin(adminModel);
		return "redirect:/home/viewAdmins";
	}


	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {


		Clients client = clientService.getClientById(id);

		model.addAttribute("client", client);
		return "UpdateClient";
	}
	//admin update page
	@GetMapping("/showFormForUpdateAdmin/{id}")
	public String showFormForUpdateAdmin(@PathVariable ( value = "id") long id, Model model) {


		AdminModel adminModel = adminService.getAdminById(id);

		model.addAttribute("admin", adminModel);
		return "UpdateAdmin";
	}

	@GetMapping("/clientView/{id}")
	public String showClient(@PathVariable ( value = "id") long id, Model model) {


		Clients client = clientService.getClientById(id);

		model.addAttribute("client", client);
		return "ViewClient";
	}

	@GetMapping("/deleteClient/{id}")
	public String deleteClient(@PathVariable (value = "id") long id) {

		this.clientService.deleteClientById(id);
		return "redirect:/home/viewClients";
	}
	//delete admin
	@GetMapping("/deleteAdmin/{id}")
	public String deleteAdmin(@PathVariable (value = "id") long id) {

		this.adminService.deleteAdminById(id);
		return "redirect:/home/viewAdmins";
	}

	//access admin settings
	@GetMapping("/Signin2")
	public String Login2(@RequestParam("username")String username, @RequestParam("password")String password , HttpSession
			session, ModelMap modelMap){
		AdminModel adminModel = adminModelRep.findAdminByUsernamePassword(username,password);

		if (adminModel !=null){
			String adminUser = adminModel.getUsername();
			String adminPass = adminModel.getPassword();

			if (username.equalsIgnoreCase(adminUser) && password.equalsIgnoreCase(adminPass)){
				session.setAttribute(username, "username");
				return "redirect:/home/viewAdmins";
			} else {
				modelMap.put("error", "details Un recorgnised");
				return "AdminDataLoginErr";
			}
		} else {
			modelMap.put("error", "details Un recorgnized");
			return "AdminDataLoginErr";
		}
	}


	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 10;

		Page<Clients> page = clientService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Clients> listClients = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listClients", listClients);
		return "ClientData";
	}


	@GetMapping("/pages/{pageNos}")
	public String findPaginated1(@PathVariable (value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 10;

		Page<Clients> page = clientService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Clients> listClients = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listClients", listClients);
		return "AmortizationSettings";
	}
	@GetMapping("/pages1/{pageNos1}")
	public String findPaginated2(@PathVariable (value = "pageNos1") int pageNo,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 10;

		Page<Amoset> page = amosetService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Amoset> listAmosets = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listAmosets", listAmosets);
		return "AmortizationData";
	}
	@GetMapping("/pages4/{pageNos4}")
	public String findPaginated4(@PathVariable (value = "pageNos4") int pageNo,
								 @RequestParam("sortField") String sortField,
								 @RequestParam("sortDir") String sortDir,
								 Model model) {
		int pageSize = 10;

		Page<AdminModel> page = adminService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<AdminModel> listAdmins = page.getContent();

		model.addAttribute("currentPage4", pageNo);
		model.addAttribute("totalPages4", page.getTotalPages());
		model.addAttribute("totalItems4", page.getTotalElements());

		model.addAttribute("sortField4", sortField);
		model.addAttribute("sortDir4", sortDir);
		model.addAttribute("reverseSortDir4", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("admins", listAdmins);
		return "AdminData";
	}

	//amortization settings

	@GetMapping("/home/amosetting")
	public String viewAmoset(Model model) {
		return findPaginated1(1, "id", "asc", model);
	}
	@GetMapping("/home/amo_settings_data")
	public String viewAmoset_data(Model model) {
		return findPaginated2(1, "code", "asc", model);
	}

	@PostMapping("/saveAmoset")
    public String saveAmoset(@RequestParam("c_id") String c_id,
            @RequestParam("f_name") String f_name,@RequestParam("location") String location,
            @RequestParam("interest") String interest, @RequestParam("loan_amt") String loan_amt,
            @RequestParam("no_years") String no_years, @RequestParam("month_pay") String month_pay,
			@RequestParam("total_payment") String total_payment, @RequestParam("status") String status,
			@RequestParam("amt_pend") String amt_pend, @RequestParam("date_e") String date_e,
			@RequestParam("profit") String profit
			) throws Exception {
        amosetAddService.saveAmosets(c_id,f_name,location,interest,loan_amt,no_years,month_pay,total_payment,status,
		amt_pend,date_e,profit);
        return "AmortizationSettings";
    }

	@GetMapping("/showFormForLoanUpdate/{id}")
	public String showFormForLoanUpdate(@PathVariable ( value = "id") long id, Model model) {


		Clients client = clientService.getClientById(id);

		model.addAttribute("client", client);
		return "AmortizationCalc";
	}


	@GetMapping("/AmosetView/{code}")
	public String showAmoset(@PathVariable ( value = "code") long code, Model model) {


		Amoset amoset = amosetService.getAmosetById(code);

		model.addAttribute("amoset", amoset);
		return "ViewAmoset";
	}
	@GetMapping("/deleteAmoset/{code}")
	public String deleteAmoset(@PathVariable (value = "code") long code) {

		this.amosetService.deleteAmosetById(code);
		return "redirect:/";
	}


}
