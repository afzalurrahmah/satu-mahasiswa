package com.usu.satu.template;

public class StudyCardPlanTemplate {

    public static final String TEMPLATE = """
            <!DOCTYPE html>
                <head>
                    <style>
                        body {
                            font-family: 'Open Sans', sans-serif;
                            font-size: 10pt;
                        }
                        table {
                            width:100%;
                        }
                        .table-course {
                            border: 1px solid;
                            border-collapse:collapse;
                        }
                    </style>
                </head>
                <body>
                    <table>
                        <tr>
                            <td rowspan="2" style="width: 70px">
                                <img src={logo_usu} alt="" style="width: 60px;">
                            </td>
                            <td style="font-size: 18pt; font-weight: 600; margin-bottom: -20px;">
                                Universitas Sumatera Utara
                            </td>
                        </tr>
                        <tr>
                            <td style="font-size: 12pt; font-weight: 800;">
                                {faculty_name}
                            </td>
                        </tr>
                    </table>

                    <br>

                    <div style="text-align: center; font-weight: 800; font-size: 14pt;">
                        KARTU RENCANA STUDI (KRS) MAHASISWA
                    </div>
                    <div style="text-align: center; font-weight: 600; font-size: 11pt;">
                        Semester : {period_name}
                    </div>

                    <br>

                    <table>
                        <tr>
                            <td style="padding: 3px 3px 3px 0; width: 140px">Nama</td>
                            <td style="width: 20px;">:</td>
                            <td style="width: auto;">{student_name}</td>
                        </tr>
                        <tr>
                            <td style="padding: 3px 3px 3px 0;">NIM</td>
                            <td>:</td>
                            <td>{student_nim}</td>
                        </tr>
                        <tr>
                            <td style="padding: 3px 3px 3px 0;">Program Studi</td>
                            <td>:</td>
                            <td>{study_program_name} {degree}</td>
                        </tr>
                        <tr>
                            <td style="padding: 3px 3px 3px 0;">Dosen PA</td>
                            <td>:</td>
                            <td>{lecture_PA_name}</td>
                        </tr>
                    </table>

                    <br>

                    <table class="table-course">
                        <tr class="table-course" style="font-weight: bold; text-align: center;">
                            <td rowspan="2" class="table-course" width="50px">No.</td>
                            <td rowspan="2" class="table-course" width="150px">Kelas</td>
                            <td colspan="2" class="table-course" width="800px">Mata Kuliah</td>
                            <td rowspan="2" class="table-course" width="100px">SKS</td>
                        </tr>
                        <tr class="table-course" style="font-weight: bold; text-align: center;">
                            <td class="table-course" width="150px">Kode</td>
                            <td class="table-course" width="650px">Nama</td>
                        </tr>
                        
                        {table_content_list}
                        
                        <tr class="table-course">
                            <td colspan="3" class="table-course"><b>Total :</b></td>
                            <td></td>
                            <td class="table-course" style="text-align: center;"><b>{credit_total}</b></td>
                        </tr>
                    </table>

                    <br>

                    <table>
                        <tr>
                            <td style="padding: 3px 3px 3px 0; width: 140px">IP semester lalu</td>
                            <td width="20px">:</td>
                            <td>{ip_period_before}</td>
                        </tr>
                        <tr>
                            <td>Maks sks semester ini</td>
                            <td>:</td>
                            <td>{credit_maks}</td>
                        </tr>
                    </table>

                    <br><br>

                    <table style="text-align: center;">
                        <tr>
                            <td width="50%" rowspan="4"></td>
                            <td width="24%">Mengetahui</td>
                            <td></td>
                            <td width="24%">Medan, {study_card_date}</td>
                        </tr>
                        <tr>
                            <td>Wakil Dekan I</td>
                            <td></td>
                            <td>Mahasiswa</td>
                        </tr>
                        <tr style="vertical-align: bottom">
                            <td height="100px" style="border-bottom: 1px solid; padding-bottom: 4px;">...................................</td>
                            <td></td>
                            <td style="border-bottom: 1px solid; padding-bottom: 4px;">{student_name}</td>
                        </tr>
                        <tr>
                            <td>NIP. ............................</td>
                            <td></td>
                            <td>NIM. {student_nim}</td>
                        </tr>
                    </table>
                </body>
            </html>
        """;
}
