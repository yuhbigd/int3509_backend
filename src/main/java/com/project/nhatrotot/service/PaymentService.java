package com.project.nhatrotot.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.io.BigDecimalParser;
import com.project.nhatrotot.mapper.AdminPaymentDtoMapper;
import com.project.nhatrotot.mapper.ManualPaymentDtoMapper;
import com.project.nhatrotot.mapper.VnpPaymentMapper;
import com.project.nhatrotot.model.ManualPayment;
import com.project.nhatrotot.model.Payment;
import com.project.nhatrotot.model.PaymentState;
import com.project.nhatrotot.model.VnPayment;
import com.project.nhatrotot.repository.jpa.ManualPaymentRepository;
import com.project.nhatrotot.repository.jpa.PaymentRepository;
import com.project.nhatrotot.repository.jpa.PaymentTypeRepository;
import com.project.nhatrotot.repository.jpa.UserEntityRepository;
import com.project.nhatrotot.repository.jpa.VnpayRespository;
import com.project.nhatrotot.rest.advice.CustomException.GeneralException;
import com.project.nhatrotot.rest.dto.GetMyDetailsPayments200ResponseDto;
import com.project.nhatrotot.rest.dto.ManualPaymentDetailDto;
import com.project.nhatrotot.rest.dto.PaymentsPageDto;
import com.project.nhatrotot.rest.dto.VnpPaymentDetailsDto;
import com.project.nhatrotot.util.PaymentUtil;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    UserEntityRepository userEntityRepository;
    @Autowired
    PaymentTypeRepository paymentTypeRepository;
    @Autowired
    VnpayRespository vnPayRepository;
    @Autowired
    ManualPaymentRepository manualPaymentRepository;
    @Autowired
    PaymentUtil paymentUtil;
    @Autowired
    private AdminPaymentDtoMapper adminPaymentDtoMapper;
    @Autowired
    private ManualPaymentDtoMapper manualPaymentDtoMapper;
    @Autowired
    private VnpPaymentMapper vnpPaymentMapper;

    public GetMyDetailsPayments200ResponseDto getDetailsPayments200ResponseDto(String paymentId) {
        var paymentOpt = paymentRepository.findById(paymentId);
        if (!paymentOpt.isPresent()) {
            throw new GeneralException("payment not found", HttpStatus.NOT_FOUND);
        }
        var payment = paymentOpt.get();

        if (payment.getType().getId().intValue() == 1) {
            var manualPaymentOpt = manualPaymentRepository.findById(paymentId);
            if (!manualPaymentOpt.isPresent()) {
                throw new GeneralException("payment not found", HttpStatus.NOT_FOUND);
            }
            ManualPaymentDetailDto manualPaymentDetailDto = manualPaymentDtoMapper
                    .convertFromManualPayment(manualPaymentOpt.get());
            return manualPaymentDetailDto;
        } else if (payment.getType().getId().intValue() == 2) {
            var VnPaymentOpt = vnPayRepository.findByPayment_PaymentIdEquals(paymentId);
            if (!VnPaymentOpt.isPresent()) {
                throw new GeneralException("payment not found", HttpStatus.NOT_FOUND);
            }
            VnpPaymentDetailsDto vnpay = vnpPaymentMapper.convertFromVnPayment(VnPaymentOpt.get());
            return vnpay;
        }
        return null;
    }

    @Transactional(rollbackFor = { Exception.class, Throwable.class }, isolation = Isolation.READ_COMMITTED)
    public PaymentsPageDto getPaymentsPageDto(Integer page,
            Integer size, Integer type, String userEmail,
            String paymentState, String sortBy, String sortOrder) {
        var sort = Sort.by("createdAt").descending();
        if (sortBy.equals("asc")) {
            sort = Sort.by("createdAt").ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Payment> paymentsPage;
        if (type == null) {
            paymentsPage = paymentRepository.findByUser_EmailContainingAndPaymentStateContainingAllIgnoreCase(userEmail,
                    paymentState, pageable);
            System.out.println(paymentsPage.getContent());
        } else {
            paymentsPage = paymentRepository
                    .findByType_IdEqualsAndUser_EmailContainingAndPaymentStateContainingAllIgnoreCase(type, userEmail,
                            paymentState, pageable);
        }
        var totalPage = paymentsPage.getTotalPages();
        var content = adminPaymentDtoMapper.convertFromPayments(paymentsPage.getContent());
        var result = new PaymentsPageDto();
        result.setCurrentPage(page);
        result.setPayments(content);
        result.setTotalPage(totalPage);
        return result;
    }

    @Transactional
    public String createVnPayUrl(String userId, String info, BigDecimal amount, String ip) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = "CNRBV68S";
        String orderType = "260000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        var vnp_CreateDate = LocalDateTime.now().atZone(ZoneId.of("Etc/GMT+7")).format(formatter);
        var vnp_ExpireDate = LocalDateTime.now().plusMinutes(15).atZone(ZoneId.of("Etc/GMT+7")).format(formatter);
        String vnp_CurrCode = "VND";
        String vnp_IpAddr = ip;
        String vnp_Locale = "vn";
        String vnp_ReturnUrl = paymentUtil.getVnp_ReturnUrl();
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setTotal(amount);
        payment.setUser(userEntityRepository.findById(userId).get());
        payment.setPaymentState(PaymentState.pending);
        payment.setPaymentDescription(info);
        var option = paymentTypeRepository.findById(2);
        payment.setType(option.get());
        UUID uuid = UUID.randomUUID();
        payment.setPaymentId(uuid.toString());
        String vnp_TxnRef = payment.getPaymentId();
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", amount.toString());
        vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", info);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        String url = paymentUtil.createUrlValueFromMap(vnp_Params);
        String vnp_SecureHash = paymentUtil.createSHA512(url.toString());
        url += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" + "?" + url;
        paymentRepository.save(payment);
        return paymentUrl;
    }

    @Transactional(rollbackFor = { Exception.class, Throwable.class }, isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<Void> createVnpPayment(Map<String, String> params) {
        var optionalPayment = paymentRepository.findById(params.get("vnp_TxnRef"));
        if (optionalPayment.isPresent()) {
            var payment = optionalPayment.get();
            if (params.get("vnp_TransactionStatus").equals("00")) {
                if (payment.getPaymentState().equals(PaymentState.pending)) {
                    payment.setPaymentState(PaymentState.done);
                    paymentRepository.save(payment);
                    VnPayment vnPayment = new VnPayment();
                    vnPayment.setId(params.get("vnp_TransactionNo"));
                    vnPayment.setPayment(payment);
                    vnPayment.setStatus(params.get("vnp_TransactionStatus"));
                    vnPayment.setVnpAmount(BigDecimalParser.parse(params.get("vnp_Amount")));
                    vnPayment.setVnpBankCode(params.get("vnp_BankCode"));
                    vnPayment.setVnpBankTransNo(params.get("vnp_BankTranNo"));
                    vnPayment.setVnpCardType(params.get("vnp_CardType"));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    LocalDateTime dateTime = LocalDateTime.parse(params.get("vnp_PayDate"), formatter);
                    vnPayment.setVnpDate(dateTime);
                    vnPayment.setVnpOrderInfo(params.get("vnp_OrderInfo"));
                    vnPayRepository.save(vnPayment);
                    userEntityRepository.updateBalance(payment.getUser().getUserId(),
                            BigDecimalParser.parse(params.get("vnp_Amount")));
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Location", paymentUtil.getVnp_RedirectURL() + "/payment/done");
                    return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
                }
            } else {
                payment.setPaymentState(PaymentState.cancelled);
                paymentRepository.save(payment);

            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", paymentUtil.getVnp_RedirectURL() + "/payment/error");
        return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
    }

    public void createManualPayment(String userId, String orderDescription, BigDecimal total) {
        Payment payment = new Payment();
        String id = UUID.randomUUID().toString();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setTotal(total);
        payment.setUser(userEntityRepository.findById(userId).get());
        payment.setPaymentState(PaymentState.pending);
        payment.setPaymentDescription(orderDescription);
        var option = paymentTypeRepository.findById(1);
        payment.setType(option.get());
        payment.setPaymentId(id);
        paymentRepository.save(payment);
    }

    @Transactional(rollbackFor = { Exception.class, Throwable.class }, isolation = Isolation.REPEATABLE_READ)
    public void verifyManualPayment(String paymentId, String verifierId, String state) {
        var paymentOpt = paymentRepository.findById(paymentId);
        if (!paymentOpt.isPresent()) {
            throw new GeneralException("payment not found", HttpStatus.NOT_FOUND);
        }
        var payment = paymentOpt.get();
        if (!payment.getPaymentState().equals(PaymentState.pending) || payment.getType().getId().intValue() != 1) {
            throw new GeneralException("This payment can't be verified", HttpStatus.NOT_FOUND);
        }
        ManualPayment manualPayment = new ManualPayment();
        var verifier = userEntityRepository.findById(verifierId).get();
        manualPayment.setPayment(payment);
        manualPayment.setCreator(payment.getUser());
        manualPayment.setVerifier(verifier);
        manualPayment.setCreatedAt(LocalDateTime.now());
        manualPayment.setPaymentId(payment.getPaymentId());
        if (state.equals(PaymentState.cancelled.name())) {
            payment.setPaymentState(PaymentState.cancelled);
            manualPayment.setAmount(new BigDecimal(0));
        } else {
            payment.setPaymentState(PaymentState.done);
            BigDecimal amount = payment.getTotal();
            manualPayment.setAmount(amount);
            userEntityRepository.updateBalance(payment.getUser().getUserId(), amount);
        }
        paymentRepository.save(payment);
        manualPaymentRepository.save(manualPayment);

    }
}
