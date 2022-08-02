package com.usu.satu.template;

public class StudyCardResultTemplate {
    public static final String TEMPLATE = """
        <!DOCTYPE html>
            <head>
                <style>
                    body {
                        font-family: sans-serif;
                        font-size: 10pt;
                    }
                    table {
                        width:100%;
                    }
                    .table-course {
                        border: 1px solid;
                        border-collapse:collapse;
                    }
                    .text-right {
                        text-align: right;
                    }
                    .text-bold {
                        font-weight: 800;
                    }
                </style>
            </head>
            <body>
                <table>
                    <tr>
                        <td rowspan="2" style="width: 80px">
                            <img src={logo_usu} alt="" style="width: 60px;">
                        </td>
                        <td class="text-bold" style="font-size: 18pt; margin-bottom: -20px;">
                            Universitas Sumatera Utara
                        </td>
                    </tr>
                    <tr>
                        <td class="text-bold" style="font-size: 11pt;">
                            {faculty_name}
                        </td>
                    </tr>
                </table>
            
                <br>
            
                <div class="text-bold" style="text-align: center; font-size: 14pt;">
                    KARTU HASIL STUDI (KHS) MAHASISWA
                </div>
                <div class="text-bold" style="text-align: center; font-size: 11pt;">
                    Semester : {period_name}
                </div>
            
                <br>
            
                <table class="text-bold">
                    <tr>
                        <td style="padding: 2px 0; width: 160px">Nama</td>
                        <td style="width: 20px;">:</td>
                        <td style="width: auto;">{student_name}</td>
                    </tr>
                    <tr>
                        <td style="padding: 2px 0;">NIM</td>
                        <td>:</td>
                        <td>{student_nim}</td>
                    </tr>
                    <tr>
                        <td style="padding: 2px 0;">Angkatan</td>
                        <td>:</td>
                        <td>{student_entry_year}</td>
                    </tr>
                    <tr>
                        <td style="padding: 2px 0;">Program Studi</td>
                        <td>:</td>
                        <td>{study_program_name}</td>
                    </tr>
                    <tr>
                        <td style="padding: 2px 0;">Dosen PA</td>
                        <td>:</td>
                        <td>{lecture_PA_name}</td>
                    </tr>
                </table>
            
                <br>
            
                <table class="table-course" style="text-align: center;">
                    <tr class="table-course text-bold">
                         <td rowspan="2" class="table-course" width="40px">No.</td>
                         <td colspan="2" class="table-course" width="auto">Mata Kuliah</td>
                         <td rowspan="2" class="table-course" width="50px">SKS</td>
                         <td rowspan="2" class="table-course" width="50px">Ke</td>
                         <td rowspan="2" class="table-course" width="50px">Nilai</td>
                         <td rowspan="2" class="table-course" width="50px">Bobot</td>
                         <td rowspan="2" class="table-course" width="70px">Nilai SKS</td>
                     </tr>
            
                    <tr class="table-course text-bold" style="text-align: center;">
                        <td class="table-course" width="auto">Kode</td>
                        <td class="table-course" width="auto">Nama</td>
                    </tr>
                   
                    {table_content_list}
                   
                    <tr class="table-course text-right text-bold">
                        <td colspan="3" class="table-course">Jumlah</td>
                        <td class="table-course">{credit_total}</td>
                        <td colspan="4" class="table-course">{credit_grade}</td>
                    </tr>
                </table>
            
                <br>
            
                <table class="text-bold">
                    <tr>
                        <td style="padding: 2px 0; width: 230px">IP Semester (IPS)</td>
                        <td>:</td>
                        <td>{ip_now}</td>
                    </tr>
                    <tr>
                        <td style="padding: 2px 0; width: 160px">IP Kumulatif (IPK)</td>
                        <td>:</td>
                        <td>{ip_kumulatif}</td>
                    </tr>
                    <tr>
                        <td>Maks. Beban sks semester berikutnya</td>
                        <td>:</td>
                        <td>{credit_next_maks}</td>
                    </tr>
                </table>
            
                <br><br>
            
                <table style="text-align: center;">
                    <tr>
                        <td width="76%"></td>
                        <td width="24%">Medan, {study_card_date}</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>...................</td>
                    </tr>
                    <tr style="vertical-align: bottom">
                        <td></td>
                        <td height="100px" style="border-bottom: 1px solid; padding-bottom: 4px;">..........................</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>NIP. ...................</td>
                    </tr>
                </table>
            </body>
        </html>
    """;
}
