package com.project.nhatrotot.rest.controllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.http.HttpHeaders;
import com.project.nhatrotot.rest.api.PaymentApi;
import com.project.nhatrotot.rest.dto.CreateVnPayPaymentUrlRequestDto;
import com.project.nhatrotot.rest.dto.GetMyDetailsPayments200ResponseDto;
import com.project.nhatrotot.rest.dto.PaymentsPageDto;
import com.project.nhatrotot.rest.dto.VerifyManualPaymentRequestDto;
import com.project.nhatrotot.service.PaymentService;
import com.project.nhatrotot.util.PaymentUtil;

@RestController
@RequestMapping("/api")
public class PaymentController implements PaymentApi {

        @Autowired
        PaymentService paymentService;

        @Autowired
        PaymentUtil paymentUtil;

        @Override
        public ResponseEntity<Void> vnPayAfterPurchaseCallback(@NotNull @Valid String vnpTmnCode,
                        @NotNull @Valid String vnpTxnRef, @NotNull @Valid String vnpOrderInfo,
                        @NotNull @Valid BigDecimal vnpAmount, @NotNull @Valid String vnpResponseCode,
                        @NotNull @Valid String vnpBankCode, @NotNull @Valid String vnpCardType,
                        @NotNull @Valid String vnpPayDate, @NotNull @Valid String vnpTransactionNo,
                        @NotNull @Valid String vnpTransactionStatus, @NotNull @Valid String vnpSecureHash,
                        @Valid String vnpBankTranNo) {
                Map<String, String> vnp_Params = new HashMap<>();
                vnp_Params.put("vnp_TmnCode", vnpTmnCode);
                vnp_Params.put("vnp_TxnRef", vnpTxnRef);
                vnp_Params.put("vnp_Amount", vnpAmount.toString());
                vnp_Params.put("vnp_OrderInfo", vnpOrderInfo);
                vnp_Params.put("vnp_ResponseCode", vnpResponseCode);
                vnp_Params.put("vnp_BankCode", vnpBankCode);
                if (vnpBankTranNo != null) {
                        vnp_Params.put("vnp_BankTranNo", vnpBankTranNo);
                }
                vnp_Params.put("vnp_CardType", vnpCardType);
                vnp_Params.put("vnp_PayDate", vnpPayDate);
                vnp_Params.put("vnp_TransactionNo", vnpTransactionNo);
                vnp_Params.put("vnp_TransactionStatus", vnpTransactionStatus);
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                                .getRequest();
                String queryString = request.getQueryString();
                String queryStringExcludedHashCode = queryString.substring(0,
                                queryString.length() - (vnpSecureHash.length() + 16));
                if (paymentUtil.createSHA512(queryStringExcludedHashCode).equals(vnpSecureHash)) {
                        try {
                                return paymentService.createVnpPayment(vnp_Params);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", paymentUtil.getVnp_RedirectURL() + "/payment/error");
                return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
        }

        @Override
        public ResponseEntity<String> createVnPayPaymentUrl(
                        @Valid CreateVnPayPaymentUrlRequestDto createVnPayPaymentUrlRequestDto) {
                JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                                .getAuthentication();
                Jwt jwt = (Jwt) authenticationToken.getCredentials();
                String userId = (String) jwt.getClaims().get("sub");
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                                .getRequest();
                return new ResponseEntity<>(
                                paymentService.createVnPayUrl(userId,
                                                createVnPayPaymentUrlRequestDto.getOrderAttachment(),
                                                createVnPayPaymentUrlRequestDto.getAmount(),
                                                request.getRemoteAddr()),
                                HttpStatus.OK);

        }

        @Override
        public ResponseEntity<Void> createManualPaymentRequest(
                        @Valid CreateVnPayPaymentUrlRequestDto createVnPayPaymentUrlRequestDto) {
                JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                                .getAuthentication();
                Jwt jwt = (Jwt) authenticationToken.getCredentials();
                String userId = (String) jwt.getClaims().get("sub");
                paymentService.createManualPayment(userId, createVnPayPaymentUrlRequestDto.getOrderAttachment(),
                                createVnPayPaymentUrlRequestDto.getAmount());
                return new ResponseEntity<>(HttpStatus.OK);
        }

        @Override
        @PreAuthorize("hasAnyRole('admin','sub_admin')")
        public ResponseEntity<PaymentsPageDto> getAkkPaymentsHandle(@Min(0) @Valid Integer page,
                        @Min(1) @Valid Integer size, @Valid Integer type, @Valid String userEmail,
                        @Valid String paymentState, @Valid String sortBy, @Valid String sortOrder) {
                var payments = paymentService.getPaymentsPageDto(page, size, type, userEmail, paymentState, sortBy,
                                sortOrder);
                return new ResponseEntity<>(payments, HttpStatus.OK);
        }

        @Override
        @PreAuthorize("hasAnyRole('admin','sub_admin')")
        public ResponseEntity<Void> verifyManualPayment(
                        @Valid VerifyManualPaymentRequestDto verifyManualPaymentRequestDto) {
                JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                                .getAuthentication();
                Jwt jwt = (Jwt) authenticationToken.getCredentials();
                String userId = (String) jwt.getClaims().get("sub");
                paymentService.verifyManualPayment(verifyManualPaymentRequestDto.getPaymentId().toString(), userId,
                                verifyManualPaymentRequestDto.getState().getValue());
                return new ResponseEntity<>(HttpStatus.OK);
        }

        @Override
        @PreAuthorize("hasAnyRole('admin','sub_admin')")
        public ResponseEntity<GetMyDetailsPayments200ResponseDto> getDetailsPayments(UUID paymentId) {
                GetMyDetailsPayments200ResponseDto response = paymentService
                                .getDetailsPayments200ResponseDto(paymentId.toString());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

}
