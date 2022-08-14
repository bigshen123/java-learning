package com.bigshen.learningDemo.utils.idGenerator;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
* @Description: 代码生成器
*/
public class CodeGenerator {

    public  String DB_URL = "jdbc:mysql://localhost:3306/ds_workorder?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";
    public  String USER_NAME = "root";
    public  String PASSWORD = "root";
    public  String DRIVER = "com.mysql.jdbc.Driver";
    public  String AUTHOR = "byj";

  // 生成的文件输出到哪个目录
  public String OUTPUT_FILE = "E:\\ds-new\\file\\src\\main\\java";
    //包名，会按照com/example/demo这种形式生成类
    public String PACKAGE = "com.ds.userservice";
    //TODO 更多配置请参考http://mp.baomidou.com/#/generate-code

    public CodeGenerator(){

    }

    public CodeGenerator(String dbUrl, String dbUsername, String dbPassword, String outFile, String basePackage){
        this.DB_URL=dbUrl;
        this.USER_NAME=dbUsername;
        this.PASSWORD=dbPassword;
        this.OUTPUT_FILE=outFile;
        this.PACKAGE=basePackage;
    }

    public void generateByTables(boolean serviceNameStartWithI, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(DB_URL)
                .setUsername(USER_NAME)
                .setPassword(PASSWORD)
                .setDriverName(DRIVER);
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(false)
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组
        config.setActiveRecord(false)
                .setAuthor(AUTHOR)
                .setOutputDir(OUTPUT_FILE)
                .setFileOverride(true);
        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
        }
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(PACKAGE)
                                .setController("controller")
                                .setEntity("entity")
                                .setXml("mapper.xml")
                                .setMapper("mapper")
                ).execute();
    }
}

