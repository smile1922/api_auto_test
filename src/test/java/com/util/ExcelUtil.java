package com.util;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.pojo.CaseData;
import com.sun.xml.bind.v2.schemagen.xmlschema.Import;

import java.io.File;
import java.util.List;

public class ExcelUtil {
    public static final String EXCEL_FILE_PATH = "src\\test\\resources\\caseData.xlsx";

    /**
     * 读取外部Excel文件中的数据
     * @param sheetNum sheet的编号（从0开始）
     * @return 读取到的数据
     */
    public static List<CaseData> readExcel(int sheetNum){
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNum);
        //读取的文件src\test\resources\caseData.xlsx
        List<CaseData> datas = ExcelImportUtil.importExcel(new File(EXCEL_FILE_PATH),
                CaseData.class,importParams);

        return datas;
    }
}
