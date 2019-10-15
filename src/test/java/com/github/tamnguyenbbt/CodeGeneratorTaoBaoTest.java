package com.github.tamnguyenbbt;

import com.github.tamnguyenbbt.dom.*;
import com.sun.deploy.net.HttpUtils;
import com.xiaoleilu.hutool.http.HttpUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.seimicrawler.xpath.JXDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CodeGeneratorTaoBaoTest
{
    private DomUtil domUtil;

    @Before
    public void init()
    {
        domUtil = new DomUtil();
    }

    @Test
    public void simpleTestMethodCodeGen() throws IOException
    {

        String content = HttpUtil.get("https://www.baidu.com");



        //Arrange
        Document document = domUtil.getDocument(content);
        CodeGenerator codeGenerator = new CodeGenerator(document, new SeleniumCodeGenAssociation());

        //Act
        String className = codeGenerator.getCodeGenClassName();


    }



}
