package com.lucene.demo;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class IndexTest {

    private IndexWriter indexWriter;


    @Before
    public void init() throws IOException {
        indexWriter = new IndexWriter(FSDirectory.open(new File("E:\\IDEA\\index").toPath()),
                new IndexWriterConfig(new IKAnalyzer()));
    }


    @Test
    public void addDocument() throws IOException {
        //1.创建一个IndexWriter对象,并使用IK分词器

        //2.创建Document对象
        Document document = new Document();
        //3.向document对象中添加域
        document.add(new TextField("name","新添加的文件", Field.Store.YES));
        document.add(new TextField("content","新添加的文件内容",Field.Store.NO));
        document.add(new StoredField("path","E:\\IDEA\\tem"));
        //4.把文档写入到索引库
        indexWriter.addDocument(document);
        //5.关闭索引库
        indexWriter.close();
    }

    //全部删除索引库
    @Test
    public void deleteAll() throws IOException {
        //删除全部文档
        indexWriter.deleteAll();
        //关闭indexWriter
        indexWriter.close();
    }

    @Test
    public void deleteDocumentByQuery() throws IOException {
        indexWriter.deleteDocuments(new Term("fileName","apache"));
        indexWriter.close();
    }

    @Test
    public void updateDocument() throws IOException {
        Document document = new Document();
        //向document中添加对象域
        //不同的document可以有不同的域,同一个document可以有相同的域
        document.add(new TextField("name","要后的文档",Field.Store.YES));
        document.add(new TextField("name1","要后的文档1",Field.Store.YES));
        document.add(new TextField("name2","要后的文档2",Field.Store.YES));
        indexWriter.updateDocument(new Term("name","web"),document);
        indexWriter.close();
    }
}
