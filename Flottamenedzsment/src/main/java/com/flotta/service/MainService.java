package com.flotta.service;

import java.time.LocalDate;
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
import com.flotta.model.registry.DeviceType;
import com.flotta.model.registry.Sim;
import com.flotta.model.registry.Subscription;
import com.flotta.model.registry.User;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.model.viewEntity.SubscriptionToView;
import com.flotta.service.invoice.InvoiceService;
import com.flotta.service.registry.RecordService;
import com.flotta.service.switchTable.SwitchTableService;
import com.flotta.utility.MessageToView;

@Service
public class MainService {

  private RecordService recordService;

  private InvoiceService invoiceService;

  private SwitchTableService switchTableService;

  @Autowired
  public MainService(RecordService recordService, InvoiceService invoiceService, SwitchTableService switchTableService) {
    this.recordService = recordService;
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

  public List<InvoiceByUserAndPhoneNumber> getPendingInvoicesOfCurrentUser() {
    return invoiceService.getPendingInvoicesOfUser(getCurrentUser());
  }

  public InvoiceByUserAndPhoneNumber getPendingInvoiceOfCurrentUserById(Long id) {
    return invoiceService.getPendingInvoiceOfUserById(getCurrentUser(), id);
  }

//  public boolean acceptInvoiceOfCurrentUserByInvoiceNumberAndNumber(String number) {
//    return billingService.acceptInvoiceOfUserByNumber(getCurrentUser(), number);
//  }

  public boolean acceptInvoicesOfCurrentUserByInvoiceNumbersAndPhoneNumbers(List<Long> ids) {
    return invoiceService.acceptInvoicesOfUserByInvoiceNumbersAndSubscription(getCurrentUser(), ids);
  }

  public void askForRevision(long id, Map<String, String> map) {
    invoiceService.askForRevision(getCurrentUser(), id, map);
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

  public List<InvoiceByUserAndPhoneNumber> getAcceptedInvoicesOfCurrentUser() {
    return invoiceService.getAcceptedInvoicesOfUser(getCurrentUser());
  }

  public InvoiceByUserAndPhoneNumber getAcceptedInvoiceOfCurrentUserById(long id) {
    return invoiceService.getAcceptedInvoiceOfUserById(getCurrentUser(), id);
  }

  public List<InvoiceByUserAndPhoneNumber> getAcceptedByCompanyInvoicesOfCurrentUser() {
    return invoiceService.getAcceptedByCompanyInvoicesOfUser(getCurrentUser());
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

  private User getCurrentUser() {
    return recordService.getCurrentUser();
  }

  public List<DeviceToView> findAllDevices() {
    return recordService.findAllDevices();
  }

  public boolean saveDevice(DeviceToView dtv) {
    return recordService.saveDevice(dtv);
  }

  public boolean updateDevice(DeviceToView dtv) {
    return recordService.updateDevice(dtv);
  }

  public DeviceToView findDeviceById(long id) {
    return recordService.findDeviceById(id);
  }

  public DeviceToView findDeviceByIdAndDate(long id, LocalDate date) {
    return recordService.findDeviceByIdAndDate(id, date);
  }

  public List<LocalDate> findDeviceDatesById(long id) {
    return recordService.findDeviceDatesById(id);
  }

  public String getDeviceServiceError() {
    return recordService.getDeviceServiceError();
  }

//-------- DEVICE TYPE SERVICE --------

  public List<DeviceType> findAllDeviceTypes() {
    return recordService.findAllDeviceTypes();
  }

  public List<String> findAllBrandOfDevicesType() {
    return recordService.findAllBrandOfDevicesType();
  }

  public boolean saveDeviceType(DeviceType deviceType) {
    return recordService.saveDeviceType(deviceType);
  }

  public DeviceType findDeviceTypeById(long id) {
    return recordService.findDeviceTypeById(id);
  }

  public void updateDeviceType(DeviceType deviceType) {
    recordService.updateDeviceType(deviceType);
  }

  public List<DeviceType> findAllVisibleDeviceTypes() {
    return recordService.findAllVisibleDeviceTypes();
  }

//-------- SIM SERVICE --------

  public List<Sim> findAllFreeSim() {
    return recordService.findAllFreeSim();
  }

  public List<Sim> findAllSim() {
    return recordService.findAllSim();
  }

  public Sim findSimById(int id) {
    return recordService.findSimById(id);
  }

  public boolean addSim(Sim sim) {
    return recordService.addSim(sim);
  }

  public String getSimError() {
    return recordService.getSimError();
  }

  public boolean canCreateSubscription() {
    return recordService.canCreateSubscription();
  }

//------- SUBSCRIPTION SERVICE --------

  public List<SubscriptionToView> findAllSubscription() {
    return recordService.findAllSubscription();
  }

  public Optional<Subscription> findSubscriptionById(long id) {
    return recordService.findSubscriptionById(id);
  }

  public SubscriptionToView findSubscriptionByIdAndDate(long id, LocalDate date) {
    return recordService.findSubscriptionByIdAndDate(id, date);
  }

//  public Optional<Subscription> findSubscriptionByNumber(String number) {
//    return recordService.findSubscriptionByNumber(number);
//  }

  public SubscriptionToView findSubscriptionByNumberAndDate(String number, LocalDate date) {
    return recordService.findSubscriptionByNumberAndDate(number, date);
  }

  public List<LocalDate> findSubscriptionDatesById(long id) {
    return recordService.findSubscriptionDatesById(id);
  }

  public boolean addSubscription(SubscriptionToView stv) {
    return recordService.addSubscription(stv);
  }

  public boolean updateSubscription(SubscriptionToView stv) {
    return recordService.updateSubscription(stv);
  }

  public String getSubscriptionServiceError() {
    return recordService.getSubscriptionServiceError();
  }

//TODO put UserServiceImp function here
  // -------- USER SERVICE --------
  public boolean registerUser(User user) {
    return recordService.registerUser(user);
  }

  public List<User> findAllUser() {
    return recordService.findAllUser();
  }

  public User findUserByEmail(String email) {
    return recordService.findUserByEmail(email);
  }

  public boolean changePassword(String oldPsw, String newPsw, String confirmPsw) {
    return recordService.changePassword(oldPsw, newPsw, confirmPsw);
  }

  public List<MessageToView> getUserError() {
    return recordService.getUserError();
  }

  public boolean firstAdminRegistration(User user) {
    return recordService.firstAdminRegistration(user);
  }

  public boolean registrationAvailable() {
    return recordService.registrationAvailable();
  }

  public boolean activation(String key) {
    return recordService.activation(key);
  }

  public List<User> findAllUserByStatus(int status) {
    return recordService.findAllUserByStatus(status);
  }

  public User findUserById(long id) {
    return recordService.findUserById(id);
  }

  public boolean updateUser(long id, Map<String, Boolean> roles) {
    return recordService.updateUser(id, roles);
  }

  public boolean requestNewPassword(String email) {
    return recordService.requestNewPassword(email);
  }

  public User editChargeRatioOfUser(long userId, long chargeRatioId) {
    Optional<ChargeRatioByCategory> optionalChargeRatio = invoiceService.findChargeRatioById(chargeRatioId);
    if (optionalChargeRatio.isPresent()) {
      return recordService.editChargeRatioOfUser(userId, optionalChargeRatio.get());
    }
    return null;
  }

  // --- SWITCH TABLE SERVICE ---

  public List<SubscriptionToView> findAllSubscriptionOfCurrentUser() {
    return switchTableService.findAllCurrentSubscriptionByUser(getCurrentUser());
  }

  public List<DeviceToView> findAllDeviceOfCurrentUser() {
    return switchTableService.findAllDeviceByUser(getCurrentUser());
  }

  public List<DeviceToView> findAllCurrentDeviceByUser(long userId) {
    return switchTableService.findAllCurrentDeviceByUser(recordService.findUserById(userId));
  }

}