//package acme.features.inventor.rustora;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//
//import acme.entities.rustora.Rustora;
//import acme.framework.controllers.AbstractController;
//import acme.roles.Inventor;
//
//@Controller
//public class InventorChimpumController extends AbstractController<Inventor, Rustora>{
//
//	@Autowired
//	protected InventorChimpumListService listService;
//
//	@Autowired
//	protected InventorChimpumShowService showService;
//	
////	@Autowired
////	protected InventorChimpumCreateService createService;
////	
////	@Autowired
////	protected InventorChimpumUpdateService updateService;
////
////	@Autowired
////	protected InventorChimpumDeleteService deleteService;
//
//	@PostConstruct
//	protected void initialise() {
//		super.addCommand("list", this.listService);
//		super.addCommand("show", this.showService);
////		super.addCommand("create", this.createService);
////		super.addCommand("update", this.updateService);
////		super.addCommand("delete", this.deleteService);
//	}
//
//}