package com.flotta.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.flotta.exception.invoice.FileUploadException;
import com.flotta.model.invoice.Category;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.invoice.DescriptionCategoryCoupler;
import com.flotta.model.invoice.Invoice;
import com.flotta.model.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.model.invoice.Participant;
import com.flotta.model.invoice.RawInvoice;
import com.flotta.model.registry.Device;
import com.flotta.model.registry.DeviceType;
import com.flotta.model.registry.Sim;
import com.flotta.model.registry.Subscription;
import com.flotta.model.registry.User;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.model.viewEntity.SubscriptionToView;
import com.flotta.service.invoice.InvoiceService;
import com.flotta.service.registry.RegistryService;
import com.flotta.service.switchTable.SwitchTableService;
import com.flotta.utility.MessageToView;

@Service
public class MainService {

  private RegistryService registryService;

  private InvoiceService invoiceService;

  private SwitchTableService switchTableService;

  @Autowired
  public MainService(RegistryService registryService, InvoiceService invoiceService, SwitchTableService switchTableService) {
    this.registryService = registryService;
    this.invoiceService = invoiceService;
    this.switchTableService = switchTableService;
  }

  // --- INVOICE SERVICE ---

  public void fileUpload(MultipartFile file) throws FileUploadException {
    invoiceService.uploadInvoice(file);
  }

  public List<Invoice> findAllInvoice() {
    return invoiceService.findAllInvoice();
  }

  public Optional<Invoice> findInvoiceByInvoiceNumber(String invoiceNumber) {
    return invoiceService.findInvoiceByInvoiceNumber(invoiceNumber);
  }

  public List<DescriptionCategoryCoupler> findAllDescriptionCategoryCoupler() {
    return invoiceService.findAllDescriptionCategoryCoupler();
  }

  public List<Category> findAllCategory() {
    return invoiceService.findAllCategory();
  }

  public Category addOrModifyCategory(long id, String name) {
    return invoiceService.addOrModifyCategory(id, name);
  }

  public void updateDescriptionCategoryCoupler(long id, List<String> descriptions, List<Long> categories, boolean available) {
    invoiceService.updateDescriptionCategoryCoupler(id, descriptions, categories, available);
  }

  public DescriptionCategoryCoupler findDescriptionCategoryCouplerById(long id) {
    return invoiceService.findDescriptionCategoryCoupler(id);
  }

  public boolean addChargeRatio(ChargeRatioByCategory chargeRatio) {
    return invoiceService.addChargeRatio(chargeRatio);
  }

  public List<ChargeRatioByCategory> findAllChargeRatio() {
    return invoiceService.findAllChargeRatio();
  }

  public Optional<ChargeRatioByCategory> findChargeRatioById(long id) {
    return invoiceService.findChargeRatioById(id);
  }

  public boolean editChargeRatio(long id, List<Long> categories, List<Integer> ratios) {
    return invoiceService.editChargeRatio(id, categories, ratios);
  }

  public List<Category> getUnusedCategoryOfChargeRatio(long id) {
    return invoiceService.getUnusedCategoryOfChargeRatio(id);
  }

  public InvoiceByUserAndPhoneNumber getPendingInvoiceOfCurrentUserById(String email, Long id) {
    return invoiceService.getPendingInvoiceOfUserById(registryService.findUserByEmail(email).get(), id);
  }

  public boolean acceptInvoicesOfCurrentUserByInvoiceNumbersAndPhoneNumbers(String email, List<Long> ids) {
    return invoiceService.acceptInvoicesOfUserByInvoiceNumbersAndSubscription(registryService.findUserByEmail(email).get(), ids);
  }

  public void askForRevision(String email, long id, Map<String, String> map) {
    invoiceService.askForRevision(registryService.findUserByEmail(email).get(), id, map);
  }

  public void restartProcessingInvoiceBy(String invoiceNumber) {
    invoiceService.restartProcessingInvoiceBy(invoiceNumber);
  }

