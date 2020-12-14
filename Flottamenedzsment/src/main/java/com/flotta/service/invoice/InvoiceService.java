package com.flotta.service.invoice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.exception.invoice.FileUploadException;
import com.flotta.model.invoice.DescriptionCategoryCoupler;
import com.flotta.model.invoice.FeeItem;
import com.flotta.model.invoice.Invoice;
import com.flotta.model.invoice.Participant;
import com.flotta.model.invoice.RawFeeItem;
import com.flotta.model.invoice.RawInvoice;
import com.flotta.model.registry.Subscription;
import com.flotta.model.registry.User;
import com.flotta.repository.invoice.InvoiceRepository;
import com.flotta.repository.invoice.RawInvoiceRepository;
import com.flotta.service.registry.SubscriptionFinderService;
import com.flotta.utility.BooleanWithMessages;

/**
 * @author CzP
 *
 */
@Service
public class InvoiceService {

  private RawInvoiceRepository rawInvoiceRepository;

  private InvoiceRepository invoiceRepository;

  private InvoiceTemplateService invoiceTemplateService;

  private SubscriptionFinderService subscriptionService;

  private ParticipantFinderService participantService;

  @Autowired
  public InvoiceService(
      RawInvoiceRepository rawInvoiceRepository, 
      InvoiceRepository invoiceRepository,
      InvoiceTemplateService invoiceTemplateService,
      SubscriptionFinderService subscriptionService, 
      ParticipantService participantService) {
    this.rawInvoiceRepository = rawInvoiceRepository;
    this.invoiceRepository = invoiceRepository;
    this.invoiceTemplateService = invoiceTemplateService;
    this.subscriptionService = subscriptionService;
    this.participantService = participantService;
  }
  
  public List<Invoice> findAll() {
    return invoiceRepository.findAll();
  }

