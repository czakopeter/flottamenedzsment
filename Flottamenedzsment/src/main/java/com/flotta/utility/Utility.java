package com.flotta.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flotta.model.invoice.Category;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.invoice.DescriptionCategoryCoupler;
import com.flotta.model.invoice.Invoice;
import com.flotta.model.invoice.Participant;
import com.flotta.model.invoice.RawInvoice;
import com.flotta.model.registry.Device;
import com.flotta.model.registry.DeviceType;
import com.flotta.model.registry.Sim;
import com.flotta.model.registry.Subscription;
import com.flotta.model.registry.User;
import com.flotta.model.switchTable.BasicSwitchTable;
import com.flotta.model.switchTable.UserDev;
import com.flotta.model.switchTable.UserSub;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.model.viewEntity.SubscriptionToView;

public class Utility {
  
  public static LocalDate getLatestSwitchTableDate(Map<LocalDate, ? extends BasicSwitchTable> map) {
    if(map == null || map.isEmpty()) {
      throw new IllegalArgumentException("Map should contains at least one element");
    }
    List<LocalDate> dates = new LinkedList<>(map.keySet());
    Collections.sort(dates, Collections.reverseOrder());
    return dates.get(0);
  }
  
  public static LocalDate floorDate(List<LocalDate> dates, LocalDate date) {
    if (dates == null || dates.isEmpty() || date == null) {
      return null;
    }
    if (dates.contains(date)) {
      return date;
    }
    Collections.sort(dates, Collections.reverseOrder());
    LocalDate r = null;
    for (LocalDate act : dates) {
      if (date.isAfter(act)) {
        break;
      }
      r = act;
    }
    return r;
  }
  
  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
  public static String getPeriod(LocalDate beginDate, LocalDate endDate) {
    StringBuilder sb = new StringBuilder();
    if(beginDate == null || (endDate != null && beginDate.isAfter(endDate))) {
    } else {
      sb.append(beginDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
      sb.append(" - ");
      if(endDate != null) {
        if(beginDate.getYear() < endDate.getYear()) {
          sb.append(endDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        } else {
          sb.append(endDate.format(DateTimeFormatter.ofPattern("MM.dd")));
        }
      }
    }
    return sb.toString();
  }
  
  public static BasicSwitchTable getLastUnendedSwitchTable(Map<LocalDate, ? extends BasicSwitchTable> map) {
    if(map == null || map.isEmpty()) {
      return null;
    }
    List<LocalDate> beginDates = new LinkedList<>(map.keySet());
    Collections.sort(beginDates, Collections.reverseOrder());
    LocalDate lastBeginDate = beginDates.get(0);
    BasicSwitchTable last = map.get(lastBeginDate);
    return last.getEndDate() == null ? last : null;
  }
  
  public static BasicSwitchTable getModSwitchTableOrNull(Map<LocalDate, ? extends BasicSwitchTable> map, LocalDate ceil) {
    if(map == null || map.isEmpty()) {
      return null;
    }
    if(map.containsKey(ceil)) {
      return map.get(ceil);
    }
    
    List<LocalDate> beginDates = new LinkedList<>(map.keySet());
    Collections.sort(beginDates, Collections.reverseOrder());
    LocalDate floorBeginDate = null;
    for(LocalDate date : beginDates) {
      if(ceil.isAfter(date)) {
        floorBeginDate = date;
        break;
      }
    }
    if(floorBeginDate == null) {
      return null;
    }
    BasicSwitchTable last = map.get(floorBeginDate);
    if(last.getEndDate() == null) {
      return last;
    } else {
      return last.getEndDate().isBefore(ceil) ? null : last;
    }
  }
  
  public static List<DeviceToView> convertDevicesToView(List<Device> devices) {
    List<DeviceToView> devicesToView = new LinkedList<>();
    for(Device device : devices) {
      devicesToView.add(new DeviceToView(device));
    }
    Collections.sort(devicesToView, DeviceToView.BY_HAS_USER_AND_SERIAL_NUMBER);
    return devicesToView;
  }

  public static List<SubscriptionToView> convertSubscripionToView(List<Subscription> subscriptions) {
    List<SubscriptionToView> subscriptionsToView = new LinkedList<>();
    for(Subscription subscription : subscriptions) {
      subscriptionsToView.add(new SubscriptionToView(subscription));
    }
    Collections.sort(subscriptionsToView, SubscriptionToView.BY_PHONE_NUMBER);
    return subscriptionsToView;
  }

  public static List<LocalDate> getModificationDateBetween(Map<LocalDate, ? extends BasicSwitchTable> map, LocalDate beginDate, LocalDate endDate) {
    if(map == null || beginDate == null || (endDate != null && endDate.isBefore(beginDate))) {
      return new LinkedList<>();
    }
    
    Set<LocalDate> result = new HashSet<>();
    List<LocalDate> dates = new LinkedList<>(map.keySet());
    Collections.sort(dates);
    System.err.println("modDate collect" + dates);
    for (LocalDate date : dates) {
      if(endDate != null && !date.isBefore(endDate)) {
        break;
      }
      BasicSwitchTable bst = map.get(date);
      if(bst.getEndDate() == null) {
        result.add(date);
        break;
      }
      if(!bst.getEndDate().isBefore(beginDate)) {
        result.add(date);
        if(endDate == null) {
          result.add(bst.getEndDate().plusDays(1));
        } else if (bst.getEndDate().plusDays(1).isBefore(endDate)) {
          result.add(bst.getEndDate().plusDays(1));
        }
      }
    }
    result.remove(endDate);
    return new LinkedList<>(result);
  }
  
  public static List<DeviceToView> convertUserDevicesToView(User user) {
    List<DeviceToView> devicesToView = new LinkedList<>();
    for(UserDev userDev : user.getUserDevs()) {
      Device device = userDev.getDev();
      List<LocalDate> dates = getModificationDateBetween(device.getDevSubs(), userDev.getBeginDate(), userDev.getEndDate());
      if(dates.isEmpty()) {
        DeviceToView dtv = new DeviceToView(device, userDev.getBeginDate());
        dtv.setEndDate(userDev.getEndDate());
        devicesToView.add(dtv);
      } else {
        Collections.sort(dates);
        if(!dates.get(0).equals(userDev.getBeginDate())) {
          DeviceToView dtv = new DeviceToView(device, userDev.getBeginDate());
          dtv.setEndDate(dates.get(0).minusDays(1));
          devicesToView.add(dtv);
        }
        for(int i = 0; i < dates.size() - 1; i++) {
          DeviceToView dtv = new DeviceToView(device, dates.get(i));
          dtv.setEndDate(dates.get(i + 1).minusDays(1));
          devicesToView.add(dtv);
        }
        DeviceToView dtv = new DeviceToView(device, dates.get(dates.size() - 1));
        dtv.setEndDate(userDev.getEndDate());
        devicesToView.add(dtv);
      }
    }
    Collections.sort(devicesToView, DeviceToView.BY_SERIAL_NUMBER_AND_BEGIN_DATE);
    return devicesToView;
  }
  
  public static List<SubscriptionToView> convertUserSubscriptionsToView(User user) {
    List<SubscriptionToView> subscriptionsToView = new LinkedList<>();
    for(UserSub userSub : user.getUserSubs()) {
      Subscription subscription = userSub.getSub();
      List<LocalDate> dates = getModificationDateBetween(subscription.getSubDev(), userSub.getBeginDate(), userSub.getEndDate());
      if(dates.isEmpty()) {
        SubscriptionToView stv = new SubscriptionToView(subscription, userSub.getBeginDate());
        stv.setEndDate(userSub.getEndDate());
        subscriptionsToView.add(stv);
      } else {
        Collections.sort(dates);
        if(!dates.get(0).equals(userSub.getBeginDate())) {
          SubscriptionToView stv = new SubscriptionToView(subscription, userSub.getBeginDate());
          stv.setEndDate(dates.get(0).minusDays(1));
          subscriptionsToView.add(stv);
        }
        for(int i = 0; i < dates.size() - 1; i++) {
          SubscriptionToView stv = new SubscriptionToView(subscription, dates.get(i));
          stv.setEndDate(dates.get(i + 1).minusDays(1));
          subscriptionsToView.add(stv);
        }
        SubscriptionToView stv = new SubscriptionToView(subscription, dates.get(dates.size() - 1));
        stv.setEndDate(userSub.getEndDate());
        subscriptionsToView.add(stv);
      }
    }
    Collections.sort(subscriptionsToView, SubscriptionToView.BY_PHONE_NUMBER_AND_BEGIN_DATE);
    return subscriptionsToView;
  }
  
  public static List<DeviceType> sortDeviceTypeByName(List<DeviceType> deviceTypes) {
    deviceTypes.sort(DeviceType.BY_NAME);
    return deviceTypes;
  }

  public static List<User> sortUserByName(List<User> users) {
    users.sort(User.BY_NAME);
    return users;
  }

  public static List<RawInvoice> sortRawInvoice(List<RawInvoice> rawInvoices) {
    rawInvoices.sort(RawInvoice.BY_INVOICE_NUMBER_ADN_ACCEPT_BY_COMPANY_AMD_DATE);
    return rawInvoices;
  }
  
  public static List<Invoice> sortInvoice(List<Invoice> invoices) {
    invoices.sort(Invoice.BY_INVOICE_NUMBER_AND_ACCEPT_BY_COMPANY_AMD_DATE);
    return invoices;
  }
  
  public static List<Category> sortCategoryByName(List<Category> categories) {
    categories.sort(Category.BY_NAME);
    return categories;
  }

  public static List<DescriptionCategoryCoupler> sortCouplerByName(List<DescriptionCategoryCoupler> couplers) {
    couplers.sort(DescriptionCategoryCoupler.BY_NAME);
    return couplers;
  }
  
  public static List<ChargeRatioByCategory> sortChargeRatioByName(List<ChargeRatioByCategory> chargeRaios) {
    chargeRaios.sort(ChargeRatioByCategory.BY_NAME);
    return chargeRaios;
  }

  public static List<Participant> sortParticipantByName(List<Participant> participants) {
    participants.sort(Participant.BY_NAME);
    return participants;
  }
  
  public static List<String> sortString(List<String> strings) {
    strings.sort(Comparator.naturalOrder());
    return strings;
  }

  public static List<Sim> sortSimByImei(List<Sim> sims) {
    sims.sort(Sim.BY_IMEI);
    return sims;
  }
}
