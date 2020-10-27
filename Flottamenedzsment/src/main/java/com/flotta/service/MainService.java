package com.flotta.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.flotta.entity.Device;
import com.flotta.entity.DeviceType;
import com.flotta.entity.Sim;
import com.flotta.entity.Subscription;
import com.flotta.entity.User;
import com.flotta.entity.switchTable.Service.UserDevService;
//import com.flotta.entity.switchTable.Service.UserSubService;
import com.flotta.entity.viewEntity.DeviceToView;
//import com.flotta.entity.viewEntity.InvoiceOfUserByNumber;
//import com.flotta.entity.viewEntity.OneCategoryOfUserFinance;
import com.flotta.entity.viewEntity.SubscriptionToView;
//import com.flotta.exception.UnknownPhoneNumberException;
import com.flotta.invoice.Category;
import com.flotta.invoice.ChargeRatioByCategory;
import com.flotta.invoice.Participant;
import com.flotta.invoice.RawInvoice;
import com.flotta.invoice.DescriptionCategoryCoupler;
import com.flotta.invoice.FeeItem;
import com.flotta.invoice.Invoice;
import com.flotta.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.invoice.exception.FileUploadException;
import com.flotta.invoice.service.BillingService;
import com.flotta.utility.MessageToView;
//import com.flotta.invoice.service.ChargeRatioService;


@Service
public class MainService {
	
	private SubscriptionService subscriptionService;
	
	private UserService userService;
	
	private SimService simService;
	
	private DeviceTypeService deviceTypeService;
	
	private DeviceService deviceService;
	
//	private SubSimService subSimService;
	
//	private UserSubService userSubService;
	
	private UserDevService userDevService;
	
//	private SubDevService subDevService;
	
//	private SubNoteService subNoteService;
	
//	private DevNoteService devNoteService;
	
	private BillingService billingService;
	
//	private ChargeRatioService chargeRatioService;

	@Autowired
	public MainService(SubscriptionService subscriptionService, UserService userService, SimService simService, DeviceTypeService deviceTypeService, DeviceService deviceService) {
		this.subscriptionService = subscriptionService;
		this.userService = userService;
		this.simService = simService;
		this.deviceTypeService = deviceTypeService;
		this.deviceService = deviceService;
		
	}
	
//	@Autowired
//	public void setSubSimService(SubSimService subSimService) {
//    this.subSimService = subSimService;
//  }
	
//	@Autowired
//	public void setUserSubService(UserSubService userSubService) {
//    this.userSubService = userSubService;
//  }
	
	@Autowired
	public void setUserService(UserService userService) {
    this.userService = userService;
  }

	@Autowired
  public void setUserDevService(UserDevService userDevService) {
    this.userDevService = userDevService;
  }
	
//	@Autowired
//	public void setSubDevService(SubDevService subDevService) {
//    this.subDevService = subDevService;
//  }
	
//	@Autowired
//	public void setSubNoteService(SubNoteService subNoteService) {
//    this.subNoteService = subNoteService;
//  }
	
//	@Autowired
//  public void setDevNoteService(DevNoteService devNoteService) {
//    this.devNoteService = devNoteService;
//  }
	
	@Autowired
  public void setBillingService(BillingService billingService) {
    this.billingService = billingService;
  }
	
//	@Autowired
//	public void setChargeRatioService(ChargeRatioService chargeRatioService) {
//    this.chargeRatioService = chargeRatioService;
//  }
	
	public Map<User, List<FeeItem>> splitting = new HashMap<>();
	
	
	//------- SUBSCRIPTION SERVICE --------


  public List<SubscriptionToView> findAllSubscription() {
		List<SubscriptionToView> list = new LinkedList<>();
		for(Subscription s : subscriptionService.findAll()) {
			list.add(s.toView());
		}
		return list;
	}
  
  public SubscriptionToView findSubscriptionById(long id) {
    return subscriptionService.findById(id).toView();
  }
  
  public SubscriptionToView findSubscriptionByIdAndDate(long id, LocalDate date) {
    return subscriptionService.findById(id).toView(date);
  }
	
	public SubscriptionToView findSubscriptionByNumber(String number) {
		return subscriptionService.findByNumber(number).toView();
	}
	
	public SubscriptionToView findSubscriptionByNumberAndDate(String number, LocalDate date) {
		return subscriptionService.findByNumber(number).toView(date);
	}
	
