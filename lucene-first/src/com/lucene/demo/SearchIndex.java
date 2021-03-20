package com.lucene.demo;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class SearchIndex {

    private IndexSearcher indexSearcher;
    private IndexReader indexReader;

    @Test
    public void init()throws Exception{
        indexReader = DirectoryReader.open(FSDirectory.open(new File("E:\\IDEA\\index").toPath()));
        indexSearcher = new IndexSearcher(indexReader);
    }


    @Test
    public void testQueryParser() throws Exception{
        //创建一个QueryParser对象
        //默认叫搜索的域 第二个参数是分词器
        QueryParser queryParser = new QueryParser("name", new IKAnalyzer());
        //通过QueryParser创建一个query对象
        Query query = queryParser.parse("lucene是一个java的开发的全文检索的工具包");
        //执行查询
        
    }
}
