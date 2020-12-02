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
import com.flotta.service.invoice.InvoiceManager;
import com.flotta.service.registry.RegistryManager;
import com.flotta.service.switchTable.SwitchTableService;
import com.flotta.utility.MessageToView;

@Service
public class ServiceManager {
  
  private RegistryManager registryManager;

  private InvoiceManager invoiceManager;

  private SwitchTableService switchTableService;

  @Autowired
  public ServiceManager(RegistryManager registryManager, InvoiceManager invoiceManager, SwitchTableService switchTableService) {
    this.registryManager = registryManager;
    this.invoiceManager = invoiceManager;
    this.switchTableService = switchTableService;
  }

  // --- INVOICE MANAGER ---
  
//-------- INVOICE SERVICE --------

  public void fileUpload(MultipartFile file) throws FileUploadException {
    invoiceManager.uploadInvoice(file);
  }

  public List<Invoice> findAllInvoice() {
    return invoiceManager.findAllInvoice();
  }

  public Optional<Invoice> findInvoiceByInvoiceNumber(String invoiceNumber) {
    return invoiceManager.findInvoiceByInvoiceNumber(invoiceNumber);
  }
  
  public void acceptInvoiceByInvoiceNumberFromCompany(String invoiceNumber) {
    invoiceManager.acceptInvoiceByInvoiceNumberFromCompany(invoiceNumber);
  }
  
  public void deleteInvoiceByInvoiceNumber(String invoiceNumber) {
    invoiceManager.deleteInvoiceByInvoiceNumber(invoiceNumber);
  }
  
//-------- RAW INVOICE SERVICE --------
  
  public List<RawInvoice> findAllRawInvoice() {
    return invoiceManager.findAllRawInvoice();
  }
  
  public void deleteRawInvoiceByInvoiceNumber(String invoiceNumber) {
    invoiceManager.deleteRawInvoiceByInvoiceNumber(invoiceNumber);
  }
  
  public void restartProcessingRawInvoiceBy(String invoiceNumber) {
    invoiceManager.restartProcessingRawInvoiceBy(invoiceNumber);
  }
  
  public List<String> findDescriptionsOfInvoiceByInvoiceNumber(String invoiceNumber) {
    return invoiceManager.findDescriptionsOfInvoiceByInvoiceNumber(invoiceNumber);
  }

//-------- CATEGORY SERVICE --------
  
  public List<Category> findAllCategory() {
    return invoiceManager.findAllCategory();
  }

  public Category createOrModifyCategory(long id, String name) {
    return invoiceManager.createOrModifyCategory(id, name);
  }
  
//-------- DESCRIPTION CATEGORY COUPLER SERVICE --------
  
  public List<DescriptionCategoryCoupler> findAllDescriptionCategoryCoupler() {
    return invoiceManager.findAllDescriptionCategoryCoupler();
  }

  public Optional<DescriptionCategoryCoupler> findDescriptionCategoryCouplerById(long id) {
    return invoiceManager.findDescriptionCategoryCoupler(id);
  }
  
  public boolean createDescriptionCategoryCoupler(DescriptionCategoryCoupler dcc) {
    return invoiceManager.createDescriptionCategoryCoupler(dcc);
  }
  
  public void updateDescriptionCategoryCoupler(long id, List<String> descriptions, List<Long> categoryIds, boolean available) {
    invoiceManager.updateDescriptionCategoryCoupler(id, descriptions, categoryIds, available);
  }

//-------- CHARGE RATIO BY CATEGORY SERVICE --------

  public List<ChargeRatioByCategory> findAllChargeRatio() {
    return invoiceManager.findAllChargeRatio();
  }
  
  public Optional<ChargeRatioByCategory> findChargeRatioById(long id) {
    return invoiceManager.findChargeRatioById(id);
  }
  
  public boolean createChargeRatio(ChargeRatioByCategory chargeRatio) {
    return invoiceManager.createChargeRatio(chargeRatio);
  }

  public boolean updateChargeRatio(long id, List<Long> categories, List<Integer> ratios) {
    return invoiceManager.updateChargeRatio(id, categories, ratios);
  }

  public List<Category> findAllUnusedCategoryOfChargeRatio(long id) {
    return invoiceManager.findAllUnusedCategoryOfChargeRatio(id);
  }
  
//-------- INVOICE BY USER AND PHONE NUMBER SERVICE --------

  public Optional<InvoiceByUserAndPhoneNumber> findInvoiceOfUserByEmailAndId(String email, Long id) {
    return invoiceManager.findInvoiceOfUserById(registryManager.findUserByEmail(email).get(), id);
  }

  public void acceptInvoicesOfUserByEmailAndInvoiceIds(String email, List<Long> ids) {
    invoiceManager.acceptInvoicesOfUserByUserAndEmailAndInvoiceIds(registryManager.findUserByEmail(email).get(), ids);
  }

  public void askRevisionOfInvoiceByUser(String email, long id, Map<String, String> map) {
    invoiceManager.askRevisionOfInvoiceByUser(registryManager.findUserByEmail(email).get(), id, map);
  }

  public List<InvoiceByUserAndPhoneNumber> findInvoicesOfUserByEmail(String email) {
    return invoiceManager.findInvoicesOfUserByEmail(registryManager.findUserByEmail(email).get());
  }

//-------- FEE ITEM SERVICE --------
  
