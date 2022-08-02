package com.usu.satu.service;

import com.usu.satu.config.Constant;
import com.usu.satu.dto.BillingItem;
import com.usu.satu.model.*;
import com.usu.satu.repository.BillingRepository;
import com.usu.satu.repository.StatusRepository;
import com.usu.satu.repository.StudentRepository;
import com.usu.satu.storage.FilesStorageService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.Decimal128;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MigrationService {

    private static final Logger logger = LogManager.getLogger(MigrationService.class);

    @Autowired
    Environment env;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FilesStorageService filesStorageService;

    @Autowired
    BillingRepository billingRepository;

    @Autowired
    StatusService statusService;

    @Autowired
    StatusRepository statusRepository;

    public HashMap<String,Object> importStudentProfile(MultipartFile file){
        try {
            long start = System.currentTimeMillis();
            filesStorageService.saveFile(file,"/migration/profile",file.getOriginalFilename().split("\\.")[0],"data");

            long end = System.currentTimeMillis();
            System.out.println("CSV Time process :"+(end-start)+" ms.");

            return this.saveToStudent(file.getOriginalFilename());
        } catch (Exception e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public Integer importStudentPaymentH2h(MultipartFile file){
        try {
            long start = System.currentTimeMillis();
            filesStorageService.saveFile(file,"/migration/billing/h2h",file.getOriginalFilename().split("\\.")[0],"data");

            long end = System.currentTimeMillis();
            System.out.println("CSV Time process :"+(end-start)+" ms.");

            List<Billing> data = this.h2hSaveToBilling(file.getOriginalFilename());
            return data.size();
        } catch (Exception e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public Integer importStudentPaymentTagihan(MultipartFile file){
        try {
            long start = System.currentTimeMillis();
            filesStorageService.saveFile(file,"/migration/billing/tagihan",file.getOriginalFilename().split("\\.")[0],"data");

            long end = System.currentTimeMillis();
            System.out.println("CSV Time process :"+(end-start)+" ms.");

            List<Billing> data = this.tagihanSaveToBilling(file.getOriginalFilename());
            return data.size();
        } catch (Exception e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public HashMap<String,Object> saveToStudent(String filename) throws FileNotFoundException {
        Path fileDir = Paths.get("files");
        String dirLocation = fileDir.toFile().getAbsolutePath();

        File initialFile = new File(dirLocation+"/migration/profile/"+filename);
        InputStream targetStream = new FileInputStream(initialFile);
        List<Student> datalist = new ArrayList<>();
        List<StudentStatus> dataStatus = new ArrayList<>();

        List<Student> bucketList;
        List<StudentStatus> bucketListStatus;

        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(targetStream, StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withDelimiter(';').withQuote('"'))){
            long start = System.currentTimeMillis();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            long end = System.currentTimeMillis();
            System.out.println("GET RECORDS: "+(end-start));

            DateTimeFormatter  sdf =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (CSVRecord csvRecord : csvRecords) {
                System.out.println("Memproses data milik : " + csvRecord.get("mhsNama"));

//                if (csvRecord.get("mhsNama").equalsIgnoreCase("Fathira Alisya Syahli")) {
                    Student student = new Student();

                    HashMap<String, Object> admission = new HashMap<>();
                    JSONArray admissionList = new JSONArray(Constant.admissionList());
                    for (int i = 0; i < admissionList.length(); i++) {
                        JSONObject dataAdmission = admissionList.getJSONObject(i);
                        if (dataAdmission.get("sia_admission_code").toString().equalsIgnoreCase(csvRecord.get("mhsJlrrKode"))) {
                            dataAdmission.keySet().forEach(key -> {
                                if (dataAdmission.isNull(key)) {
                                    admission.put(key, null);
                                } else {
                                    admission.put(key, dataAdmission.get(key));
                                }
                            });
                        }
                    }

                    student.setAdmission(admission.get("admission_name") == null ? csvRecord.get("mhsJlrrKode") : admission.get("admission_name").toString());
                    student.setNim(csvRecord.get("mhsNiu"));
                    student.setName(csvRecord.get("mhsNama"));
                    student.setIdentityNumber(csvRecord.get("mhsNik").equals("NULL") || csvRecord.get("mhsNik").isEmpty() ? null : csvRecord.get("mhsNik"));
                    student.setEntryStatus(csvRecord.get("mhsStatusMasukPt").equalsIgnoreCase("B") ? "Baru" : csvRecord.get("mhsStatusMasukPt").equalsIgnoreCase("P") ? "Pindahan" : null);
                    student.setMajorCode(csvRecord.get("mhsProdiKode"));
                    student.setRegistrationNumber(csvRecord.get("mhsNomorTes").equalsIgnoreCase("NULL") || csvRecord.get("mhsNomorTes").isEmpty() ? null : csvRecord.get("mhsNomorTes"));
                    student.setRegistrationDate(csvRecord.get("mhsTanggalTerdaftar").equals("NULL") || csvRecord.get("mhsTanggalTerdaftar").equalsIgnoreCase("0000-00-00") || csvRecord.get("mhsTanggalTerdaftar").isEmpty() ? null : LocalDate.parse(csvRecord.get("mhsTanggalTerdaftar"), sdf));
                    student.setGender(csvRecord.get("mhsJenisKelamin").equalsIgnoreCase("L") ? "Laki-laki" : "Perempuan");
                    student.setBirthPlace(csvRecord.get("mhsTmptLahir").equalsIgnoreCase("NULL") || csvRecord.get("mhsTmptLahir").isEmpty() ? null : csvRecord.get("mhsTmptLahir"));
                    student.setBirthDate(Objects.equals(csvRecord.get("mhsTanggalLahir"), "NULL") || csvRecord.get("mhsTanggalLahir").equalsIgnoreCase("0000-00-00") || csvRecord.get("mhsTanggalLahir").isEmpty() ? null : LocalDate.parse(csvRecord.get("mhsTanggalLahir"), sdf));
                    student.setReligion(csvRecord.get("agamaNama").equalsIgnoreCase("NULL") || csvRecord.get("agamaNama").isEmpty() ? null : csvRecord.get("agamaNama"));
                    student.setBloodType(csvRecord.get("mhsgoldarah").equalsIgnoreCase("NULL") || csvRecord.get("mhsgoldarah").isEmpty() ? null : csvRecord.get("mhsgoldarah"));
                    student.setSiblings(csvRecord.get("mhsJumlahSaudara").equalsIgnoreCase("NULL") || csvRecord.get("mhsJumlahSaudara").isEmpty() ? 0 : Integer.parseInt(csvRecord.get("mhsJumlahSaudara")));
                    student.setDependents(csvRecord.get("mhsJumlahTanggungan").equalsIgnoreCase("NULL") || csvRecord.get("mhsJumlahTanggungan").isEmpty() ? 0 :Integer.parseInt(csvRecord.get("mhsJumlahTanggungan")));
                    student.setNationality(csvRecord.get("mhsNgrKode").equalsIgnoreCase("NULL") || csvRecord.get("mhsNgrKode").isEmpty() ? null : csvRecord.get("mhsNgrKode"));
                    student.setScholarshipName(csvRecord.get("mhsBeasiswa").equalsIgnoreCase("NULL") || csvRecord.get("mhsBeasiswa").isEmpty() ? null : csvRecord.get("mhsBeasiswa"));
                    student.setTelephone(csvRecord.get("mhsNoTelp").equalsIgnoreCase("NULL") || csvRecord.get("mhsNoTelp").isEmpty() ? null : csvRecord.get("mhsNoTelp"));
                    student.setHp(csvRecord.get("mhsNoHp").equalsIgnoreCase("NULL") || csvRecord.get("mhsNoHp").isEmpty() ? null : csvRecord.get("mhsNoHp"));
                    student.setEmail(csvRecord.get("mhsEmail").equalsIgnoreCase("NULL") || csvRecord.get("mhsEmail").isEmpty() ? null : csvRecord.get("mhsEmail"));
                    student.setHobby(csvRecord.get("mhsHobi1").equalsIgnoreCase("NULL") || csvRecord.get("mhsHobi1").isEmpty() ? null : csvRecord.get("mhsHobi1"));
                    student.setParentMaritalStatus(csvRecord.get("mhsIdStatusOrtu").equalsIgnoreCase("NULL") || csvRecord.get("mhsIdStatusOrtu").isEmpty() ? null :csvRecord.get("mhsIdStatusOrtu"));
                    student.setAcademicLectures(new ArrayList<>());

                    int upYear = Integer.parseInt(csvRecord.get("mhsAngkatan"));
                    student.setEntryYear(csvRecord.get("mhsAngkatan") + "/" + (upYear += 1));
                    student.setPeriod(csvRecord.get("mhsSemesterMasuk").equalsIgnoreCase("1") ? "ganjil" : "genap");

                    List<Parent> parentList = new ArrayList<>();
                    Parent ayahParent = new Parent();
                    ayahParent.setName(csvRecord.get("mhsNamaAyah").equalsIgnoreCase("NULL") || csvRecord.get("mhsNamaAyah").isEmpty() ? null : csvRecord.get("mhsNamaAyah"));
                    ayahParent.setBirthDate(Objects.equals(csvRecord.get("mhsTanggalLahirAyah"), "NULL") || csvRecord.get("mhsTanggalLahirAyah").equalsIgnoreCase("0000-00-00") || csvRecord.get("mhsTanggalLahirAyah").isEmpty() ? null : LocalDate.parse(csvRecord.get("mhsTanggalLahirAyah"), sdf));
                    ayahParent.setEducation(csvRecord.get("mhsPndrIdTerakhirAyah").equalsIgnoreCase("NULL") || csvRecord.get("mhsPndrIdTerakhirAyah").isEmpty() ? null : csvRecord.get("mhsPndrIdTerakhirAyah"));
                    ayahParent.setTelephone(csvRecord.get("mhsNoTelpOrangTua").equalsIgnoreCase("NULL") || csvRecord.get("mhsNoTelpOrangTua").isEmpty() ? null : csvRecord.get("mhsNoTelpOrangTua"));
                    ayahParent.setEmail(csvRecord.get("mhsEmailOrangTua").equalsIgnoreCase("NULL") || csvRecord.get("mhsEmailOrangTua").isEmpty() ? null : csvRecord.get("mhsEmailOrangTua"));
                    ayahParent.setJob(csvRecord.get("mhsPkjrIdAyah").equalsIgnoreCase("NULL") || csvRecord.get("mhsPkjrIdAyah").isEmpty() ? null : csvRecord.get("mhsPkjrIdAyah"));
                    ayahParent.setAddress(csvRecord.get("mhsAlamatOrangTua").equalsIgnoreCase("NULL") || csvRecord.get("mhsAlamatOrangTua").isEmpty() ? null : csvRecord.get("mhsAlamatOrangTua"));
                    ayahParent.setPostCode(csvRecord.get("mhsKotaKodeOrangTua").equalsIgnoreCase("NULL") || csvRecord.get("mhsKotaKodeOrangTua").isEmpty() ? null : csvRecord.get("mhsKotaKodeOrangTua"));
                    ayahParent.setIncome(csvRecord.get("mhsPhslrId").equalsIgnoreCase("NULL") || csvRecord.get("mhsPhslrId").isEmpty() ? null : csvRecord.get("mhsPhslrId"));
                    ayahParent.setType("ayah");
                    parentList.add(ayahParent);

                    Parent ibuParent = new Parent();
                    ibuParent.setName(csvRecord.get("mhsNamaIbu").equalsIgnoreCase("NULL") || csvRecord.get("mhsNamaIbu").isEmpty() ? null : csvRecord.get("mhsNamaIbu"));
                    ibuParent.setBirthDate(Objects.equals(csvRecord.get("mhsTanggalLahirIbu"), "NULL") || csvRecord.get("mhsTanggalLahirIbu").isEmpty() || csvRecord.get("mhsTanggalLahirIbu").equalsIgnoreCase("0000-00-00") ? null : LocalDate.parse(csvRecord.get("mhsTanggalLahirIbu"), sdf));
                    ibuParent.setEducation(csvRecord.get("mhsPndrIdTerakhirIbu").equalsIgnoreCase("NULL") || csvRecord.get("mhsPndrIdTerakhirIbu").isEmpty() ? null : csvRecord.get("mhsPndrIdTerakhirIbu"));
                    ibuParent.setTelephone(csvRecord.get("mhsNoTelpIbu").equalsIgnoreCase("NULL") || csvRecord.get("mhsNoTelpIbu").isEmpty() ? null : csvRecord.get("mhsNoTelpIbu"));
                    ibuParent.setJob(csvRecord.get("mhsPkjrIdIbu").equalsIgnoreCase("NULL") || csvRecord.get("mhsPkjrIdIbu").isEmpty() ? null : csvRecord.get("mhsPkjrIdIbu"));
                    ibuParent.setAddress(csvRecord.get("mhsAlamatIbu").equalsIgnoreCase("NULL") || csvRecord.get("mhsAlamatIbu").isEmpty() ? null : csvRecord.get("mhsAlamatIbu"));
                    ibuParent.setPostCode(csvRecord.get("mhsKodePosIbu").equalsIgnoreCase("NULL") || csvRecord.get("mhsKodePosIbu").isEmpty() ? null : csvRecord.get("mhsKodePosIbu"));
                    ibuParent.setType("ibu");
                    parentList.add(ibuParent);

                    Parent waliParent = new Parent();
                    waliParent.setName(csvRecord.get("mhsNamaWali").equalsIgnoreCase("NULL") || csvRecord.get("mhsNamaWali").isEmpty() ? null : csvRecord.get("mhsNamaWali"));
                    waliParent.setBirthDate(Objects.equals(csvRecord.get("mhsTanggalLahirWali"), "NULL") || csvRecord.get("mhsTanggalLahirWali").isEmpty() || csvRecord.get("mhsTanggalLahirWali").equalsIgnoreCase("0000-00-00") ? null : LocalDate.parse(csvRecord.get("mhsTanggalLahirWali"), sdf));
                    waliParent.setEducation(csvRecord.get("mhsPndrIdTerakhirWali").equalsIgnoreCase("NULL") || csvRecord.get("mhsPndrIdTerakhirWali").isEmpty() ? null : csvRecord.get("mhsPndrIdTerakhirWali"));
                    waliParent.setTelephone(csvRecord.get("mhsNoTelpWali").equalsIgnoreCase("NULL") || csvRecord.get("mhsNoTelpWali").isEmpty() ? null : csvRecord.get("mhsNoTelpWali"));
                    waliParent.setEmail(csvRecord.get("mhsEmailWali").equalsIgnoreCase("NULL") || csvRecord.get("mhsEmailWali").isEmpty() ? null : csvRecord.get("mhsEmailWali"));
                    waliParent.setJob(csvRecord.get("mhsPkjrIdWali").equalsIgnoreCase("NULL") || csvRecord.get("mhsPkjrIdWali").isEmpty() ? null : csvRecord.get("mhsPkjrIdWali"));
                    waliParent.setAddress(csvRecord.get("mhsAlamatWali").equalsIgnoreCase("NULL") || csvRecord.get("mhsAlamatWali").isEmpty() ? null : csvRecord.get("mhsAlamatWali"));
                    waliParent.setPostCode(csvRecord.get("mhsKodePosWali").equalsIgnoreCase("NULL") || csvRecord.get("mhsKodePosWali").isEmpty() ? null : csvRecord.get("mhsKodePosWali"));
                    waliParent.setType("wali");
                    parentList.add(waliParent);

                    student.setParents(parentList);

                    HashMap<String, Object> education = new HashMap<>();
                    education.put("asal_sekolah", csvRecord.get("mhsSmtaKode").equalsIgnoreCase("NULL") || csvRecord.get("mhsSmtaKode").isEmpty() ? null : csvRecord.get("mhsSmtaKode"));
                    education.put("nisn", csvRecord.get("mhsnisn").equalsIgnoreCase("NULL") || csvRecord.get("mhsnisn").isEmpty() ? null : csvRecord.get("mhsnisn"));
                    education.put("npsn", csvRecord.get("mhsnpsn").equalsIgnoreCase("NULL") || csvRecord.get("mhsnpsn").isEmpty() ? null : csvRecord.get("mhsnpsn"));
                    education.put("jurusan", csvRecord.get("mhsJursmtarKode").equalsIgnoreCase("NULL") || csvRecord.get("mhsJursmtarKode").isEmpty() ? null : csvRecord.get("mhsJursmtarKode"));
                    education.put("alamat_sekolah", csvRecord.get("mhsAlamatSmta").equalsIgnoreCase("NULL") || csvRecord.get("mhsAlamatSmta").isEmpty() ? null : csvRecord.get("mhsAlamatSmta"));
                    education.put("nomor_ijazah", csvRecord.get("mhsNoIjasahSmta").equalsIgnoreCase("NULL") || csvRecord.get("mhsNoIjasahSmta").isEmpty() ? null : csvRecord.get("mhsNoIjasahSmta"));
                    education.put("tanggal_ijazah", csvRecord.get("mhsTanggalIjasahSmta").equalsIgnoreCase("NULL") || csvRecord.get("mhsTanggalIjasahSmta").isEmpty() ? null : csvRecord.get("mhsTanggalIjasahSmta"));
                    education.put("tanggal_lulus", csvRecord.get("mhsTahunLulusSmta").equalsIgnoreCase("NULL") || csvRecord.get("mhsTahunLulusSmta").isEmpty() ? null : csvRecord.get("mhsTahunLulusSmta"));
                    education.put("nilai_ujian_akhir", csvRecord.get("mhsNilaiUjianAkhirSmta").equalsIgnoreCase("NULL") || csvRecord.get("mhsNilaiUjianAkhirSmta").isEmpty() ? null : csvRecord.get("mhsNilaiUjianAkhirSmta"));
                    education.put("jumlah_mata_pelajaran", csvRecord.get("mhsJumlahMpUjianAkhirSmta").equalsIgnoreCase("NULL") || csvRecord.get("mhsJumlahMpUjianAkhirSmta").isEmpty() ? null : csvRecord.get("mhsJumlahMpUjianAkhirSmta"));
                    student.setEducation(education);

                    List<Address> addressList = new ArrayList<>();
                    Address addressAsal = new Address();
                    addressAsal.setStreetName(csvRecord.get("mhsAlamatLuar").equalsIgnoreCase("NULL") || csvRecord.get("mhsAlamatLuar").isEmpty() ? null : csvRecord.get("mhsAlamatLuar"));
//                    addressAsal.setCityId(csvRecord.get("mhsKotaLuar").equalsIgnoreCase("NULL") || csvRecord.get("mhsKotaLuar").isEmpty() ? null : csvRecord.get("mhsKotaLuar"));  //abaikan ?
                    addressAsal.setPostCode(csvRecord.get("mhsKodePosLuar").equalsIgnoreCase("NULL") || csvRecord.get("mhsKodePosLuar").isEmpty() ? null : csvRecord.get("mhsKodePosLuar"));
                    addressAsal.setAddressType("asal");
                    addressList.add(addressAsal);

                    Address addressDomisili = new Address();
                    addressDomisili.setStreetName(csvRecord.get("mhsAlamatMhs").equalsIgnoreCase("NULL") || csvRecord.get("mhsAlamatMhs").isEmpty() ? null : csvRecord.get("mhsAlamatMhs"));
                    addressDomisili.setCityId(csvRecord.get("mhsKotaKode").equalsIgnoreCase("NULL") || csvRecord.get("mhsKotaKode").isEmpty() ? null : csvRecord.get("mhsKotaKode"));
                    addressDomisili.setPostCode(csvRecord.get("mhsKodePos").equalsIgnoreCase("NULL") || csvRecord.get("mhsKodePos").isEmpty() ? null : csvRecord.get("mhsKodePos"));
                    addressDomisili.setAddressType("domisili");
                    addressList.add(addressDomisili);

                    student.setAddress(addressList);
                    StudentStatus studentStatus = statusService.saveStatusUnsaved(csvRecord.get("mhsNiu"),"62e69f7d12ed34573949177b","9880/UN5.1.R1/SPB/2022");

                    datalist.add(student);
                    dataStatus.add(studentStatus);
                    System.out.println("Selesai");
                }

//            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        bucketList = findDuplicateInStream(datalist.stream(),datalist);
        bucketListStatus = findDuplicateInStreamStatus(dataStatus.stream(),dataStatus);
        logger.info("bucketList size: "+bucketList.size());
        logger.info("bucketList: "+bucketList);

        logger.info("List size before Remove : "+datalist.size());
        bucketList.forEach(datalist::remove);
        bucketListStatus.forEach(dataStatus::remove);
        logger.info("List size after remove: "+datalist.size());

        HashMap<String,Object> data = new HashMap<>();
        data.put("amount_saved",datalist.size());
        data.put("duplicate_data",bucketList);

        studentRepository.saveAll(datalist);
        statusRepository.saveAll(dataStatus);
        return data;
    }

    public static List<Student> findDuplicateInStream(Stream<Student> stream, List<Student> dataList)
    {
        Set<String> items = new HashSet<>();
        List<Student> bucketList = new ArrayList<>();
        Set<Student> duplicateList = stream.filter(n -> !items.add(n.getNim())).collect(Collectors.toSet());
        duplicateList.forEach((dl) -> {
            dataList.stream().filter(s -> {
                if (s.getNim().equalsIgnoreCase(dl.getNim())) {
                    bucketList.add(s);
                    return true;
                }else return false;
            }).collect(Collectors.toList());

        });
        // Return the set of duplicate elements
        return bucketList;
    }

    public static List<StudentStatus> findDuplicateInStreamStatus(Stream<StudentStatus> stream, List<StudentStatus> dataList)
    {
        Set<String> items = new HashSet<>();
        List<StudentStatus> bucketList = new ArrayList<>();
        Set<StudentStatus> duplicateList = stream.filter(n -> !items.add(n.getNim())).collect(Collectors.toSet());
        duplicateList.forEach((dl) -> {
            dataList.stream().filter(s -> {
                if (s.getNim().equalsIgnoreCase(dl.getNim())) {
                    bucketList.add(s);
                    return true;
                }else return false;
            }).collect(Collectors.toList());

        });
        // Return the set of duplicate elements
        return bucketList;
    }

    public List<Billing> h2hSaveToBilling(String filename) throws FileNotFoundException {
        Path fileDir = Paths.get("files");
        String dirLocation = fileDir.toFile().getAbsolutePath();

        File initialFile = new File(dirLocation+"/migration/billing/h2h/"+filename);
        InputStream targetStream = new FileInputStream(initialFile);

        List<Billing> datalist = new ArrayList<>();
        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(targetStream, StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withDelimiter(';').withQuote('"'))){
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                System.out.println("Memproses Data milik :"+csvRecord.get("NAMA"));

                String billingNumber = csvRecord.get("Nomor_Tagihan");
                String nim = csvRecord.get("NIM");
                String name = csvRecord.get("NAMA");
                String majorCode = csvRecord.get("Kode_Major");
                String academicYear = csvRecord.get("Tahun_Akademik");
                String totalItem = csvRecord.get("Total_Item");
                String strDateTimePayment = csvRecord.get("Tanggal_Pembayaran");
                String namaBank = csvRecord.get("Nama_Bank");
                String totalAmount = csvRecord.get("Total_Amount");
                String strEditTime = csvRecord.get("editTime");

                HashMap<String,Object> majorData = new HashMap<>();
                JSONArray majorlist = new JSONArray(Constant.majorList());
                for(int i=0;i<majorlist.length();i++){
                    JSONObject dataMajor = majorlist.getJSONObject(i);
                    if(dataMajor.get("major_code").toString().equalsIgnoreCase(majorCode)){
                        dataMajor.keySet().forEach(key -> {
                            if(dataMajor.isNull(key)){
                                majorData.put(key, null);
                            }else{
                                majorData.put(key,dataMajor.get(key));
                            }
                        });
                    }
                }

                String educationLevel = majorData.get("education_level") == null ? null : majorData.get("education_level").toString();

                LocalDateTime dateTimePayment = null;
                if(!strDateTimePayment.equalsIgnoreCase("NULL")){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    dateTimePayment = LocalDateTime.parse(strDateTimePayment, formatter);
                }

                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime editTime = LocalDateTime.parse(strEditTime, formatter2);

                String kodeBank = null;
                if(!namaBank.equalsIgnoreCase("NULL")){
                    if(namaBank.equalsIgnoreCase("Bank Sumut")){
                        kodeBank = "SUMUT";
                    }else{
                        kodeBank = namaBank;
                    }
                }

                Billing billing = new Billing();
                billing.setNim(nim);
                billing.setDatetimeCreated(null);
                billing.setDatetimeExpired(null);
                billing.setPeriodId(academicYear);
                billing.setPaymentNtb(null);
                billing.setBankCode(kodeBank);
                billing.setTrxId(billingNumber);
                billing.setVirtualAccount(null);
                billing.setTrxAmount(Decimal128.parse(totalAmount));
                billing.setDatetimePayment(dateTimePayment == null ? editTime : dateTimePayment);
                billing.setPaymentAmount(Decimal128.parse(totalAmount));
                billing.setPaymentType("H2H");
                billing.setUpdatedAt(editTime);
                billing.setPaid(true);

                String year = academicYear.substring(0,4);
                String semester = academicYear.substring(4,5);

                List<BillingItem> itemData = new ArrayList<>();
                for (int i=1; i<=Integer.parseInt(totalItem); i++){
                    String item = csvRecord.get("Item"+i);
                    String itemName;
                    if(item.contains("SPP")){
                        itemName = item+" "+educationLevel+" "+year+" "+semester+" "+name+" "+nim;
                    }else{
                        itemName = item+" "+year+" "+semester+" "+name+" "+nim;
                    }

                    if(!item.equalsIgnoreCase("NULL")) {
                        BillingItem billingItem = new BillingItem();
                        billingItem.setAmount(Decimal128.parse(csvRecord.get("Amount" + i)));
                        billingItem.setName(itemName);

                        if(educationLevel == null){
                            billingItem.setFeeTypeSlug("spp-s1");
                        } else {
                            boolean isSpp = item.contains("SPP") || item.contains("spp") || item.contains("Spp") || item.equalsIgnoreCase("SPP");

                            if (isSpp && educationLevel.equalsIgnoreCase("S-1")) {
                                billingItem.setFeeTypeSlug("spp-s1");
                            } else if (isSpp && (educationLevel.equalsIgnoreCase("D-3") || educationLevel.equalsIgnoreCase("D-III"))){
                                billingItem.setFeeTypeSlug("spp-d3");
                            } else if (isSpp && educationLevel.equalsIgnoreCase("S-2")) {
                                billingItem.setFeeTypeSlug("spp-s2");
                            } else if (isSpp && educationLevel.equalsIgnoreCase("S-3")) {
                                billingItem.setFeeTypeSlug("spp-s3");
                            } else if (isSpp && educationLevel.equalsIgnoreCase("PR")) {
                                billingItem.setFeeTypeSlug("spp-profesi");
                            } else if (isSpp && educationLevel.equalsIgnoreCase("Sp-1")) {
                                billingItem.setFeeTypeSlug("spp-spesialis");
                            } else if (item.equalsIgnoreCase("Registrasi") || item.equalsIgnoreCase("Registrasi Ulang") || item.equalsIgnoreCase("Daftar Ulang") || item.equalsIgnoreCase("Registrasi Semester")) {
                                billingItem.setFeeTypeSlug("registrasi-ulang");
                            } else if (item.equalsIgnoreCase("PKA")) {
                                billingItem.setFeeTypeSlug("pka");
                            } else if (item.equalsIgnoreCase("Skripsi")) {
                                billingItem.setFeeTypeSlug("skripsi");
                            } else if (item.equalsIgnoreCase("Thesis") || item.equalsIgnoreCase("Tesis")) {
                                billingItem.setFeeTypeSlug("thesis");
                            } else if (item.equalsIgnoreCase("Disertasi")) {
                                billingItem.setFeeTypeSlug("disertasi");
                            } else if (item.equalsIgnoreCase("DKA")) {
                                billingItem.setFeeTypeSlug("dka");
                            } else if (item.equalsIgnoreCase("DKM")) {
                                billingItem.setFeeTypeSlug("dkm");
                            } else if (item.equalsIgnoreCase("Matrikulasi") || item.equalsIgnoreCase("Marikulasi") || item.equalsIgnoreCase("Martikulasi")) {
                                billingItem.setFeeTypeSlug("matrikulasi");
                            } else if (item.equalsIgnoreCase("Sumpah Profesi")) {
                                billingItem.setFeeTypeSlug("sumpah-profesi");
                            } else if (item.equalsIgnoreCase("ICT")) {
                                billingItem.setFeeTypeSlug("ict");
                            } else if (item.equalsIgnoreCase("Perpus") || item.equalsIgnoreCase("Perpustakaan")) {
                                billingItem.setFeeTypeSlug("perpustakaan");
                            } else if (item.equalsIgnoreCase("Ujian Komprehensif")) {
                                billingItem.setFeeTypeSlug("ujian-komprehensif");
                            } else if (item.equalsIgnoreCase("PKL")) {
                                billingItem.setFeeTypeSlug("pkl");
                            } else if (item.equalsIgnoreCase("Praktikum")) {
                                billingItem.setFeeTypeSlug("praktikum");
                            } else if (item.equalsIgnoreCase("Daftar Masuk")) {
                                billingItem.setFeeTypeSlug("daftar-masuk");
                            } else if (item.equalsIgnoreCase("Pendaftaran") || item.equalsIgnoreCase("PENDAFTARARAN")) {
                                billingItem.setFeeTypeSlug("pendaftaran");
                            } else if (item.equalsIgnoreCase("WISUDA")) {
                                billingItem.setFeeTypeSlug("wisuda");
                            } else if (item.equalsIgnoreCase("Uji ketrampilan") || item.equalsIgnoreCase("Uji Keterampilan")) {
                                billingItem.setFeeTypeSlug("uji-keterampilan");
                            } else if (item.equalsIgnoreCase("Sumbangan")) {
                                billingItem.setFeeTypeSlug("sumbangan-spesialis");
                            } else if (item.equalsIgnoreCase("Latihan Dasar Profesi")) {
                                billingItem.setFeeTypeSlug("latihan-dasar-profesi");
                            } else if (item.equalsIgnoreCase("Biaya Lain")) {
                                billingItem.setFeeTypeSlug("biaya-lain");
                            } else if (item.equalsIgnoreCase("DPA")) {
                                billingItem.setFeeTypeSlug("dpa");
                            }else if(item.contains("Pelatihan Kompetensi")){
                                billingItem.setFeeTypeSlug("pelatihan-kompetensi");
                            } else if (item.equalsIgnoreCase("PRAKTIKUM") || item.equalsIgnoreCase("Biaya PRAKTIKUM")) {
                                billingItem.setFeeTypeSlug("praktikum");
                            } else if (item.equalsIgnoreCase("-") || item.equalsIgnoreCase("") || item.equalsIgnoreCase("Test")) {
                                billingItem.setFeeTypeSlug("-");
                            } else {
                                throw new IllegalArgumentException("Fee type not found : "+item);
                            }
                        }

                        itemData.add(billingItem);
                    }
                }

                billing.setBillingItems(itemData);
                datalist.add(billing);

                System.out.println("Selesai");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        billingRepository.saveAll(datalist);
        return datalist;
    }

    public List<Billing> tagihanSaveToBilling(String filename) throws FileNotFoundException {
        Path fileDir = Paths.get("files");
        String dirLocation = fileDir.toFile().getAbsolutePath();

        File initialFile = new File(dirLocation+"/migration/billing/tagihan/"+filename);
        InputStream targetStream = new FileInputStream(initialFile);

        List<Billing> datalist = new ArrayList<>();
        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(targetStream, StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withDelimiter(';').withQuote('"'))){
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                System.out.println("Memproses Data milik :"+csvRecord.get("nim").split(" ")[0]);

                String nim = csvRecord.get("nim").split(" ")[0];
                String bankCode = csvRecord.get("bank_code");
                String virtualAccount = csvRecord.get("virtual_account");
                String billingNumber = csvRecord.get("transaction_id");
                String totalAmount = csvRecord.get("total_amount");
                String paymentNtb = csvRecord.get("payment_ntb");
                String strDateTimeCreated = csvRecord.get("datetime_created");
                String strDateTimePayment = csvRecord.get("datetime_payment");
                String totalItem = csvRecord.get("total_items");
                String periodId = csvRecord.get("period_id");
                String uktInfo = csvRecord.get("ukt_info");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTimeCreated = LocalDateTime.parse(strDateTimeCreated, formatter);
                LocalDateTime dateTimePayment = LocalDateTime.parse(strDateTimePayment, formatter);

                Billing billing = new Billing();
                billing.setNim(nim);
                billing.setDatetimeCreated(dateTimeCreated);
                billing.setDatetimeExpired(null);
                billing.setPeriodId(periodId);
                billing.setPaymentNtb(paymentNtb);
                billing.setBankCode(bankCode);
                billing.setTrxId(billingNumber);
                billing.setVirtualAccount(virtualAccount);
                billing.setTrxAmount(Decimal128.parse(totalAmount));
                billing.setDatetimePayment(dateTimePayment);
                billing.setPaymentAmount(Decimal128.parse(totalAmount));
                billing.setPaymentType("VA");
                billing.setUktLevel(uktInfo);
                billing.setPaid(true);

                List<BillingItem> itemData = new ArrayList<>();
                for (int i=1; i<=Integer.parseInt(totalItem); i++){
                    String item = csvRecord.get("item_name_"+i);
                    String codeRef = csvRecord.get("code_ref_"+i);

                    if(!item.equalsIgnoreCase("NULL")) {
                        BillingItem billingItem = new BillingItem();
                        billingItem.setAmount(Decimal128.parse(csvRecord.get("item_price_" + i)));
                        billingItem.setName(item);

                        boolean isSpp = item.contains("SPP") || item.contains("spp") || item.equalsIgnoreCase("SPP");

                        if (isSpp && (item.contains("S-1") || item.contains("SNMPTN") || item.contains("SBMPTN") || item.contains("SMM") || item.contains("PSDKU") || item.contains("Internasional")) || item.contains("SBPU") || item.endsWith("-")) {
                            billingItem.setFeeTypeSlug("spp-s1");
                        } else if (isSpp && (item.contains("D-3") || item.contains("SPMPD"))) {
                            billingItem.setFeeTypeSlug("spp-d3");
                        } else if (isSpp && (item.contains("S-2") || item.contains("Pascasarjana"))) {
                            billingItem.setFeeTypeSlug("spp-s2");
                        } else if (isSpp && (item.contains("S-3") || item.contains("Doktoral"))) {
                            billingItem.setFeeTypeSlug("spp-s3");
                        } else if (isSpp && (item.contains("Profesi") || item.contains("PROFESI") || item.contains(" PR "))) {
                            billingItem.setFeeTypeSlug("spp-profesi");
                        } else if (isSpp && (item.contains("SPESIALIS") || item.contains("spesialis") || item.contains("PPDS") || item.contains("PPDGS") || item.contains(" Sp-1 "))) {
                            billingItem.setFeeTypeSlug("spp-spesialis");
                        } else if (item.contains("Registrasi Ulang Internasional")) {
                            billingItem.setFeeTypeSlug("registrasi-ulang-internasional");
                        } else if (item.contains("Registrasi Ulang") || item.contains("Registrai Ulang") || item.contains("REGISTRASI ULANG") || item.contains("REGISTRASI") || item.contains("Registrasi")) {
                            billingItem.setFeeTypeSlug("registrasi-ulang");
                        } else if (item.contains("PKA")) {
                            billingItem.setFeeTypeSlug("pka");
                        } else if (item.contains("Skripsi")) {
                            billingItem.setFeeTypeSlug("skripsi");
                        } else if (item.contains("Thesis") || item.contains("Tesis")) {
                            billingItem.setFeeTypeSlug("thesis");
                        } else if (item.contains("Disertasi")) {
                            billingItem.setFeeTypeSlug("disertasi");
                        } else if (item.contains("DKA")) {
                            billingItem.setFeeTypeSlug("dka");
                        } else if (item.contains("Matrikulasi") || item.contains("MATRIKULASI")) {
                            billingItem.setFeeTypeSlug("matrikulasi");
                        } else if (item.contains("Sumpah Profesi") || item.contains("SUMPAH PROFESI")) {
                            billingItem.setFeeTypeSlug("sumpah-profesi");
                        } else if (item.contains("Pendaftaran") || item.contains("PENDAFTARAN")) {
                            billingItem.setFeeTypeSlug("pendaftaran");
                        } else if (item.contains("Sumbangan Spesialis") || item.contains("SUMBANGAN SPESIALIS") || item.contains("Sumbangan")) {
                            billingItem.setFeeTypeSlug("sumbangan-spesialis");
                        } else if (item.contains("MMPI")) {
                            billingItem.setFeeTypeSlug("mmpi");
                        } else if (item.contains("Latihan Dasar Profesi") || item.contains("LATIHAN DASAR PROFESI")) {
                            billingItem.setFeeTypeSlug("latihan-dasar-profesi");
                        }else if(item.contains("Pelatihan Kompetensi") || item.contains("PELATIHAN KOMPETENSI")){
                            billingItem.setFeeTypeSlug("pelatihan-kompetensi");
                        } else {
                            String nimInItem;
                            Pattern itemNim = Pattern.compile("([0-9]{9})");
                            Matcher match = itemNim.matcher(item);
                            if (match.find()) {
                                nimInItem = match.group(0);
                                if(nimInItem.startsWith("21")){
                                    billingItem.setFeeTypeSlug("spp-s1");
                                }
                            }else{
                                throw new IllegalArgumentException("Fee type not found : "+item);
                            }
                        }

                        itemData.add(billingItem);
                    }
                }

                billing.setBillingItems(itemData);

                datalist.add(billing);
                System.out.println("Selesai");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        billingRepository.saveAll(datalist);
        return datalist;
    }
}