  public void deleteInvoiceByInvoiceNumber(String invoiceNumber) {
    invoiceService.deleteInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void acceptInvoiceByInvoiceNumber(String invoiceNumber) {
    invoiceService.acceptInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void modifyFeeItemGrossAmountRatio(long id, double userAmount, double compAmount) {
    invoiceService.modifyFeeItemGrossAmountRatio(id, userAmount, compAmount);
  }

  public List<InvoiceByUserAndPhoneNumber> getAcceptedInvoicesOfCurrentUser(String email) {
    return invoiceService.getAcceptedInvoicesOfUser(registryService.findUserByEmail(email).get());
  }

  public InvoiceByUserAndPhoneNumber getAcceptedInvoiceOfCurrentUserById(String email, long id) {
    return invoiceService.getAcceptedInvoiceOfUserById(registryService.findUserByEmail(email).get(), id);
  }

  public List<InvoiceByUserAndPhoneNumber> getInvoicesOfUserByEmail(String email) {
    return invoiceService.getAcceptedByCompanyInvoicesOfUser(registryService.findUserByEmail(email).get());
  }

  public List<Participant> findAllParticipant() {
    return invoiceService.findAllParticipant();
  }

  public Optional<Participant> findParticipantById(long id) {
    return invoiceService.findParticipantById(id);
  }

  public List<RawInvoice> findAllRawInvoice() {
    return invoiceService.findAllRawInvoice();
  }

  public boolean addParticipant(Participant participant, long descriptionCategoryCouplerId) {
    return invoiceService.addParticipant(participant, descriptionCategoryCouplerId);
  }

  public boolean addDescriptionCategoryCoupler(DescriptionCategoryCoupler dcc) {
    return invoiceService.addDescriptionCategoryCoupler(dcc);
  }

  public List<String> findDescriptionsOfInvoiceByInvoiceNumber(String invoiceNumber) {
    return invoiceService.findDescriptionsOfInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void deleteRawInvoiceByInvoiceNumber(String invoiceNumber) {
    invoiceService.deleteRawInvoiceByInvoiceNumber(invoiceNumber);
  }

  public void updateParticipant(Participant participant) {
    invoiceService.updateParticipant(participant);
  }

  // --- RECORD SERVIVE ---
  
  
  //-------- DEVICE SERVICE --------

  public List<Device> findAllDevices() {
    return registryService.findAllDevices();
  }

  public boolean createDevice(DeviceToView dtv) {
    return registryService.createDevice(dtv);
  }

  public boolean updateDevice(DeviceToView dtv) {
    return registryService.updateDevice(dtv);
  }

  public Optional<Device> findDeviceById(long id) {
    return registryService.findDeviceById(id);
  }

  public String getDeviceServiceError() {
    return registryService.getDeviceServiceError();
  }

//-------- DEVICE TYPE SERVICE --------

  public List<DeviceType> findAllDeviceTypes() {
    return registryService.findAllDeviceTypes();
  }

  public List<String> findAllBrandOfDevicesType() {
    return registryService.findAllBrandOfDevicesType();
  }

  public DeviceType findDeviceTypeById(long id) {
    return registryService.findDeviceTypeById(id);
  }

  public boolean createDeviceType(DeviceType deviceType) {
    return registryService.saveDeviceType(deviceType);
  }
  
  public void updateDeviceType(DeviceType deviceType) {
    registryService.updateDeviceType(deviceType);
  }

  public List<DeviceType> findAllVisibleDeviceTypes() {
    return registryService.findAllVisibleDeviceTypes();
  }

//-------- SIM SERVICE --------

  public List<Sim> findAllFreeSim() {
    return registryService.findAllFreeSim();
  }

  public List<Sim> findAllSim() {
    return registryService.findAllSim();
  }

  public Optional<Sim> findSimById(int id) {
    return registryService.findSimById(id);
  }

  public boolean createSim(Sim sim) {
    return registryService.createSim(sim);
  }

  public String getSimError() {
    return registryService.getSimError();
  }

  public boolean canCreateSubscription() {
    return registryService.canCreateSubscription();
  }

//------- SUBSCRIPTION SERVICE --------

  public List<Subscription> findAllSubscription() {
    return registryService.findAllSubscription();
  }

  public Optional<Subscription> findSubscriptionById(long id) {
    return registryService.findSubscriptionById(id);
  }

  public boolean createSubscription(SubscriptionToView stv) {
    return registryService.createSubscription(stv);
  }

  public boolean updateSubscription(SubscriptionToView stv) {
    return registryService.updateSubscription(stv);
  }

  public String getSubscriptionServiceError() {
    return registryService.getSubscriptionServiceError();
  }

//TODO put UserServiceImp function here
  // -------- USER SERVICE --------
  public boolean registerUser(User user) {
    return registryService.registerUser(user);
  }

  public List<User> findAllUser() {
    return registryService.findAllUser();
  }

  public Optional<User> findUserByEmail(String email) {
    return registryService.findUserByEmail(email);
  }

  public boolean changePassword(String email, String oldPsw, String newPsw, String confirmPsw) {
    return registryService.changePassword(email, oldPsw, newPsw, confirmPsw);
  }

  public List<MessageToView> getUserError() {
    return registryService.getUserError();
  }

  public boolean firstAdminRegistration(User user) {
    return registryService.firstAdminRegistration(user);
  }

  public boolean registrationAvailable() {
    return registryService.registrationAvailable();
  }

  public boolean activation(String key) {
    return registryService.activation(key);
  }

  public List<User> findAllUserByStatus(int status) {
    return registryService.findAllUserByStatus(status);
  }

  public Optional<User> findUserById(long id) {
    return registryService.findUserById(id);
  }

  public boolean updateUser(long id, Map<String, Boolean> roles) {
    return registryService.updateUser(id, roles);
  }

  public boolean requestNewPassword(String email) {
    return registryService.requestNewPassword(email);
  }

  public boolean updateChargeRatioOfUser(long userId, long chargeRatioId) {
    Optional<ChargeRatioByCategory> chargeRatioOpt = invoiceService.findChargeRatioById(chargeRatioId);
    if(chargeRatioOpt.isPresent()) {
      return registryService.updateChargeRatioOfUser(userId, chargeRatioOpt.get());
    }
    return false;
  }
  
  public Optional<ChargeRatioByCategory> getChargeRatioOfUserById(long id) {
    Optional<User> userOpt = registryService.findUserById(id);
    if(userOpt.isPresent()) {
      return Optional.ofNullable(userOpt.get().getChargeRatio());
    }
    return Optional.empty();
  }

  // --- SWITCH TABLE SERVICE ---

  public List<Device> findAllCurrentDeviceByUser(long userId) {
    return switchTableService.findAllCurrentDeviceByUser(registryService.findUserById(userId));
  }
}