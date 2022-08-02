package com.usu.satu.controller;

import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.model.Billing;
import com.usu.satu.dto.UserJWT;
import com.usu.satu.helper.ResponseBody;
import com.usu.satu.service.BillingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    BillingService billingService;

    private static final Logger logger = LogManager.getLogger(BillingController.class);

    @PostMapping
    public ResponseEntity addBillings(@RequestAttribute UserJWT userdata) {
        try {
            HashMap<String, Object> billing = billingService.createBilling(userdata.getIdentity());
            if (billing == null) {
                return new ResponseBody().notFound("billing data is empty", null);
            } else {
                return new ResponseBody().found("success add billing", billing);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseBody().failed("failed to add billing", e.getMessage());
        }
    }

    @PostMapping("/nim/{nim}")
    public ResponseEntity addBillingByNim(@PathVariable String nim) {
        try {
            HashMap<String, Object> billing = billingService.createBilling(nim);
            if (billing == null) {
                return new ResponseBody().notFound("billing data is empty", null);
            } else {
                return new ResponseBody().found("success add billing", billing);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseBody().failed("failed to add billing", e.getMessage());
        }
    }

    @PostMapping("/callback")
    public HashMap<String, String> updateBillings(@RequestParam Map<String,String> billingReq) {
        HashMap<String, String> response = new HashMap<>();

        try {
            billingService.addBillingHistory(billingReq);
            response.put("code", "201");
            return response;
        } catch (IllegalArgumentException e) {
            response.put("code", "401");
            return response;
        } catch (Exception e) {
            logger.error("Reference-callbackBilling: "+e.getMessage());
            response.put("code", "500");
            return response;
        }
    }

    // check bill status 4 level
    @GetMapping("/check/me")
    public ResponseEntity checkBilling(@RequestAttribute UserJWT userdata) {
        try {
            HashMap<String, Object> check = billingService.checkBilling(userdata.getIdentity());
            int status = Integer.parseInt(String.valueOf(check.get("status")));
            Object data = check.get("data");
            if (status == 4) {
                return new ResponseBody().found("you have not billing", data);
            } else if (status == 3) {
                return new ResponseBody().found("your bill has expired", data);
            } else if (status == 2) {
                return new ResponseBody().found("pay your bill", data);
            } else if (status == 1) {
                return new ResponseBody().found("can create billing", data);
            } else {
                return new ResponseBody().found("can't create billing", data);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseBody().failed("failed to check billing", e.getMessage());
        }
    }

    @GetMapping("/check/nim/{nim}")
    public ResponseEntity checkBillingByNim(@PathVariable String nim) {
        try {
            HashMap<String, Object> check = billingService.checkBilling(nim);
            int status = Integer.parseInt(String.valueOf(check.get("status")));
            Object data = check.get("data");
            if (status == 4) {
                return new ResponseBody().found("you have not billing", data);
            } else if (status == 3) {
                return new ResponseBody().found("your bill has expired", data);
            } else if (status == 2) {
                return new ResponseBody().found("pay your bill", data);
            } else if (status == 1) {
                return new ResponseBody().found("can create billing", data);
            } else {
                return new ResponseBody().found("can't create billing", data);
            }
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseBody().failed("failed to check billing", e.getMessage());
        }
    }

    @GetMapping("/list/{nim}")
    public ResponseEntity getPaidList(@PathVariable String nim) {
        try {
            List<HashMap<String, Object>> listBill = billingService.billPaidList(nim);
            return new ResponseBody().found("billing list", listBill);
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e) {
            logger.error("getPaidList : " + e.getMessage());
            return new ResponseBody().failed("failed to listing billing", e.getMessage());
        }
    }

    @GetMapping("nim/{nim}")
    public ResponseEntity getBillDataList(@PathVariable String nim) {
        try {
            List<Billing> listBill = billingService.billingList(nim);
            return new ResponseBody().found("billing list", listBill);
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage(), null);
        } catch (Exception e) {
            logger.error("getBillDataList : " + e.getMessage());
            return new ResponseBody().failed("failed to listing billing", e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBilling(@PathVariable String id) {
        try {
            billingService.removeBilling(id);
            return new ResponseBody().found("delete billing", id);
        } catch (AcceptedException e) {
            return new ResponseBody().notFound(e.getMessage()+id, null);
        } catch (Exception e) {
            logger.error("deleteBilling : " + e.getMessage());
            return new ResponseBody().failed("failed to delete billing "+id, e.getMessage());
        }
    }
}
