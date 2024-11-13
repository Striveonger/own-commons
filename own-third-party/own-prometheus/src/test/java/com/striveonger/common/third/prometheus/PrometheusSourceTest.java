package com.striveonger.common.third.prometheus;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import junit.framework.TestCase;
import org.apache.poi.ss.usermodel.Cell;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PrometheusSourceTest extends TestCase {

    public void test() throws IOException {

        PrometheusConfig config = new PrometheusConfig();
        config.setHost("192.168.10.100");
        config.setPort(59090);
        config.setTimeout(5000);
        PrometheusKit prometheus = PrometheusKit.Builder.builder().config(config).build();


        Set<Dict> rows = new HashSet<>();

        List<Pair<String, String>> pairs = List.of(
                // Pair.of("通用基础套件", "Nacos注册中心"),
                // Pair.of("通用基础套件", "达梦数据库"),
                // Pair.of("通用基础套件", "Redis内存数据库"),
                // Pair.of("通用基础套件", "Postgresql")
                Pair.of("数据中台运营", "监控数据采集"),
                Pair.of("数据中台运营", "告警管理"),
                Pair.of("数据中台运营", "多维视图展现"),
                Pair.of("数据中台运营", "智能运维")
        );

        for (Pair<String, String> pair : pairs) {
            String objectType = pair.getKey();
            String objectName = pair.getValue();
            List<ObjectNode> list = prometheus.metadatas(List.of(Pair.of("object_name", objectName), Pair.of("object_type", objectType)));
            List<Dict> part = list.stream().map(x -> {
                String metric = x.get("metric").asText();
                String type = x.get("type").asText();
                String help = x.get("help").asText();
                if (metric.startsWith("hikaricp_")) {
                    return new Dict(metric, metric, type, help, "通用基础套件", "JVM指标");
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).toList();
            rows.addAll(part);
        }

        rows.forEach(x -> System.out.println(x.sql()));

        // 写入excel文件
        // XSSFWorkbook workbook = new XSSFWorkbook();
        // XSSFSheet sheet = workbook.createSheet();
        // XSSFRow tableHead = sheet.createRow(0);
        // tableHead.createCell(0).setCellValue("指标名称");
        // tableHead.createCell(1).setCellValue("指标名称(中文)");
        // tableHead.createCell(2).setCellValue("指标类型");
        // tableHead.createCell(3).setCellValue("指标说明");
        // tableHead.createCell(4).setCellValue("监控对象分类");
        // tableHead.createCell(5).setCellValue("监控对象名称");
        // for (int i = 0; i < rows.size(); i++) {
        //     // Dict row = rows.get(i);
        //     XSSFRow xssfRow = sheet.createRow(i + 1);
        //     xssfRow.createCell(0).setCellValue(row.getName());
        //     xssfRow.createCell(1).setCellValue(row.getNameCn());
        //     xssfRow.createCell(2).setCellValue(row.getType());
        //     xssfRow.createCell(3).setCellValue(row.getHelp());
        //     xssfRow.createCell(4).setCellValue(row.getObjectType());
        //     xssfRow.createCell(5).setCellValue(row.getIndicatorType());
        // }
        //
        // ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // workbook.write(bos);
        // byte[] bytes = bos.toByteArray();
        // bos.close();
        // workbook.close();
        // FileUtil.writeBytes(bytes, "/Users/striveonger/temp/metrics1.xlsx");
    }

    public void test2() {
        PrometheusConfig config = new PrometheusConfig();
        config.setHost("10.13.144.116");
        config.setPort(9090);
        config.setTimeout(5000);
        PrometheusKit prometheus = PrometheusKit.Builder.builder().config(config).build();

        List<ObjectNode> targets = prometheus.targets();
        System.out.println(targets);

        List<ObjectNode> result = prometheus.query("{job='postgres'}");
        System.out.println(result);
    }

    public void test3() throws Exception {
        Set<Dict> set = new HashSet<>();
        ExcelReader reader = ExcelUtil.getReader("/Users/striveonger/temp/metrics-0829.xls");
        for (int i = 1; i < reader.getRowCount(); i++) {
            Dict dict = new Dict(
                    Optional.ofNullable(reader.getCell(0, i)).map(Cell::getStringCellValue).orElse(""),
                    Optional.ofNullable(reader.getCell(1, i)).map(Cell::getStringCellValue).orElse(""),
                    Optional.ofNullable(reader.getCell(2, i)).map(Cell::getStringCellValue).orElse(""),
                    Optional.ofNullable(reader.getCell(3, i)).map(Cell::getStringCellValue).orElse(""),
                    Optional.ofNullable(reader.getCell(4, i)).map(Cell::getStringCellValue).orElse(""),
                    Optional.ofNullable(reader.getCell(5, i)).map(Cell::getStringCellValue).orElse("")
            );
            if (set.contains(dict)) {
                continue;
            }
            set.add(dict);
        }
        System.out.println(set.size());

        List<String> list = set.stream().map(Dict::sql).toList();
        byte[] bytes = String.join("\n", list).getBytes(StandardCharsets.UTF_8);
        FileUtil.writeBytes(bytes, "/Users/striveonger/temp/metrics-0829.sql");
    }
}

class Dict {
    private final String name;
    private final String nameCn;
    private final String type;
    private final String help;
    private final String objectType;
    private final String indicatorType;

    public Dict(String name, String nameCn, String type, String help, String objectType, String indicatorType) {
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

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return name.equals(((Dict) obj).getName());
    }

    public String sql() {
        return String.format("""
                INSERT INTO OMM2.INDICATOR_DICT (NAME, NAME_CN, TYPE, HELP, OBJECT_TYPE, INDICATOR_TYPE) VALUES ('%s', '%s', '%s', '%s', '%s', '%s');
                """,
                name, nameCn, type, help.replace("'", " "), objectType, indicatorType
                );
    }
}