	public List<LocalDate> findSubscriptionDatesById(long id) {
	  return subscriptionService.findById(id).getAllModificationDateDesc();
	}
	
	public boolean addSubscription(SubscriptionToView stv) {
    Sim sim = simService.findByImei(stv.getImei());
    if(sim != null && subscriptionService.add(stv)) {
      Subscription saved = subscriptionService.findByNumber(stv.getNumber());
      saved.addSim(sim, null, stv.getDate());
      subscriptionService.save(saved);
      return true;
    }
    return false;
  }
	
	public boolean updateSubscription(SubscriptionToView stv) {
	  Subscription sub = subscriptionService.findById(stv.getId());
	  Sim sim = simService.findByImei(stv.getImei());
    User user = userService.findById(stv.getUserId());
    Device dev = deviceService.findById(stv.getDeviceId());
    
    sub.addSim(sim, stv.getSimChangeReason(), stv.getDate());
    sub.addUser(user, stv.getDate());
    sub.addDevice(dev, stv.getDate());
    sub.addNote(stv.getNote(), stv.getDate());
    subscriptionService.save(sub);
    return true;
  }
	
	public String getSubscriptionServiceError() {
    return subscriptionService.removeMsg();
  }
	
	//TODO put UserServiceImp function here
	//-------- USER SERVICE --------
	public boolean registerUser(User user) {
		if(userService.registerUser(user)) {
			return true;
		}
		return false;
	}
		
	public List<User> findAllUser() {
		return userService.findAll();
	}
	
	public User findUserByEmail(String email) {
		return userService.findByEmail(email);
	}
	
	public boolean changePassword(String oldPsw, String newPsw, String confirmPsw) {
    return userService.changePassword(oldPsw, newPsw, confirmPsw);
 }

 public List<MessageToView> getUserError() {
   MessageToView mtv = new MessageToView(userService.removeMsg());
   mtv.setToDanger();
   return Arrays.asList(mtv);
 }
 
 public boolean firstUserRegistration(User user) {
   return userService.firstUserRegistration(user);
 }
 
 public boolean registrationAvailable() {
   return userService.registrationAvailable();
 }
 
 public boolean activation(String key) {
   return userService.activation(key);
 }
 
//-------- SIM SERVICE --------
	
  public List<Sim> findAllFreeSim() {
    return simService.findAllFree();
  }

  public List<Sim> findAllSim() {
    return simService.findAll();
  }
  
  public Sim findSimById(int i) {
    return simService.findById(i);
  }
  
  public List<String> getSimChangeReasons() {
    return simService.getAllChagneReason();
  }
  
  public boolean addSim(Sim sim) {
    return simService.add(sim);
  }
  
//-------- DEVICE TYPE SERVICE --------

  public List<DeviceType> findAllDeviceTypes() {
    return deviceTypeService.findAll();
  }

  public List<String> findAllBrandOfDevicesType() {
    return deviceTypeService.findAllBrandOfDevicesType();
  }

  public void saveDeviceType(DeviceType deviceType) {
    deviceTypeService.save(deviceType);
  }

//-------- DEVICE SERVICE --------
  
//  private DeviceToView toView(Device device) {
//    DeviceToView dtv = new DeviceToView();
//    dtv.setId(device.getId());
//    dtv.setSerialNumber(device.getSerialNumber());
//    dtv.setTypeName(device.getDeviceType().getName());
//    dtv.setEditable(true);
//    
//    User user = userDevService.findLastUser(device);
//    if(user != null) {
//      dtv.setUserId(user.getId());
//      dtv.setUserName(user.getFullName());
//    } else {
//      dtv.setUserId(0);
//      dtv.setUserName("");
//    }
//    
//    Subscription sub = subDevService.findLastSub(device);
//    if(sub != null) {
//      dtv.setNumber(sub.getNumber());
//    } else {
//      dtv.setNumber("");
//    }
//    
//    DevNote note = devNoteService.findLastNote(device);
//    
//    if(note != null) {
//      dtv.setNote(note.getNote());
//    } else {
//      dtv.setNote("");
//    }
//    
//    return dtv;
//  }
  
  public List<DeviceToView> findAllDevices() {
    List<DeviceToView> list = new LinkedList<>();
    deviceService.findAll().forEach(
        d -> list.add(d.toView()));
    return list;
  }
  
