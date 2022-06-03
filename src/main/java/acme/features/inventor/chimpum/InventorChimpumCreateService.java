package acme.features.inventor.chimpum;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.chimpum.Chimpum;
import acme.entities.item.Item;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.features.authenticated.systemConfiguration.AuthenticatedSystemConfigurationRepository;
import acme.features.inventor.item.InventorItemRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractCreateService;
import acme.roles.Inventor;
import spamDetector.SpamDetector;

@Service
public class InventorChimpumCreateService implements AbstractCreateService<Inventor, Chimpum>{
	
	@Autowired
	protected InventorChimpumRepository inventorChimpumRepository;
	@Autowired
	protected InventorItemRepository inventorItemRepository;
	@Autowired
	protected AuthenticatedSystemConfigurationRepository systemConfigRepository;

	@Override
	public boolean authorise(final Request<Chimpum> request) {
		assert request != null;
		Item item;
		final int inventorId = request.getPrincipal().getActiveRoleId();
		final int itemId = request.getModel().getInteger("itemId");
		item = this.inventorItemRepository.findOneById(itemId);
		final int itemInventorId = this.inventorItemRepository.findOneById(itemId).getInventor().getId();

		return  inventorId == itemInventorId && item.isPublished(); 
	}

	@Override
	public void bind(final Request<Chimpum> request, final Chimpum entity, final Errors errors) {
		request.bind(entity, errors, "code", "title", "description", "budget", "creationMoment", "startDate", "endDate", "moreInfo");
	}

	@Override
	public void unbind(final Request<Chimpum> request, final Chimpum entity, final Model model) {
		request.unbind(entity, model, "code", "title", "description", "budget", "creationMoment", "startDate", "endDate", "moreInfo");	
		model.setAttribute("itemId", request.getModel().getInteger("itemId"));
	}

	@Override
	public Chimpum instantiate(final Request<Chimpum> request) {
		assert request != null;
		
		Chimpum res;
		Date now;
		now = new Date(System.currentTimeMillis() - 1);
	
		res = new Chimpum();
		res.setCreationMoment(now);
		final LocalDate creationMoment =  res.getCreationMoment().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		res.setCode(this.generateCode(creationMoment));
		return res;
	}

	@Override
	public void validate(final Request<Chimpum> request, final Chimpum entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

        final SystemConfiguration systemConfig = this.systemConfigRepository.findSystemConfiguration();
        final String StrongEN = systemConfig.getStrongSpamTermsEn();
        final String StrongES = systemConfig.getStrongSpamTermsEs();
        final String WeakEN = systemConfig.getWeakSpamTermsEn();
        final String WeakES = systemConfig.getWeakSpamTermsEs();

        final double StrongThreshold = systemConfig.getStrongThreshold();
        final double WeakThreshold = systemConfig.getWeakThreshold();
        
        if(!errors.hasErrors("title")) {
            final boolean res;
            res = SpamDetector.spamDetector(entity.getTitle(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
            errors.state(request, res, "title", "alert-message.form.spam");
        }
        
        if(!errors.hasErrors("description")) {
            final boolean res;
            res = SpamDetector.spamDetector(entity.getDescription(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
            errors.state(request, res, "description", "alert-message.form.spam");
        }
        
        if(!errors.hasErrors("moreInfo")) {
            final boolean res;
            res = SpamDetector.spamDetector(entity.getMoreInfo(),StrongEN,StrongES,WeakEN,WeakES,StrongThreshold,WeakThreshold);
            errors.state(request, res, "moreInfo", "alert-message.form.spam");
        }
		
		if(!errors.hasErrors("budget")) {
			final List<String> currencies = new ArrayList<>();
			String currency;
			Double amount;
			
			for(final String c: this.systemConfigRepository.acceptedCurrencies().split(",")) {
				currencies.add(c.trim());
			}
			
			currency = entity.getBudget().getCurrency();
			amount = entity.getBudget().getAmount();
			
			errors.state(request, currencies.contains(currency) , "budget","inventor.chimpum.form.error.currency");
			errors.state(request, amount>=0.00 , "budget","inventor.chimpum.form.error.negative-amount");
		}
		
		if(entity.getStartDate()!=null) {
			if(!errors.hasErrors("startDate")) {
				Date startDate;
				startDate = entity.getStartDate();

				final long diff1 = startDate.getTime() - entity.getCreationMoment().getTime();
				final TimeUnit time = TimeUnit.DAYS; 
		        final long diff2 = time.convert(diff1, TimeUnit.MILLISECONDS);
		        
		        errors.state(request, diff2>=30 , "startDate","inventor.chimpum.form.error.startDate");
			}
			
			if(!errors.hasErrors("endDate")) {
				Date endDate;
				endDate = entity.getEndDate();
				
				final long diff1 = endDate.getTime() - entity.getStartDate().getTime();
			    final TimeUnit time = TimeUnit.DAYS; 
			    final long diff2 = time.convert(diff1, TimeUnit.MILLISECONDS);
			        
			    errors.state(request, diff2>=7 , "endDate","inventor.chimpum.form.error.endDate");
			}
		}
	}

	@Override
	public void create(final Request<Chimpum> request, final Chimpum entity) {
		assert request != null;
		assert entity != null;
		Item item;
		
		this.inventorChimpumRepository.save(entity);
		final int itemId = request.getModel().getInteger("itemId");
		item = this.inventorItemRepository.findOneById(itemId);
		item.setChimpum(entity);
		this.inventorItemRepository.save(item);
	}
		
	public String generateCode(final LocalDate creationMoment) {
		String result = "";
		
		final Integer day1 = creationMoment.getDayOfMonth();
		final Integer month1 = creationMoment.getMonthValue();
		final Integer year1 = creationMoment.getYear();

		final String year2 = year1.toString().substring(2, 4);
		String month2= "";
		String day2= "";
		
		if(day1.toString().length()==1) {
			day2 = "0" + day1.toString();
		}else {
			day2 = day1.toString();
		}
		
		if(month1.toString().length()==1) {
			month2 = "0" + month1.toString();
		}else{
			month2 = month1.toString();
		}
					
		result = year2 + "-" + month2 + "-" + day2;
			
		return result;
	}
}