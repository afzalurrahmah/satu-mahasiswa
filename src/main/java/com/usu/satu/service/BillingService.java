package com.usu.satu.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.usu.satu.dto.*;
import com.usu.satu.exeption.AcceptedException;
import com.usu.satu.model.Billing;
import com.usu.satu.helper.FormatData;
import com.usu.satu.helper.SIAGenerate;
import com.usu.satu.model.Student;
import com.usu.satu.model.StudentStatus;
import com.usu.satu.model.StudyCard;
import com.usu.satu.repository.BillingRepository;
import com.usu.satu.repository.StatusRepository;
import com.usu.satu.repository.StudentRepository;
import com.usu.satu.repository.StudyCardRepository;
import org.bson.types.Decimal128;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BillingService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SIAGenerate siaGenerate;

    @Autowired
    StudentService studentService;

    @Autowired
    BillingRepository billingRepository;

    @Autowired
    FormatData formatData;

    @Autowired
    PeriodService periodService;

    @Autowired
    StudyCardRepository studyCardRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    StatusService statusService;

//    @Value("${server.billing}")
//    private String billingServer;

//    @Value("${server.finance}")
//    private String financeServer;

    @Value("${app.env}")
    private String env;

//    @Value("${prefix}")
//    private String prefix;
//
//    @Value("${prefix.dev}")
//    private String prefixDev;
//
//    @Value("${cid}")
//    private String cid;
//
//    @Value("${cid.dev}")
//    private String cidDev;

    @Value("${server.backend}")
    private String baseUrl;

    String billingServer = "https://tagihan.usu.ac.id";
    String financeServer = "https://keuangan.usu.ac.id";

    private static final Logger logger = LogManager.getLogger(BillingService.class);