  public List<DeviceToView> findAllDevicesByUser(long userId) {
    User user = userService.findById(userId);
    List<DeviceToView> result = new LinkedList<>();
    userDevService.findAllFreeDeviceByUser(user).forEach(d -> {
      result.add(d.toView());
    });
    return result ;
  }

  public boolean saveDevice(DeviceToView dtv) {
    DeviceType deviceType = deviceTypeService.findByName(dtv.getTypeName());
    if(deviceType != null && deviceService.add(dtv)) {
      Device saved = deviceService.findBySerialNumber(dtv.getSerialNumber());
      saved.setDeviceType(deviceType);
      deviceService.save(saved);
      return true;
    }
    return false;
  }
  
  public boolean updateDevice(DeviceToView dtv) {
    Device device = deviceService.findById(dtv.getId());
    User user = userService.findById(dtv.getUserId());
    
    device.addUser(user, dtv.getDate());
    device.addNote(dtv.getNote(), dtv.getDate());
    
    deviceService.save(device);
    return true;
  }

  public DeviceToView findDeviceById(long id) {
    Device device = deviceService.findById(id);
    return device != null ? device.toView() : null;
  }
  
  public DeviceToView findDeviceByIdAndDate(long id, LocalDate date) {
    return deviceService.findById(id).toView(date);
  }
  
  public List<LocalDate> findDeviceDatesById(long id) {
    return deviceService.findById(id).getAllModificationDateDesc();
  }

  public String getDeviceServiceError() {
    return deviceService.getError();
  }
  
  //-------- BILLING SERVICE --------
  
  public boolean fileUpload(MultipartFile file) throws FileUploadException {
    return billingService.uploadInvoiceAndProcess(file);
  }

  public List<Invoice> findAllInvoice() {
    return billingService.findAllInvoice();
  }

  public Invoice findInvoiceByInvoiceNumber(String invoiceNumber) {
    return billingService.findInvoicedByInvoiceNumber(invoiceNumber);
  }

  public List<DescriptionCategoryCoupler> findAllDescriptionCategoryCoupler() {
    return billingService.findAllDescriptionCategoryCoupler();
  }
  
//  public boolean invoiceDivisionByTemplateId(long billId, long templateId) {
//    List<FeeItem> fees = billingService.findAllFeeItemByBillId(billId);
//
//    fees = divideFeeItemsByUserChanges(fees);
//    fees = setFeeItemsUser(fees);
//    billingService.save(fees);
//    
//    return billingService.billPartitionByTemplateId(billId, templateId);
//  }
  
//  private List<FeeItem> divideFeeItemsByUserChanges(List<FeeItem> fees) {
//    List<FeeItem> result = new LinkedList<>();
//    for(FeeItem fee : fees) {
//      String number = fee.getSubscription();
//      LocalDate begin = fee.getBeginDate();
//      LocalDate end = fee.getEndDate();
//      List<LocalDate> allNewUserBegin = userSubService.findAllBeginDateBySubBetween(number, begin, end);
//      result.addAll(fee.splitBeforeDate(allNewUserBegin));
//    }
//    return result;
//  }
  
//  private List<FeeItem> setFeeItemsUser(List<FeeItem> fees) {
//    for(FeeItem fee : fees) {
//      User user = userSubService.getUser(fee.getSubscription(), fee.getBeginDate(), fee.getEndDate());
//      if(user != null) {
//        fee.setUserId(user.getId());
//      }
//    }
//    return fees;
//  }
  
  public List<Category> findAllCategory() {
    return billingService.findAllCategory();
  }

  public boolean addCategory(String category) {
    return billingService.addCategory(category);
  }

  public List<String> getUnknownFeeDescToTemplate(long templateId) {
    return billingService.getUnknownFeeDescToTemplate(templateId);
  }

  public void upgradeDescriptionCategoryCoupler(long id, List<String> descriptions, List<Long> categories) {
    billingService.upgradeDescriptionCategoryCoupler(id, descriptions, categories);
  }

//  public List<OneCategoryOfUserFinance> getUserFinance(String email) {
//    User user = userService.findByEmail(email);
//    return billingService.getFinanceByUserId(user.getId());
//  }

  public List<SubscriptionToView> findAllCurrentSubscriptionOfUser() {
    return subscriptionService.findAllCurrentByUser(getCurrentUser());
  }
  
