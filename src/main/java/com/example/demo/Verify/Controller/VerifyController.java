package com.example.demo.Verify.Controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class VerifyController {

    @Value("${iamport.apikey}")
    String apiKey;
    @Value("${iamport.apisecret}")
    String apiSecret;
    private final IamportClient iamportClient;

    public VerifyController(){
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    @GetMapping("/valifyIamport/test")
    public String test(){
        return "index";
    }

    @GetMapping("/verifyIamport/mobile")
    public String verifyIamportMobilePayment(@RequestParam(required = false) String imp_uid,
                                             @RequestParam(required = false) String merchant_uid,
                                             Model model,
                                             Locale locale,
                                             HttpSession httpSession) throws IamportResponseException, IOException
    {
        IamportResponse<Payment> result = this.paymentByImpUid(imp_uid);

        System.out.println("접근");

        if(result.getResponse().getAmount().compareTo(BigDecimal.ONE) == 0){
            System.out.println("아임포트 검증완료");
        }

        return "test";
    }

    /** 프론트에서 받은 PG사 결과값을 통해 아임포트 토큰 발행 */
    @PostMapping("/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable String imp_uid)
        throws IamportResponseException, IOException{
        log.info("paymentByImpUid Entered");
        return iamportClient.paymentByImpUid(imp_uid);
    }
}
