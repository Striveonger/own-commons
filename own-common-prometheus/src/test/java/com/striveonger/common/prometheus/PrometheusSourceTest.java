package com.striveonger.common.prometheus;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import com.fasterxml.jackson.databind.node.ObjectNode;
import junit.framework.TestCase;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrometheusSourceTest extends TestCase {

    public void test() throws IOException {

        PrometheusConfig config = new PrometheusConfig();
        config.setHost("192.168.10.100");
        config.setPort("59090");
        config.setTimeout(5000);
        PrometheusSource prometheus = PrometheusSource.Builder.builder().config(config).build();


        List<Row> rows = new ArrayList<>();

        List<Pair<String, String>> pairs = List.of(
                Pair.of("通用基础套件", "Nacos注册中心"),
                Pair.of("通用基础套件", "达梦数据库"),
                Pair.of("通用基础套件", "Redis内存数据库"),
                Pair.of("通用基础套件", "Postgresql")
        );

        for (Pair<String, String> pair : pairs) {
            String objectType = pair.getKey();
            String objectName = pair.getValue();
            List<ObjectNode> list = prometheus.metadatas(List.of(Pair.of("object_name", objectName), Pair.of("object_type", objectType)));
            List<Row> part = list.stream().map(x -> {
                String metric = x.get("metric").asText();
                String type = x.get("type").asText();
                String help = x.get("help").asText();
                return new Row(metric, metric, type, help, objectType, objectName);
            }).toList();
            rows.addAll(part);
        }

        // 写入excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow tableHead = sheet.createRow(0);
        tableHead.createCell(0).setCellValue("指标名称");
        tableHead.createCell(1).setCellValue("指标名称(中文)");
        tableHead.createCell(2).setCellValue("指标类型");
        tableHead.createCell(3).setCellValue("指标说明");
        tableHead.createCell(4).setCellValue("监控对象分类");
        tableHead.createCell(5).setCellValue("监控对象名称");
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            XSSFRow xssfRow = sheet.createRow(i + 1);
            xssfRow.createCell(0).setCellValue(row.getName());
            xssfRow.createCell(1).setCellValue(row.getNameCn());
            xssfRow.createCell(2).setCellValue(row.getType());
            xssfRow.createCell(3).setCellValue(row.getHelp());
            xssfRow.createCell(4).setCellValue(row.getObjectType());
            xssfRow.createCell(5).setCellValue(row.getIndicatorType());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] bytes = bos.toByteArray();
        bos.close();
        workbook.close();
        FileUtil.writeBytes(bytes, "/Users/striveonger/temp/metrics.xlsx");
    }

    public void test2() {
        PrometheusConfig config = new PrometheusConfig();
        config.setHost("10.13.144.116");
        config.setPort("9090");
        config.setTimeout(5000);
        PrometheusSource prometheus = PrometheusSource.Builder.builder().config(config).build();

        List<ObjectNode> targets = prometheus.targets();
        System.out.println(targets);

        List<ObjectNode> result = prometheus.query("{job='postgres'}");
        System.out.println(result);
    }

}

class Row {
    private final String name;
    private final String nameCn;
    private final String type;
    private final String help;
    private final String objectType;
    private final String indicatorType;

    public Row(String name, String nameCn, String type, String help, String objectType, String indicatorType) {
        this.name = name;
        this.nameCn = nameCn;
        this.type = type;
        this.help = help;
        this.objectType = objectType;
        this.indicatorType = indicatorType;
    }

    public String getName() {
        return name;
    }

    public String getNameCn() {
        return nameCn;
    }

    public String getType() {
        return type;
    }

    public String getHelp() {
        return help;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getIndicatorType() {
        return indicatorType;
    }
}