  public void modifyFeeItemGrossAmountRatio(long id, double userAmount, double compAmount) {
    invoiceManager.modifyFeeItemGrossAmountRatio(id, userAmount, compAmount);
  }

//-------- PARTICIPANT SERVICE --------
  
  public List<Participant> findAllParticipant() {
    return invoiceManager.findAllParticipant();
  }

  public Optional<Participant> findParticipantById(long id) {
    return invoiceManager.findParticipantById(id);
  }

  public boolean createParticipant(Participant participant, long descriptionCategoryCouplerId) {
    return invoiceManager.createParticipant(participant, descriptionCategoryCouplerId);
  }

  public void updateParticipant(Participant participant) {
    invoiceManager.updateParticipant(participant);
  }

  // --- RECORD MANAGER ---
  
  
  //-------- DEVICE SERVICE --------

  public List<Device> findAllDevices() {
    return registryManager.findAllDevices();
  }

  public boolean createDevice(DeviceToView dtv) {
    return registryManager.createDevice(dtv);
  }

  public boolean updateDevice(DeviceToView dtv) {
    return registryManager.updateDevice(dtv);
  }

  public Optional<Device> findDeviceById(long id) {
    return registryManager.findDeviceById(id);
  }

  public String getDeviceServiceError() {
    return registryManager.getDeviceServiceError();
  }

//-------- DEVICE TYPE SERVICE --------

  public List<DeviceType> findAllDeviceTypes() {
    return registryManager.findAllDeviceTypes();
  }

  public List<String> findAllBrandOfDevicesType() {
    return registryManager.findAllBrandOfDevicesType();
  }

  public Optional<DeviceType> findDeviceTypeById(long id) {
    return registryManager.findDeviceTypeById(id);
  }

  public boolean createDeviceType(DeviceType deviceType) {
    return registryManager.saveDeviceType(deviceType);
  }
  
  public void updateDeviceType(DeviceType deviceType) {
    registryManager.updateDeviceType(deviceType);
  }

  public List<DeviceType> findAllVisibleDeviceTypes() {
    return registryManager.findAllVisibleDeviceTypes();
  }

//-------- SIM SERVICE --------

  public List<Sim> findAllFreeSim() {
    return registryManager.findAllFreeSim();
  }

  public List<Sim> findAllSim() {
    return registryManager.findAllSim();
  }

  public Optional<Sim> findSimById(int id) {
    return registryManager.findSimById(id);
  }

  public boolean createSim(Sim sim) {
    return registryManager.createSim(sim);
  }

  public String getSimError() {
    return registryManager.getSimError();
  }

  public boolean canCreateSubscription() {
    return registryManager.canCreateSubscription();
  }

//------- SUBSCRIPTION SERVICE --------

  public List<Subscription> findAllSubscription() {
    return registryManager.findAllSubscription();
  }

  public Optional<Subscription> findSubscriptionById(long id) {
    return registryManager.findSubscriptionById(id);
  }

  public boolean createSubscription(SubscriptionToView stv) {
    return registryManager.createSubscription(stv);
  }

  public boolean updateSubscription(SubscriptionToView stv) {
    return registryManager.updateSubscription(stv);
  }

  public String getSubscriptionServiceError() {
    return registryManager.getSubscriptionServiceError();
  }

  // -------- USER SERVICE --------
  
  public boolean createUser(User user) {
    return registryManager.createUser(user);
  }

  public List<User> findAllUser() {
    return registryManager.findAllUser();
  }

  public Optional<User> findUserByEmail(String email) {
    return registryManager.findUserByEmail(email);
  }

  public boolean changePassword(String email, String oldPsw, String newPsw, String confirmPsw) {
    return registryManager.changePassword(email, oldPsw, newPsw, confirmPsw);
  }

  public List<MessageToView> getUserError() {
    return registryManager.getUserError();
  }

  public boolean createFirstAdmin(User user) {
    return registryManager.createFirstAdmin(user);
  }

  public boolean registrationAvailable() {
    return registryManager.registrationAvailable();
  }

  public boolean activation(String key) {
    return registryManager.activation(key);
  }

  public Optional<User> findUserById(long id) {
    return registryManager.findUserById(id);
  }

  public boolean updateUser(long id, Map<String, Boolean> roles) {
    return registryManager.updateUser(id, roles);
  }

  public boolean requestNewPassword(String email) {
    return registryManager.requestNewPassword(email);
  }

  public boolean updateChargeRatioOfUser(long userId, long chargeRatioId) {
    return registryManager.updateChargeRatioOfUser(userId, invoiceManager.findChargeRatioById(chargeRatioId));
  }
  
  public Optional<ChargeRatioByCategory> getChargeRatioOfUserById(long id) {
    Optional<User> userOpt = registryManager.findUserById(id);
    if(userOpt.isPresent()) {
      return Optional.ofNullable(userOpt.get().getChargeRatio());
    }
    return Optional.empty();
  }

  // --- SWITCH TABLE SERVICE ---

  public List<Device> findAllCurrentDeviceByUser(long userId) {
    return switchTableService.findAllCurrentDeviceByUser(registryManager.findUserById(userId));
  }
}