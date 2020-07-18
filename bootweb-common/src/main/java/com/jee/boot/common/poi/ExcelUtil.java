package com.jee.boot.common.poi;

import com.jee.boot.common.core.result.R;
import com.jee.boot.common.exception.base.BusinessException;
import com.jee.boot.common.poi.annotation.Excel;
import com.jee.boot.common.poi.annotation.Excel.ColumnType;
import com.jee.boot.common.poi.annotation.Excels;
import com.jee.boot.common.utils.DateUtils;
import com.jee.boot.common.utils.math.Arith;
import com.jee.boot.common.utils.reflect.ReflectUtils;
import com.jee.boot.common.utils.text.Convert;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jee.boot.common.poi.annotation.Excel.Type;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Excel相关处理
 *
 * @author jeeLearner
 * @version V1.0
 */
public class ExcelUtil<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 标题占的行数
     */
    private static final int titleRow = 3;

    /**
     * 工作表名称
     */
    private String sheetName;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Type type;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 导入导出数据列表
     */
    private List<T> list;

    /**
     * 注解列表
     */
    private List<Object[]> fields;

    /**
     * 实体对象
     */
    public Class<T> clazz;

    public ExcelUtil(Class<T> clazz){
        this.clazz = clazz;
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @param downloadPath 导出路径
     * @return 结果
     */
    public R exportExcel(List<T> list, String sheetName, String downloadPath){
        this.init(list, sheetName, Type.EXPORT);
        String filename = exportExcel(downloadPath);
        return R.ok().data("filename", filename);
    }


    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param sheetName 工作表的名称
     * @param downloadPath 导出路径
     * @return 结果
     */
    public R importTemplateExcel(String sheetName, String downloadPath) {
        this.init(null, sheetName, Type.IMPORT);
        String filename = exportExcel(downloadPath);
        return R.ok().data("filename", filename);
    }

    /**
     * 对excel表单默认第一个sheet转换成list
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importExcel(InputStream is) throws Exception {
        return importExcel(JeeStringUtils.EMPTY, is);
    }


    /**
     * Excel初始化数据
     *
     * @param list
     * @param sheetName
     * @param type
     */
    public void init(List<T> list, String sheetName, Type type){
        log.info("ExcelUtil.init  start...");
        if (list == null){
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        createExcelField();
        createWorkbook();
        log.info("ExcelUtil.init  end...");
    }


    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param sheetName 表格索引名
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importExcel(String sheetName, InputStream is) throws Exception{
        this.type = Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (JeeStringUtils.isNotEmpty(sheetName)){
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
        } else {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = wb.getSheetAt(0);
        }
        if (sheet == null){
            throw new IOException("文件sheet不存在");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if (rows > 0){
            // 定义一个map用于存放excel列的序号和field.
            // <单元格值, 列索引号>
            Map<String, Integer> cellMap = new HashMap<String, Integer>();
            // 获取表头
            Row heard = sheet.getRow(0 + titleRow);
            for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++){
                Cell cell = heard.getCell(i);
                if (JeeStringUtils.isNotNull(cell)){
                    String value = getCellValue(heard, i).toString();
                    cellMap.put(value, i);
                } else {
                    cellMap.put(null, i);
                }
            }
            // 有数据时才处理 得到类的所有field.
            Field[] allFields = clazz.getDeclaredFields();
            // 定义一个map用于存放列的序号和field.
            Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
            for (int col = 0; col < allFields.length; col++){
                Field field = allFields[col];
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Type.ALL || attr.type() == type)){
                    // 设置类的私有字段属性可访问.
                    field.setAccessible(true);
                    Integer column = cellMap.get(attr.name());
                    fieldsMap.put(column, field);
                }
            }
            for (int i = 1+titleRow; i < rows; i++) {
                // 从第5行开始取数据,默认前三行是title，第四行是表头.
                Row row = sheet.getRow(i);
                T entity = null;
                for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet()) {
                    Object val = getCellValue(row, entry.getKey());

                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = fieldsMap.get(entry.getKey());
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType) {
                        String s = Convert.toStr(val);
                        if (JeeStringUtils.endsWith(s, ".0")) {
                            val = JeeStringUtils.substringBefore(s, ".0");
                        } else {
                            String dateFormat = field.getAnnotation(Excel.class).dateFormat();
                            if (JeeStringUtils.isNotEmpty(dateFormat)) {
                                val = FastDateFormat.getInstance(dateFormat).format((Date) val);
                            } else {
                                val = Convert.toStr(val);
                            }
                        }
                    } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)){
                        val = Convert.toInt(val);
                    } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                        val = Convert.toLong(val);
                    } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                        val = Convert.toDouble(val);
                    } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                        val = Convert.toFloat(val);
                    } else if (BigDecimal.class == fieldType) {
                        val = Convert.toBigDecimal(val);
                    } else if (LocalDateTime.class == fieldType) {
                        if (val instanceof String) {
                            //Date
                            val = DateUtils.autoParseDate(val);
                        } else if (val instanceof Double) {
                            val = DateUtil.getJavaDate((Double) val);
                        } else if (val instanceof Date) {
                            val = (Date) val;
                        }
                    }
                    if (JeeStringUtils.isNotNull(fieldType)){
                        Excel attr = field.getAnnotation(Excel.class);
                        String propertyName = field.getName();
                        if (JeeStringUtils.isNotEmpty(attr.targetAttr())){
                            propertyName = field.getName() + "." + attr.targetAttr();
                        } else if (JeeStringUtils.isNotEmpty(attr.readConverterExp())) {
                            val = reverseByExp(String.valueOf(val), attr.readConverterExp());
                        }
                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }
                }
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * excel导出
     * @param downloadPath 下载路径
     * @return 文件名
     */
    private String exportExcel(String downloadPath) {
        log.info("ExcelUtil.exportExcel  start...");
        OutputStream out = null;
        try {
            // 取出一共有多少个sheet.
            double sheetNo = Math.ceil(Arith.div(list.size(), sheetSize, 10));
            if (list.size() == 0){
                sheetNo = 1.0;
            }
            for (int index = 0; index < sheetNo; index++){
                //创建工作表
                createSheet(sheetNo, index);
                //创建标题
                createTitle(fields.size()-1);
                //创建副标题
                createSubTitle(fields.size()-1);
                // 产生一行
                Row row = sheet.createRow(0+titleRow);
                int column = 0;
                // 列头写入各个字段的名称
                for (Object[] os : fields){
                    Excel excel = (Excel) os[1];
                    createHeaderCell(excel, row, column++);
                }
                if (Type.EXPORT.equals(type)){
                    fillExcelData(index, row);
                }
            }
            String filename = encodingFilename(sheetName);
            out = new FileOutputStream(getAbsoluteFile(downloadPath+filename));
            wb.write(out);
            log.info("ExcelUtil.exportExcel  end...");
            return filename;
        } catch (Exception e){
            log.error("导出Excel异常{}", e.getMessage());
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        } finally {
            IOUtils.closeQuietly(wb, out);
        }
    }

    /**
     * 获取下载路径
     *
     * @param filePath 文件路径
     */
    private String getAbsoluteFile(String filePath) {
        File desc = new File(filePath);
        if (!desc.getParentFile().exists()){
            desc.getParentFile().mkdirs();
        }
        return filePath;
    }

    /**
     * 编码文件名
     */
    private String encodingFilename(String filename){
        //filename = filename + "_" + System.currentTimeMillis() + ".xlsx";
        filename = filename + "_" + UUID.randomUUID().toString() + ".xlsx";
        return filename;
    }

    /**
     * 创建标题
     * @param lastColumn 标题最后一列的索引
     */
    private void createTitle(int lastColumn) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        CellRangeAddress cellAddresses = new CellRangeAddress(0, 1, 0,lastColumn);
        sheet.addMergedRegion(cellAddresses);
        createReginStyles(cellAddresses);
        CellStyle titleStyle = styles.get("title");
        titleCell.setCellStyle(titleStyle);
        titleCell.setCellValue(sheetName + "报表");
    }

    /**
     * 创建副标题
     * @param lastColumn 标题最后一列的索引
     */
    private void createSubTitle(int lastColumn) {
        Row titleRow = sheet.createRow(2);
        Cell titleCell = titleRow.createCell(0);
        CellRangeAddress cellAddresses = new CellRangeAddress(2, 2, 0,lastColumn);
        sheet.addMergedRegion(cellAddresses);
        createReginStyles(cellAddresses);
        CellStyle titleStyle = styles.get("subtitle");
        titleCell.setCellStyle(titleStyle);
        titleCell.setCellValue("查询时间：" + DateUtils.formatDateTime(LocalDateTime.now()));
    }

    /**
     * 填充excel数据
     *
     * @param index 序号
     * @param row 单元格行
     */
    private void fillExcelData(int index, Row row) {
        int startNo = index * sheetSize;
        int endNo = Math.min(startNo + sheetSize, list.size());
        for (int i = startNo; i < endNo; i++){
            row = sheet.createRow(i + 1 + titleRow - startNo);
            // 得到导出对象.
            T vo = (T) list.get(i);
            int column = 0;
            for (Object[] os : fields){
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                // 设置实体类私有属性可访问
                field.setAccessible(true);
                //添加单元格并填充数据
                addCell(excel, row, vo, field, column++);
            }
        }

    }

    /**
     * 添加单元格并填充数据
     * @param attr Excel注解实体
     * @param row  行
     * @param vo  数据体
     * @param field 数据体字段
     * @param column  列号
     */
    private Cell addCell(Excel attr, Row row, T vo, Field field, int column) {
        Cell cell = null;
        try {
            // 设置行高
            row.setHeight((short) (attr.height() * 20));
            // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
            if (attr.isExportData()) {
                // 创建cell
                cell = row.createCell(column);
                cell.setCellStyle(styles.get("data"));

                // 用于读取对象中的属性
                Object value = getTargetValue(vo, field, attr);
                String dateFormat = attr.dateFormat();
                String readConverterExp = attr.readConverterExp();
                if (JeeStringUtils.isNotEmpty(dateFormat) && JeeStringUtils.isNotNull(value)){
                    cell.setCellValue(FastDateFormat.getInstance(dateFormat).format((Date) value));
                } else if (JeeStringUtils.isNotEmpty(readConverterExp) && JeeStringUtils.isNotNull(value)) {
                    cell.setCellValue(convertByExp(String.valueOf(value), readConverterExp));
                } else {
                    // 设置列类型
                    setCellVo(value, attr, cell);
                }
            }
        } catch (Exception ex) {
            log.error("导出Excel失败{}", ex.getMessage());
        }
        return cell;
    }

    /**
     * 设置单元格信息
     *
     * @param value 单元格值
     * @param attr 注解相关
     * @param cell 单元格信息
     */
    public void setCellVo(Object value, Excel attr, Cell cell){
        if (ColumnType.STRING == attr.cellType()) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(JeeStringUtils.isNull(value) ? attr.defaultValue() : value + attr.suffix());
        } else if (ColumnType.NUMERIC == attr.cellType()) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Integer.parseInt(value + ""));
        }
    }


    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @return 解析后值
     * @throws Exception
     */
    private String convertByExp(String propertyValue, String converterExp) {
        try {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource) {
                String[] itemArray = item.split("=");
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        }
        catch (Exception e){
            throw e;
        }
        return propertyValue;
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @return 解析后值
     * @throws Exception
     */
    private String reverseByExp(String propertyValue, String converterExp) {
        try {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource) {
                String[] itemArray = item.split("=");
                if (itemArray[1].equals(propertyValue)) {
                    return itemArray[0];
                }
            }
        }
        catch (Exception e){
            throw e;
        }
        return propertyValue;
    }

    /**
     * 获取bean中的属性值
     *
     * @param vo 实体对象
     * @param field 字段
     * @param excel 注解
     * @return 最终的属性值
     * @throws Exception
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception{
        Object o = field.get(vo);
        if (JeeStringUtils.isNotEmpty(excel.targetAttr())) {
            String target = excel.targetAttr();
            if (target.indexOf(".") > -1) {
                String[] targets = target.split("[.]");
                for (String name : targets) {
                    o = getValue(o, name);
                }
            } else {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * 以类的属性的get方法方法形式获取值
     *
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception {
        if (JeeStringUtils.isNotEmpty(name)){
            Class<?> clazz = o.getClass();
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method method = clazz.getMethod(methodName);
            o = method.invoke(o);
        }
        return o;
    }

    /**
     * 创建列头单元格
     */
    private Cell createHeaderCell(Excel attr, Row row, int column) {
        // 创建单元格
        Cell cell = row.createCell(column);
        // 写入列头信息
        cell.setCellValue(attr.name());
        //创建表格样式
        setDataValidation(attr, row, column);
        cell.setCellStyle(styles.get("header"));
        return cell;

    }

    /**
     * 创建表格样式
     */
    private void setDataValidation(Excel attr, Row row, int column) {
        if (attr.name().indexOf("注：") >= 0){
            sheet.setColumnWidth(column, 6000);
        }else{
            // 设置列宽
            sheet.setColumnWidth(column, (int) ((attr.width() + 0.72) * 256));
            row.setHeight((short) (attr.height() * 20));
        }
        // 如果设置了提示信息则鼠标放上去提示.
        if (JeeStringUtils.isNotEmpty(attr.prompt())) {
            // 这里默认设了2-101列提示.
            setXSSFPrompt(sheet, "", attr.prompt(), 1, 100, column, column);
        }
        // 如果设置了combo属性则本列只能选择不能输入
        if (attr.combo().length > 0){
            // 这里默认设了2-101列只能选择不能输入.
            setXSSFValidation(sheet, attr.combo(), 1, 100, column, column);
        }
    }


    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet 要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    public void setXSSFValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation){
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    /**
     * 设置 POI XSSFSheet 单元格提示
     *
     * @param sheet 表单
     * @param promptTitle 提示标题
     * @param promptContent 提示内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     */
    public void setXSSFPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
                              int firstCol, int endCol){
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        dataValidation.createPromptBox(promptTitle, promptContent);
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    /**
     * 创建工作表
     *
     * @param sheetNo sheet数量
     * @param index 序号
     */
    private void createSheet(double sheetNo, int index) {
        this.sheet = wb.createSheet();
        this.styles = createStyles(wb);
        // 设置工作表的名称.
        if (index == 0){
            wb.setSheetName(index, sheetName);
        } else {
            wb.setSheetName(index, sheetName + index);
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        // 写入各条记录,每条记录对应excel表中的一行
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setBold(true);
        titleFont.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(titleFont);
        styles.put("title", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.LEFT);
        styles.put("subtitle", style);
        return styles;
    }

    /**
     * 创建合并单元格样式
     * @param cellAddresses
     */
    private void createReginStyles(CellRangeAddress cellAddresses) {
        RegionUtil.setBorderTop(BorderStyle.THIN,cellAddresses, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN,cellAddresses, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN,cellAddresses, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN,cellAddresses, sheet);
        RegionUtil.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellAddresses, sheet);
        RegionUtil.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellAddresses, sheet);
        RegionUtil.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellAddresses, sheet);
        RegionUtil.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex(), cellAddresses, sheet);
    }


    /**
     * 创建一个工作簿
     */
    private void createWorkbook() {
        this.wb = new SXSSFWorkbook(500);
    }

    /**
     * 得到所有定义字段
     */
    private void createExcelField(){
        this.fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields){
            // 单注解
            if (field.isAnnotationPresent(Excel.class)){
                putToField(field, field.getAnnotation(Excel.class));
            }
            // 多注解
            if (field.isAnnotationPresent(Excels.class)){
                Excels attrs = field.getAnnotation(Excels.class);
                Excel[] excels = attrs.value();
                for (Excel excel : excels){
                    putToField(field, excel);
                }
            }
        }
    }

    /**
     * 放到字段集合中
     *      Type.ALL  + type
     */
    private void putToField(Field field, Excel attr){
        if (attr != null && (attr.type() == Type.ALL || attr.type() == type)){
            this.fields.add(new Object[] {field, attr});
        }
    }

    /**
     * 获取单元格值
     *
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column){
        if (row == null){
            return row;
        }
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (JeeStringUtils.isNotNull(cell)) {
                if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
                    val = cell.getNumericCellValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        //POI Excel 日期格式转换
                        val = DateUtil.getJavaDate((Double) val);
                    } else {
                        if ((Double) val % 1 > 0) {
                            val = new DecimalFormat("0.00").format(val);
                        } else {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                } else if (cell.getCellTypeEnum() == CellType.STRING){
                    val = cell.getStringCellValue();
                } else if (cell.getCellTypeEnum() == CellType.BOOLEAN){
                    val = cell.getBooleanCellValue();
                } else if (cell.getCellTypeEnum() == CellType.ERROR){
                    val = cell.getErrorCellValue();
                }
            }
        } catch (Exception e){
            return val;
        }
        return val;
    }
}