  public List<DeviceToView> findAllCurrentDeviceOfUser() {
    return deviceService.findAllCurrentByUser(getCurrentUser());
  }

  public String getSimError() {
    return simService.removeMsg();
  }

  public boolean canCreateSubscription() {
    return !simService.findAllFree().isEmpty();
  }

  public List<User> findAllUserByStatus(int status) {
    return userService.findAllByStatus(status);
  }

  public User findUserById(long id) {
    return userService.findById(id);
  }

  public boolean updateUser(long id, Map<String, Boolean> roles) {
    return userService.updateUser(id, roles);
  }

  public boolean passwordReset(String email) {
    return userService.passwordReset(email);
  }

  public DescriptionCategoryCoupler findBillPartitionTemplateById(long id) {
    return billingService.findBillPartitionTemplateById(id);
  }

  public List<String> findAllFeeDescription() {
    return billingService.findAllBillDescription();
  }

  public boolean addChargeRatio(ChargeRatioByCategory payDevision, List<Long> categories, List<Integer> ratios) {
    return billingService.addPayDivision(payDevision, categories, ratios);
  }

  public List<ChargeRatioByCategory> findAllChargeRatio() {
    return billingService.findAllChargeRatio();
  }

  public ChargeRatioByCategory findChargeRatioById(long id) {
    return billingService.findChargeRatioById(id);
  }

  public boolean editChargeRatio(long id, List<Long> categories, List<Integer> ratios) {
    return billingService.editChargeRatio(id, categories, ratios);
  }

  public List<Category> getUnusedCategoryOfChargeRatio(long id) {
    return billingService.getUnusedCategoryOfChargeRatio(id);
  }

  public List<InvoiceByUserAndPhoneNumber> getPendingInvoicesOfCurrentUser() {
    return billingService.getPendingInvoicesOfUser(getCurrentUser());
  }

  public InvoiceByUserAndPhoneNumber getPendingInvoiceOfCurrentUserById(Long id) {
    return billingService.getPendingInvoiceOfUserById(getCurrentUser(), id);
  }
  
  private User getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return userService.findByEmail(((UserDetailsImpl)auth.getPrincipal()).getUsername());
  }

//  public boolean acceptInvoiceOfCurrentUserByInvoiceNumberAndNumber(String number) {
//    return billingService.acceptInvoiceOfUserByNumber(getCurrentUser(), number);
//  }

  public boolean acceptInvoicesOfCurrentUserByInvoiceNumbersAndPhoneNumbers(List<Long> ids) {
    return billingService.acceptInvoicesOfUserByInvoiceNumbersAndSubscriptions(getCurrentUser(), ids);
  }

  public void askForRevision(long id, Map<String, String> map) {
    billingService.askForRevision(getCurrentUser(), id, map);
  }
  
  public void restartPorcessingOfInvoice(String invoiceNumber) {
    billingService.resetInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void deleteInvoiceByInvoiceNumber(String invoiceNumber) {
    billingService.deleteInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void acceptInvoiceByInvoiceNumber(String invoiceNumber) {
    billingService.acceptInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void modifyFeeItemGrossAmountRatio(long id, double userAmount, double compAmount) {
    billingService.modifyFeeItemGrossAmountRatio(id, userAmount, compAmount);
  }

  public List<InvoiceByUserAndPhoneNumber> getAcceptedInvoicesOfCurrentUser() {
    return billingService.getAcceptedInvoicesOfUser(getCurrentUser());
  }

  public InvoiceByUserAndPhoneNumber getAcceptedInvoiceOfCurrentUserById(long id) {
    return billingService.getAcceptedInvoiceOfUserById(getCurrentUser(), id);
  }

  public List<InvoiceByUserAndPhoneNumber> getAcceptedByCompanyInvoicesOfCurrentUser() {
    return billingService.getAcceptedByCompanyInvoicesOfUser(getCurrentUser());
  }

  public List<Participant> findAllParticipant() {
    return billingService.findAllParticipant();
  }

  public Optional<Participant> findParticipantById(long id) {
    return billingService.findParticipantById(id);
  }

  public List<RawInvoice> findAllRawInvoice() {
    return billingService.findAllRawInvoice();
  }

  public boolean addParticipant(Participant participant) {
    return billingService.addParticipant(participant);
    
  }

}