//    private String getCid(){
//        return env.equalsIgnoreCase("dev") ? cidDev : cid;
//    }
//    private String getPrefix(){
//        return env.equalsIgnoreCase("dev") ? prefixDev : prefix;
//    }

    public HashMap<String,Object> createBilling(String nim) {
        Optional<Student> data = studentRepository.findByNimAndIsDeletedIsFalse(nim);

        if (data.isEmpty()){
            throw new AcceptedException("your data is not exist for create billing "+nim);
        } else {
            logger.info("start process create billing");
            Student student = data.get();

            // get last billing
            List<Billing> billings = billingRepository.findBillingsByNim(nim);
            billings.sort(new FormatData.sortCreatedAtBilling());
            Billing bill = billings.get(0);

            HashMap<String, Object> periodActive    = siaGenerate.periodActive(studentService.getMajorCode(nim));
            HashMap<String, Object> majorDetail     = siaGenerate.majorDetail(student.getMajorCode());
            String level                            = majorDetail.get("education_level").toString();
            String facultyId                        = majorDetail.get("faculty_id").toString();
            String year                             = periodActive.get("academic_year").toString().split("/")[0];
            String semester                         = periodActive.get("semester").toString().equalsIgnoreCase("ganjil") ? "1" : "2";
            String expiredDate                      = periodActive.get("payment_end").toString();

            RestTemplate restTemplate   = new RestTemplate();
            HttpHeaders headers         = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String request = restTemplate.exchange(billingServer+"/api/billing?items.ref="+nim+"&items.item_name=spp "+level+" "+facultyId+" "+year+" "+semester, HttpMethod.GET, entity, String.class).getBody();

            JSONObject result   = new JSONObject(request);
            JSONObject jsonData = result.getJSONObject("data");

            logger.info("success hit billing server for check bill");

            String totalData = jsonData.get("total_data").toString();
            int totalDataInt = Integer.parseInt(totalData);

            String reqKeu           = restTemplate.exchange(financeServer+"/api/reference/faculty/find-bank/active-student/"+nim, HttpMethod.GET, entity, String.class).getBody();
            Gson gson               = new GsonBuilder().create();
            JsonObject jsonKeu      = gson.fromJson(reqKeu, JsonObject.class);
            JsonObject resKeu       = jsonKeu.getAsJsonObject("data");

            logger.info("success hit finance server for get data bank");

            JsonObject resInfo      = resKeu.getAsJsonObject("bank_info");
            JsonArray arrKeu        = resKeu.getAsJsonArray("reception_codes");
            String bankId           = resKeu.get("bank_id").getAsString();

            String cid;
            if (env.equalsIgnoreCase("dev")) {
                cid = resKeu.get("cid_dev").getAsString();
            } else {
                cid = resKeu.get("cid_live").getAsString();
            }

            String va;
            // cek apakah udah buat tagihan //
            if (totalDataInt == 0) { // jika belum buat ... //
                va = this.createNewVirtualAccount(nim, resKeu);
            } else { // jika sudah buat //
                JSONArray jsonBillings = jsonData.getJSONArray("billings");
                JSONObject billing = jsonBillings.getJSONObject(0); // tagihan urutan pertama adalah tagihan terakhir dibuat //

                // cek jika tagihan sudah dibuat dan pembayarannya //
                String billStatus = billing.getString("billing_status");
                if (billStatus.equalsIgnoreCase("unpaid")) { // jika belum dibayar //
                    va = billing.getString("virtual_account");
                } else { // jika sudah dibayar //
//                    va = this.createNewVirtualAccount(nim, resKeu);
                    throw new AcceptedException("no data billing");
                }
            }
            logger.info("success create va : "+va);

            // send data to billing server //
            BillingRequest billingRequest = new BillingRequest();
            billingRequest.setRef("akademik-"+nim);
            billingRequest.setCustomerName(student.getName());
            billingRequest.setCustomerEmail(student.getEmail()==null?"tagihan@usu.ac.id":student.getEmail());
            billingRequest.setPhone(student.getTelephone()==null?"08":student.getTelephone());
            billingRequest.setVirtualAccount(va); // bakal bisa milih bank, kalo ga dipilih default bni
            billingRequest.setDescription("TAGIHAN AKADEMIK USU "+student.getName().toUpperCase(Locale.ROOT)+" "+nim); // dari sia
            billingRequest.setCid( cid );

            List<BillRequestItem> items = new ArrayList<>(); // ada data dari sia

            List<HashMap<String,Object>> hashMapList = new ArrayList<>();
            bill.getBillingItems().forEach(b->{
                final String[] codeRef = new String[1];
                final String[] itemName = new String[1];
                arrKeu.forEach(keu->{
                    if (keu.getAsJsonObject().get("reception_code_slug").getAsString().equalsIgnoreCase(b.getFeeTypeSlug())) {

                        String degree, degree1, degree2, names = null;
                        String name = keu.getAsJsonObject().get("reception_code_name").getAsString();
                        if ((name.toLowerCase(Locale.ROOT).contains("s1") || name.toLowerCase(Locale.ROOT).contains("s2") ||
                            name.toLowerCase(Locale.ROOT).contains("s3") || name.toLowerCase(Locale.ROOT).contains("d3") ||
                            name.toLowerCase(Locale.ROOT).contains("s4")) && name.toLowerCase(Locale.ROOT).contains("spp")) {
                            degree = name.split(" ")[1];
                            if (degree.length() == 2) {
                                degree1 = degree.substring(0,1);
                                degree2 = degree.substring(1,2);
                                names   = name.split(" ")[0]+" "+degree1+"-"+degree2;
                            } else {
                                names = name;
                            }
                        }

                        codeRef[0]  = keu.getAsJsonObject().get("reception_code").getAsString();
//                        itemName[0] = keu.getAsJsonObject().get("reception_code_name").getAsString()+" "+facultyId+" "+year+" "+semester+" "+student.getName()+" "+nim;
                        itemName[0] = names+" "+facultyId+" "+year+" "+semester+" "+student.getName()+" "+nim;

                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("name", itemName[0]);
                        hashMap.put("amount", b.getAmount());
                        hashMap.put("slug", b.getFeeTypeSlug());
                        hashMapList.add(hashMap);
                    }
                });

                BillRequestItem billingItem = new BillRequestItem();
                billingItem.setItemName(itemName[0]);
                billingItem.setItemPrice(b.getAmount()); // dari sia
                billingItem.setItemQty(1);
                billingItem.setCodeRef(codeRef[0]); // dari sia
                billingItem.setRef("akademik-"+nim);
                items.add(billingItem);
            });

            billingRequest.setItems(items);
            billingRequest.setUktInfo(bill.getUktLevel());
            billingRequest.setCurrency("IDR");
            billingRequest.setCallbackBill(baseUrl+"/billing/callback"); // set properties (get host)
//            billingRequest.setCallbackBill("https://b4f2-103-54-0-3.ngrok.io/billing/callback");
            billingRequest.setDatetimeExpired(expiredDate);

            ObjectMapper mapper = new ObjectMapper();
            List<HashMap<String, Object>> itemListMap = new ArrayList<>();
            items.forEach(e->{
                HashMap<String, Object> map =
                        mapper.convertValue(e, new TypeReference<HashMap<String, Object>>() {});
                itemListMap.add(map);
            });

            HashMap<String, Object> map = mapper.convertValue(billingRequest, new TypeReference<HashMap<String, Object>>() {});
            map.put("items", itemListMap);
            map.put("bank_id", bankId);

            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<HashMap<String, Object>> requestBody = new HttpEntity<>(map, headers);
            String requestBill = restTemplate.exchange(
                    "https://tagihan.usu.ac.id/api/new-create-billing",
                    HttpMethod.POST,
                    requestBody,
                    String.class).getBody();
            JSONObject resultBill = new JSONObject(requestBill);
            JSONObject jsonObject  = resultBill.getJSONObject("data");

            logger.info("success hit billing server for create billing");
//
            HashMap<String, Object> hashMapRes = formatData.jsonObjToHashmap(jsonObject);

//        https://git.usu.ac.id/Rifariha/NEW_SIREG/src/branch/master/src/main/java/id/ac/usu/registrasi/sireg20/services/StudentBillsService.java

            System.out.println(hashMapRes);

            Decimal128 trxDecimal = Decimal128.parse(hashMapRes.get("total_amount").toString());

            String billCreated = hashMapRes.get("datetime_created_iso8601").toString();
            String billExpired = hashMapRes.get("datetime_expired_iso8601").toString();

            String[] split = billCreated.split("T");
            String createdDate = split[0];
            String createdTime = split[1].split("\\.")[0];

            String[] splitEx = billExpired.split("T");
            String exDate = splitEx[0];
            String exTime = splitEx[1].split("\\.")[0];

            LocalDateTime timeCreated = LocalDateTime.parse(createdDate+" "+createdTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime timeExpired = LocalDateTime.parse(exDate+" "+exTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // save database //
            Billing billingStudent = new Billing();
            billingStudent.setTrxId(hashMapRes.get("transaction_id").toString());
            billingStudent.setNim(nim);
//            billingStudent.setPeriodId(periodActive.get("id").toString());
            billingStudent.setPeriodId(year+semester);
            billingStudent.setVirtualAccount(va);
            billingStudent.setTrxAmount(trxDecimal);
            billingStudent.setPaid(false);
            billingStudent.setUktLevel(student.getUktLevel());
            billingStudent.setDatetimePayment(null);
            billingStudent.setDatetimeCreated(timeCreated);
            billingStudent.setDatetimeExpired(timeExpired);
            billingStudent.setPaymentType("va");
            billingStudent.setBankCode(resInfo.get("code").getAsString());

            List<BillingItem> billingItems = new ArrayList<>();
            hashMapList.forEach(b->{
                BillingItem item = new BillingItem();
                item.setName(b.get("name").toString());
                item.setAmount(Decimal128.parse(b.get("amount").toString()));
                item.setFeeTypeSlug(b.get("slug").toString());
                billingItems.add(item);
            });

            billingStudent.setBillingItems(billingItems);
            billingRepository.save(billingStudent);
            // ----- //

            logger.info("success save data billing to db");

            // tampilan status pembayaran di ui //
            HashMap<String, Object> dataHashmap = new HashMap<>();
            dataHashmap.put("total_amount", hashMapRes.get("total_amount"));
            dataHashmap.put("payment_method", "virtual account");
            dataHashmap.put("virtual_account", hashMapRes.get("virtual_account"));
            dataHashmap.put("bill_created", hashMapRes.get("datetime_created_iso8601"));
            dataHashmap.put("bill_paid", hashMapRes.get("datetime_payment_iso8601"));
            dataHashmap.put("bill_expired", hashMapRes.get("datetime_expired_iso8601"));
            dataHashmap.put("invoice_number", hashMapRes.get("transaction_id"));
            dataHashmap.put("customer_detail", hashMapRes.get("customer_name")+" "+nim);
            dataHashmap.put("bank_code", resInfo.get("code").getAsString());

            logger.info("finish create billing");

            return dataHashmap;
        }
    }

    public Billing addBillingHistory(Map<String,String> billingReq) {
        Optional<Billing> optional = billingRepository.findBillingByTrxId(billingReq.get("trx_id"));
        if (optional.isEmpty()) {
            throw new AcceptedException("Transaction ID is not exist");
        } else {
            String billPaid    = billingReq.get("datetime_payment");
            String billCreated = billingReq.get("datetime_created");
            String billExpired = billingReq.get("datetime_expired");

            String[] splitP = billPaid.split("T");
            String createdDateP = splitP[0];
            String createdTimeP = splitP[1].split("\\.")[0];

            String[] split = billCreated.split("T");
            String createdDate = split[0];
            String createdTime = split[1].split("\\.")[0];

            String[] splitEx = billExpired.split("T");
            String exDate = splitEx[0];
            String exTime = splitEx[1].split("\\.")[0];

            LocalDateTime timePaid    = LocalDateTime.parse(createdDateP+" "+createdTimeP, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime timeCreated = LocalDateTime.parse(createdDate+" "+createdTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime timeExpired = LocalDateTime.parse(exDate+" "+exTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            Billing billingData = optional.get();
            billingData.setTrxAmount(Decimal128.parse(billingReq.get("trx_amount")));
            billingData.setDatetimePayment(timePaid);
            billingData.setDatetimeCreated(timeCreated);
            billingData.setDatetimeExpired(timeExpired);
            billingData.setPaymentNtb(billingReq.get("payment_ntb"));
            billingData.setPaymentAmount(Decimal128.parse(billingReq.get("payment_amount")));
            billingData.setPaid(true);

            billingRepository.save(billingData);

            String nim      = billingData.getNim();
            // if bill paid //
            if (billingData.isPaid()){
                HashMap<String, Object> periodActive    = siaGenerate.periodActive(studentService.getMajorCode(nim));
                String periodIdActive                   = periodActive.get("id").toString();
                Student student                         = studentService.setLastStatus(nim, "nim", periodIdActive, "get");

                // add status "spp" //
                // jika periodId sudah ada dan status pertama registrasi //
//                if (statusService.checkRegistered(nim, periodIdActive)) {
//                    String schemaId = siaGenerate.periodActive(student.getMajorCode()).get("sk_number").toString();

//                    StudentStatus studentStatus = statusService.updateStatus(student.getNim(), periodIdActive, "spp", true ,schemaId);
                    StudentStatus studentStatus = statusService.updateStatus(nim, periodIdActive, "spp", true ,null);
                    statusRepository.save(studentStatus);
//                }

                // create new krs //
                List<String> studyCardList = periodService.getPeriodIdListStudent(nim);
                if (!studyCardList.contains(periodIdActive) || studyCardList.isEmpty()) {
                    StudyCard studyCard = new StudyCard();
                    studyCard.setNim(nim);
                    studyCard.setPeriodTaken(periodIdActive);
                    studyCard.setScheduleId("dari sia");
                    studyCard.setStudentCourses(new ArrayList<>());
                    studyCardRepository.save(studyCard);
                }
            }
            return billingData;
        }
    }

//    private String createVirtualAccount(String nim){
//        String va;
//        String suffix;
//        if(env.equalsIgnoreCase("dev")){
//            suffix = String.format("%08d", Integer.parseInt(nim.substring(2)));
//        }else{
//            suffix = String.format("%010d", Integer.parseInt(nim));
//        }
//        va = String.format("%s%s%s",this.getPrefix(),this.getCid(),suffix);
//        return va;
//    }

    private String createNewVirtualAccount(String nim, JsonObject data){
        String va;
        String suffix;
        String prefix;
        String cid;
        int vaLength = data.get("bank_info").getAsJsonObject().get("va_length").getAsInt();
        if(env.equalsIgnoreCase("dev")){
            prefix = data.get("prefix_dev").getAsString();
            cid = data.get("cid_dev").getAsString();
            if(data.get("bank_info").getAsJsonObject().get("code").getAsString().equalsIgnoreCase("BNI")){
                suffix = String.format("%08d", Integer.parseInt(nim.substring(2)));
            }else{
                suffix = String.format("%010d", Integer.parseInt(nim));
            }
        }else{
            prefix = data.get("prefix_live").getAsString();
            cid = data.get("cid_live").getAsString();
            suffix = String.format("%010d", Integer.parseInt(nim));
        }
        va = String.format("%s%s%s",prefix,cid,suffix);
        if(va.length() != vaLength){
            throw new IllegalArgumentException("Va length not match");
        }
        return va;
    }

    public HashMap<String,Object> checkBilling(String nim) {
        Optional<Student> data = studentRepository.findByNimAndIsDeletedIsFalse(nim);

        if (data.isEmpty()){
            throw new AcceptedException("your data is not exist for create billing");
        } else {
            Student student = data.get();

            // check billing previous semester //
            HashMap<String, Object> periodActive    = siaGenerate.periodActive(student.getMajorCode());
            int yearNow                             = Integer.parseInt(periodActive.get("academic_year").toString().split("/")[0]);
            int semesterNow                         = periodActive.get("semester").toString().equalsIgnoreCase("ganjil") ? 1 : 2;

            int yearBfr;
            int semesterBfr;
            if (semesterNow == 1) {
                semesterBfr = 2;
                yearBfr = yearNow-1;
            } else {
                semesterBfr = 1;
                yearBfr = yearNow;
            }

            // naming  item.ref
            HashMap<String, Object> majorDetail     = siaGenerate.majorDetail(student.getMajorCode());
            String level                            = majorDetail.get("education_level").toString();
            String facultyId                        = majorDetail.get("faculty_id").toString();

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String requestBfr = restTemplate.exchange("https://tagihan.usu.ac.id/api/billing?items.ref=" + nim + "&items.item_name=spp " + level + " " + facultyId + " " + yearBfr + " " + semesterBfr, HttpMethod.GET, entity, String.class).getBody();
//            String request = restTemplate.exchange("https://tagihan.usu.ac.id/api/billing?items.ref=" + nim + "&items.item_name=spp", HttpMethod.GET, entity, String.class).getBody();

            JSONObject resultBfr = new JSONObject(requestBfr);
            JSONObject objectBfr = resultBfr.getJSONObject("data");
            JSONArray arrBfr = objectBfr.getJSONArray("billings");

            HashMap<String,Object> hashMap = new HashMap<>();

            if (arrBfr.length() == 0) {
                logger.info("no data billing in previous billing");
                hashMap.put("status", 0);
                hashMap.put("data", null);
                hashMap.put("message", "tidak dapat membuat tagihan");
            } else {
                // tagihan urutan pertama adalah tagihan terakhir dibuat //
                JSONObject billing = arrBfr.getJSONObject(0);
                // cek jika tagihan sudah dibuat dan pembayarannya //
                String billStatus = billing.getString("billing_status");
                String billExpired = billing.getString("datetime_expired_iso8601");

                String[] split = billExpired.split("T");
                String expiredDate = split[0];
                String expiredTime = split[1].split("\\.")[0];

                LocalDateTime timeExpired = LocalDateTime.parse(expiredDate+" "+expiredTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime timeNow     = formatData.getNowLocal();

                HashMap<String,Object> dataHashmap = new HashMap<>();

                // if billing previous semester has paid //
                if (billStatus.equalsIgnoreCase("paid")){
                    String requestNext = restTemplate.exchange("https://tagihan.usu.ac.id/api/billing?items.ref=" + nim + "&items.item_name=spp " + level + " " + facultyId + " " + yearNow + " " + semesterNow, HttpMethod.GET, entity, String.class).getBody();
                    JSONObject resultNext = new JSONObject(requestNext);
                    JSONObject objectNext = resultNext.getJSONObject("data");
                    JSONArray arrNext = objectNext.getJSONArray("billings");
                    // cek di tagihan semester sekarang, apakah udah buat tagihan
                    // jika tidak ada tagihan
                    if (arrNext.length() == 0) {
                        logger.info("create billing");
                        hashMap.put("data", dataHashmap);
                        hashMap.put("status", 1);
                        hashMap.put("message", null);
                    } else {
                        JSONObject bill1 = arrNext.getJSONObject(0);
                        String billStatus1 = bill1.getString("billing_status");
                        String billExpired1 = bill1.getString("datetime_expired_iso8601");

                        String[] split1 = billExpired1.split("T");
                        String expiredDate1 = split1[0];
                        String expiredTime1 = split1[1].split("\\.")[0];

                        LocalDateTime timeExpired1 = LocalDateTime.parse(expiredDate1+" "+expiredTime1, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        LocalDateTime timeNow1     = formatData.getNowLocal();

                        if (billStatus1.equalsIgnoreCase("paid")){
                            logger.info("no billing");

                            JSONArray jsonArray1 = bill1.getJSONArray("items");
                            List<HashMap<String,Object>> itemList = new ArrayList<>();
                            for (int i=0;i<jsonArray1.length();i++) {
                                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                HashMap<String,Object> hash = new HashMap<>();
                                hash.put("item_name", jsonObject.get("item_name"));
                                hash.put("item_price", jsonObject.get("item_price"));
                                itemList.add(hash);
                            }

                            dataHashmap.put("total_amount", bill1.get("total_amount"));
                            dataHashmap.put("payment_method", "virtual account");
                            dataHashmap.put("virtual_account", bill1.get("virtual_account"));
                            dataHashmap.put("bill_created", bill1.get("datetime_created_iso8601"));
                            dataHashmap.put("bill_paid", bill1.get("datetime_payment_iso8601"));
                            dataHashmap.put("bill_expired", bill1.get("datetime_expired_iso8601"));
                            dataHashmap.put("invoice_number", bill1.get("transaction_id"));
                            dataHashmap.put("customer_detail", bill1.get("customer_name")+" "+nim);
                            dataHashmap.put("bank_code", bill1.get("bank_code"));
                            dataHashmap.put("period", (semesterNow==1?"Ganjil":"Genap") +" "+( yearNow+"/"+(yearNow+1)));
                            dataHashmap.put("items", itemList);

                            hashMap.put("data", dataHashmap);
                            hashMap.put("status", 4);
                            hashMap.put("message", "your billing has paid");
                        } else {
                            // if not expired //
                            if (timeNow1.isBefore(timeExpired1)) {
                                logger.info("please pay your billing this semester");

                                JSONArray jsonArray1 = bill1.getJSONArray("items");
                                List<HashMap<String,Object>> itemList = new ArrayList<>();
                                for (int i=0;i<jsonArray1.length();i++) {
                                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                    HashMap<String,Object> hash = new HashMap<>();
                                    hash.put("item_name", jsonObject.get("item_name"));
                                    hash.put("item_price", jsonObject.get("item_price"));
                                    itemList.add(hash);
                                }

                                dataHashmap.put("total_amount", bill1.get("total_amount"));
                                dataHashmap.put("payment_method", "virtual account");
                                dataHashmap.put("virtual_account", bill1.get("virtual_account"));
                                dataHashmap.put("bill_created", bill1.get("datetime_created_iso8601"));
                                dataHashmap.put("bill_paid", bill1.get("datetime_payment_iso8601"));
                                dataHashmap.put("bill_expired", bill1.get("datetime_expired_iso8601"));
                                dataHashmap.put("invoice_number", bill1.get("transaction_id"));
                                dataHashmap.put("customer_detail", bill1.get("customer_name")+" "+nim);
                                dataHashmap.put("bank_code", bill1.get("bank_code"));
                                dataHashmap.put("period", (semesterNow==1?"Ganjil":"Genap") +" "+( yearNow+"/"+(yearNow+1)));
                                dataHashmap.put("items", itemList);

                                hashMap.put("status", 2);
                                hashMap.put("data", dataHashmap);
                                hashMap.put("message", "tagihan pada semester sebelumnya belum expired, masih aktif, silahkan melakukan pembayaran terlebih dahulu");
                            } else {
                                logger.info("your billing previous semester had expired");
                                hashMap.put("status", 3);
                                hashMap.put("data", dataHashmap);
                                hashMap.put("message", "silahkan hubungi fakultas atau biro akademik untuk menyelesaikan pembayaran tagihan yang sudah expired");
                            }
                        }
                    }
                } else {
                    // if not expired //
                    if (timeNow.isBefore(timeExpired)) {
                        logger.info("please pay your billing");
                        JSONArray jsonArray = billing.getJSONArray("items");
                        List<HashMap<String,Object>> itemList = new ArrayList<>();
                        jsonArray.forEach(e->{
                            JSONObject jsonObject = new JSONObject(e);
                            HashMap<String,Object> hash = new HashMap<>();
                            hash.put("item_name", jsonObject.get("item_name"));
                            hash.put("item_price", jsonObject.get("item_price"));
                            itemList.add(hash);
                        });

                        dataHashmap.put("total_amount", billing.get("total_amount"));
                        dataHashmap.put("payment_method", "virtual account");
                        dataHashmap.put("virtual_account", billing.get("virtual_account"));
                        dataHashmap.put("bill_created", billing.get("datetime_created_iso8601"));
                        dataHashmap.put("bill_paid", billing.get("datetime_payment_iso8601"));
                        dataHashmap.put("bill_expired", billing.get("datetime_expired_iso8601"));
                        dataHashmap.put("invoice_number", billing.get("transaction_id"));
                        dataHashmap.put("customer_detail", billing.get("customer_name")+" "+nim);
                        dataHashmap.put("bank_code", billing.get("bank_code"));
                        dataHashmap.put("period", (semesterBfr==1?"Ganjil":"Genap" )+" "+ (yearBfr+"/"+yearBfr+1));
                        dataHashmap.put("items", itemList);

                        hashMap.put("status", 2);
                        hashMap.put("data", dataHashmap);
                        hashMap.put("message", "tagihan pada semester sekarang belum expired, masih aktif, silahkan melakukan pembayaran");
                    } else {
                        logger.info("your billing this semester had expired");
                        hashMap.put("status", 3);
                        hashMap.put("data", dataHashmap);
                        hashMap.put("message", "tagihan pada semester sekarang sudah expired");
                    }
                }
            }
            return hashMap;
        }
    }

    public List<HashMap<String,Object>> billPaidList(String nim) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String request = restTemplate.exchange("https://tagihan.usu.ac.id/api/billing?items.ref=" + nim + "&items.item_name=spp", HttpMethod.GET, entity, String.class).getBody();

        JSONObject result = new JSONObject(request);
        JSONObject jsonData = result.getJSONObject("data");

        JSONArray jsonBillings = jsonData.getJSONArray("billings");

        System.out.println("jsonBillings : "+ jsonBillings);

        return new ArrayList<>();
    }

    public List<Billing> billingList(String nim) {
        List<Billing> billings = billingRepository.findBillingsByNim(nim);
        billings.sort(new FormatData.sortCreatedAtBilling());
        return billings;
    }

    public void removeBilling(String id) {
        billingRepository.deleteById(id);
    }
}