  public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) {
    return invoiceRepository.findByInvoiceNumber(invoiceNumber);
  }

  BooleanWithMessages uploadInvoice(MultipartFile file) {
    BooleanWithMessages eb = new BooleanWithMessages(true);
    String xmlString;
    try {
      xmlString = getXMLString(file);
      
      RawInvoice rawInvoice = parseXmlStringToRawInvoice(xmlString);
      
      if (invoiceRepository.findByInvoiceNumber(rawInvoice.getInvoiceNumber()).isPresent() || 
          rawInvoiceRepository.findByInvoiceNumber(rawInvoice.getInvoiceNumber()).isPresent()) {
        eb.setFalse();
        eb.addMessage(MessageKey.INVOICE_NUMBER_ALREADY_USED, MessageType.WARNING);
      } else {
        processRawInvoice(rawInvoice);
      }
    } catch (FileUploadException e) {
      eb.setFalse();
      eb.addMessage(e.getKey(), e.getType());
    }
  return eb;
  }
  
  void deleteInvoiceByInvoiceNumber(String invoiceNumber) {
    Optional<Invoice> invoiceOpt = invoiceRepository.findByInvoiceNumber(invoiceNumber);
    invoiceOpt.ifPresent(invoice -> invoiceRepository.delete(invoice));
  }
  
  void acceptInvoiceByInvoiceNumberFromCompany(String invoiceNumber) {
    Optional<Invoice> invoiceOpt = invoiceRepository.findByInvoiceNumber(invoiceNumber);
    invoiceOpt.ifPresent(invoice -> {
      invoice.setAcceptedByCompany();
      invoiceRepository.save(invoice);
    });
  }

  public List<RawInvoice> findAllRawInvoice() {
    return rawInvoiceRepository.findAll();
  }
  
  void restartProcessingRawInvoiceByInvoiceNumber(String invoiceNumber) {
    Optional<RawInvoice> rawInvoiceOpt= rawInvoiceRepository.findByInvoiceNumber(invoiceNumber);
    rawInvoiceOpt.ifPresent(rawInvoice -> {
      rawInvoice.clearProblem();
      if(processRawInvoice(rawInvoice)) {
        rawInvoiceRepository.delete(rawInvoice);
      }
    });
  }

  void deleteRawInvoiceByInvoiceNumber(String invoiceNumber) {
    Optional<RawInvoice> optional = rawInvoiceRepository.findByInvoiceNumber(invoiceNumber);
    if(optional.isPresent()) {
      rawInvoiceRepository.delete(optional.get());
    }
  }
  
  List<String> findDescriptionsOfInvoiceByInvoiceNumber(String invoiceNumber) {
    Optional<Invoice> optionalInvoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
    Optional<RawInvoice> optionalRawInvoice = rawInvoiceRepository.findByInvoiceNumber(invoiceNumber);
    if (optionalInvoice.isPresent()) {
      return optionalInvoice.get().getAllDescription();
    }
    if(optionalRawInvoice.isPresent()) {
      return optionalRawInvoice.get().getAllDescription();
    }
    return new LinkedList<>();
  }
  
  private RawInvoice parseXmlStringToRawInvoice(String xml) throws FileUploadException {
    Element root = getTreeFromXMLString(xml);

    if (invoiceTemplateService.invoiceTreeFormalCheck(root)) {
      RawInvoice rawInvoice = new RawInvoice();
      try {
        rawInvoice.setBeginDate(LocalDate.parse(getFirstTagValue(root, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
        rawInvoice.setEndDate(LocalDate.parse(getFirstTagValue(root, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
        rawInvoice.setInvoiceNumber(getFirstTagValue(root, "InvNb"));
        rawInvoice.setNetAmount(Double.valueOf(getFirstTagValue(root, "InvTotalNetA").replace(',', '.')));
        rawInvoice.setTaxAmount(Double.valueOf(getFirstTagValue(root, "InvTotalTaxA").replace(',', '.')));
        rawInvoice.setGrossAmount(Double.valueOf(getFirstTagValue(root, "InvTotalGrossA").replace(',', '.')));

        Element companyData = (Element) root.getElementsByTagName("CompanyData").item(0);
        rawInvoice.setCompanyName(getFirstTagValue(companyData, "Name"));
        rawInvoice.setCompanyAddress(getFirstTagValue(companyData, "City"));

        NodeList feeItemNodes = root.getElementsByTagName("FeeItem");
        for (int i = 0; i < feeItemNodes.getLength(); i++) {
          Element feeItemElement = (Element) feeItemNodes.item(i);
          rawInvoice.addFeeItem(parseToFeeItem(feeItemElement));
        }
      } catch (DateTimeParseException | NumberFormatException e) {
        throw new FileUploadException(e.getMessage());
      }

      return rawInvoice;
    } else {
      throw new FileUploadException(MessageKey.NOT_VALID_INVOICE_STUCTURE, MessageType.WARNING);
    }
  }
  
  private boolean processRawInvoice(RawInvoice rawInvoice) {
    if (check(rawInvoice)) {
      Invoice invoice = parseRawInvoiceToInvoice(rawInvoice);
      invoiceRepository.save(invoice);
      return true;
    } else {
      rawInvoiceRepository.save(rawInvoice);
      return false;
    }
  }

  private boolean check(RawInvoice rawInvoice) {
    Optional<Participant> optionalParticipant = participantService.findByName(rawInvoice.getCompanyName());
    if (optionalParticipant.isPresent()) {
      DescriptionCategoryCoupler dcc = optionalParticipant.get().getDescriptionCategoryCoupler();
      for (RawFeeItem rawFeeItem : rawInvoice.getFeeItems()) {
        if (dcc.getCategoryByDescription(rawFeeItem.getDescription()) == null) {
          rawInvoice.addProblem("Ismeretlen leírás: " + rawFeeItem.getDescription());
        }
      }
    } else {
      rawInvoice.addProblem("Ismeretlen feladó: " + rawInvoice.getCompanyName());
    }
    Set<User> users = new HashSet<>();
    for (RawFeeItem rawFeeItem : rawInvoice.getFeeItems()) {
      Optional<Subscription> optionalSubscription = subscriptionService.findByNumber(rawFeeItem.getSubscription());
      if (!optionalSubscription.isPresent()) {
        rawInvoice.addProblem("Ismeretlen telefonszám: " + rawFeeItem.getSubscription());
      } else {
        users.addAll(optionalSubscription.get().getUsersBetween(rawFeeItem.getBeginDate(), rawFeeItem.getEndDate()));
      }
    }
    for(User user : users) {
      if(user.getChargeRatio() == null) {
        rawInvoice.addProblem("Nincs költség aránya: " + user.getEmail());
      }
    }
    return !rawInvoice.hasProblem();
  }

  private Invoice parseRawInvoiceToInvoice(RawInvoice rawInvoice) {
    Invoice invoice = new Invoice();

    invoice.setInvoiceNumber(rawInvoice.getInvoiceNumber());
    invoice.setBeginDate(rawInvoice.getBeginDate());
    invoice.setEndDate(rawInvoice.getEndDate());
    invoice.setNetAmount(rawInvoice.getNetAmount());
    invoice.setTaxAmount(rawInvoice.getTaxAmount());
    invoice.setGrossAmount(rawInvoice.getGrossAmount());
    invoice.setCompany(participantService.findByName(rawInvoice.getCompanyName()).get());

    for (RawFeeItem rawFeeItem : rawInvoice.getFeeItems()) {
      FeeItem feeItem = parseRawFeeItemToFeeItem(rawFeeItem);
      Subscription subscription = subscriptionService.findByNumber(feeItem.getSubscription()).get();
      invoice.processAndAddFeeItem(subscription, feeItem);
    }
    return invoice;
  }

  private FeeItem parseRawFeeItemToFeeItem(RawFeeItem rawFeeItem) {
    FeeItem feeItem = new FeeItem();
    feeItem.setBeginDate(rawFeeItem.getBeginDate());
    feeItem.setEndDate(rawFeeItem.getEndDate());
    feeItem.setNetAmount(rawFeeItem.getNetAmount());
    feeItem.setDescription(rawFeeItem.getDescription());
    feeItem.setSubscription(rawFeeItem.getSubscription());
    feeItem.setTaxAmount(rawFeeItem.getTaxAmount());
    feeItem.setGrossAmount(rawFeeItem.getGrossAmount());
    return feeItem;
  }
  
  private RawFeeItem parseToFeeItem(Element feeItemElement) {
    RawFeeItem rawFeeItem = new RawFeeItem();
    rawFeeItem.setSubscription(getFirstTagValue(feeItemElement, "ItemNr"));
    rawFeeItem.setDescription(getFirstTagValue(feeItemElement, "Desc"));
    rawFeeItem.setBeginDate(LocalDate.parse(getFirstTagValue(feeItemElement, "Begin"),DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
    rawFeeItem.setEndDate(LocalDate.parse(getFirstTagValue(feeItemElement, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
    rawFeeItem.setNetAmount(Double.valueOf(getFirstTagValue(feeItemElement, "NetA").replace(',', '.')));
    rawFeeItem.setTaxAmount(Double.valueOf(getFirstTagValue(feeItemElement, "TaxA").replace(',', '.')));
    rawFeeItem.setGrossAmount(Double.valueOf(getFirstTagValue(feeItemElement, "GrossA").replace(',', '.')));
    return rawFeeItem;
  }

  private String getFirstTagValue(Element root, String tagname) {
    return root.getElementsByTagName(tagname).item(0).getFirstChild().getNodeValue();
  }

  private Element getTreeFromXMLString(String xmlString) throws FileUploadException {
    try {
      DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));

      doc.getDocumentElement().normalize();

      return doc.getDocumentElement();
    } catch (ParserConfigurationException | IOException | SAXException e) {
      throw new FileUploadException(MessageKey.NOT_VALID_XML, MessageType.WARNING);
    }
  }

  private String getXMLString(MultipartFile file) throws FileUploadException {
    if (file.isEmpty()) {
      throw new FileUploadException(MessageKey.EMPTY_FILE, MessageType.WARNING);
    }

    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        line = br.readLine();
      }

      return sb.toString().replaceAll(">\\s+<", "><");
    } catch (IOException e) {
      throw new FileUploadException(MessageKey.FILE_CANT_READ, MessageType.WARNING);
    }
  }
}