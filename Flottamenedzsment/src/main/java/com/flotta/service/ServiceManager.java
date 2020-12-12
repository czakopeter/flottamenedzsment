package com.flotta.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.flotta.enums.Availability;
import com.flotta.model.invoice.Category;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.invoice.DescriptionCategoryCoupler;
import com.flotta.model.invoice.Invoice;
import com.flotta.model.invoice.GroupedFeeItems;
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
import com.flotta.utility.BooleanWithMessages;

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

  public BooleanWithMessages fileUpload(MultipartFile file) {
    return invoiceManager.uploadInvoice(file);
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
  
  public List<DescriptionCategoryCoupler> findAllDescriptionCategoryCouplerByAvailability(Availability availability) {
    return invoiceManager.findAllDescriptionCategoryCouplerByAvailability(availability);
  }
  
  public BooleanWithMessages createDescriptionCategoryCoupler(DescriptionCategoryCoupler dcc) {
    return invoiceManager.createDescriptionCategoryCoupler(dcc);
  }
  
  public void updateDescriptionCategoryCoupler(DescriptionCategoryCoupler coupler, List<String> descriptions, List<Long> categoryIds) {
    invoiceManager.updateDescriptionCategoryCoupler(coupler, descriptions, categoryIds); 
  }

//-------- CHARGE RATIO BY CATEGORY SERVICE --------

  public List<ChargeRatioByCategory> findAllChargeRatio() {
    return invoiceManager.findAllChargeRatio();
  }
  
  public Optional<ChargeRatioByCategory> findChargeRatioById(long id) {
    return invoiceManager.findChargeRatioById(id);
  }
  
  public List<ChargeRatioByCategory> findAllChargeRatioByAvailability(Availability availability) {
    return invoiceManager.findAllChargeRatioByAvailability(availability);
  }
  
  public BooleanWithMessages createChargeRatio(ChargeRatioByCategory chargeRatio) {
    return invoiceManager.createChargeRatio(chargeRatio);
  }

  public boolean updateChargeRatio(ChargeRatioByCategory chargeRatio, List<Long> categories, List<Integer> ratios) {
    return invoiceManager.updateChargeRatio(chargeRatio, categories, ratios);
  }

  public List<Category> findAllUnusedCategoryOfChargeRatio(long id) {
    return invoiceManager.findAllUnusedCategoryOfChargeRatio(id);
  }
  
//-------- GROUPED FEE ITEMS SERVICE --------

  public Optional<GroupedFeeItems> findGroupedFeeItemsOfUserByEmailAndId(String email, Long id) {
    return invoiceManager.findGroupedFeeItemsByUserAndId(registryManager.findUserByEmail(email).get(), id);
  }

  public void acceptGroupedFeeItemsOfUserByEmailAndIdsFromUser(String email, List<Long> ids) {
    invoiceManager.acceptGroupedFeeItemsOfUserByIdsFromUser(registryManager.findUserByEmail(email).get(), ids);
  }

  public void askForRevision(String email, long id, Map<String, String> notes) {
    invoiceManager.askForRevision(registryManager.findUserByEmail(email).get(), id, notes);
  }

  public List<GroupedFeeItems> findAllGroupedFeeItemsByUserEmail(String email) {
    return invoiceManager.findAllGroupedFeeItemsByUser(registryManager.findUserByEmail(email).get());
  }

//-------- FEE ITEM SERVICE --------
  
  public void modifyFeeItemGrossAmountRatio(long id, double userAmount) {
    invoiceManager.modifyFeeItemGrossAmountRatio(id, userAmount);
  }

//-------- PARTICIPANT SERVICE --------
  
  public List<Participant> findAllParticipant() {
    return invoiceManager.findAllParticipant();
  }

  public Optional<Participant> findParticipantById(long id) {
    return invoiceManager.findParticipantById(id);
  }

  public BooleanWithMessages createParticipant(Participant participant) {
    return invoiceManager.createParticipant(participant);
  }

  public void updateParticipant(Participant participant) {
    invoiceManager.updateParticipant(participant);
  }

  // --- RECORD MANAGER ---
  
  
  //-------- DEVICE SERVICE --------

  public List<Device> findAllDevices() {
    return registryManager.findAllDevices();
  }

  public BooleanWithMessages createDevice(DeviceToView dtv) {
    return registryManager.createDevice(dtv);
  }

  public BooleanWithMessages updateDevice(DeviceToView dtv) {
    return registryManager.updateDevice(dtv);
  }

  public Optional<Device> findDeviceById(long id) {
    return registryManager.findDeviceById(id);
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

  public BooleanWithMessages createDeviceType(DeviceType deviceType) {
    return registryManager.saveDeviceType(deviceType);
  }
  
  public void updateDeviceType(DeviceType deviceType) {
    registryManager.updateDeviceType(deviceType);
  }

  public List<DeviceType> findAllDeviceTypesByAvailability(Availability availability) {
    return registryManager.findAllDeviceTypesByAvailability(availability);
  }
  
  public BooleanWithMessages canCreateDevice() {
    return registryManager.canCreateDevice();
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

  public BooleanWithMessages createSim(Sim sim) {
    return registryManager.createSim(sim);
  }

  public BooleanWithMessages canCreateSubscription() {
    return registryManager.canCreateSubscription();
  }

//------- SUBSCRIPTION SERVICE --------

  public List<Subscription> findAllSubscription() {
    return registryManager.findAllSubscription();
  }

  public Optional<Subscription> findSubscriptionById(long id) {
    return registryManager.findSubscriptionById(id);
  }

  public BooleanWithMessages createSubscription(SubscriptionToView stv) {
    return registryManager.createSubscription(stv);
  }

  public BooleanWithMessages updateSubscription(SubscriptionToView stv) {
    return registryManager.updateSubscription(stv);
  }

  // -------- USER SERVICE --------
  
  public BooleanWithMessages createUser(User user) {
    return registryManager.createUser(user);
  }

  public List<User> findAllUser() {
    return registryManager.findAllUser();
  }

  public Optional<User> findUserByEmail(String email) {
    return registryManager.findUserByEmail(email);
  }

  public BooleanWithMessages changePassword(String email, String oldPsw, String newPsw, String confirmPsw) {
    return registryManager.changePassword(email, oldPsw, newPsw, confirmPsw);
  }

  public BooleanWithMessages createFirstAdmin(User user) {
    return registryManager.createFirstAdmin(user);
  }
  
  public BooleanWithMessages activation(String key) {
    return registryManager.activation(key);
  }

  public Optional<User> findUserById(long id) {
    return registryManager.findUserById(id);
  }

  public BooleanWithMessages updateUser(long id, Map<String, Boolean> roles) {
    return registryManager.updateUser(id, roles);
  }

  public BooleanWithMessages requestNewPassword(String email) {
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
  
  public boolean hasEnabledAdmin() {
    return registryManager.hasEnabledAdmin();
  }
  
  public boolean hasAdmin() {
    return registryManager.hasAdmin();
  }
  
  public BooleanWithMessages deleteUserById(long id) {
    return registryManager.deleteUserById(id);
  }
  

  // --- SWITCH TABLE SERVICE ---

  public List<Device> findAllAvailableDeviceByUser(long userId) {
    return switchTableService.findAllAvailableDeviceByUser(registryManager.findUserById(userId));
  }

}