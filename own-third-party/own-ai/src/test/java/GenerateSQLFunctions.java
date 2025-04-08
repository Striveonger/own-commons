import com.agentsflex.core.llm.functions.annotation.FunctionDef;
import com.agentsflex.core.llm.functions.annotation.FunctionParam;

public class GenerateSQLFunctions {

    @FunctionDef(name = "generateSQL", description = "给出可执行的SQL语句")
    public static String generateSQL( @FunctionParam(name = "sql", description = "可执行的SQL语句") String sql) {
        System.out.println(sql);
        return sql;
    }
}
