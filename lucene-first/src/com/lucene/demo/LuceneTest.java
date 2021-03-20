package com.lucene.demo;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class LuceneTest {
    /**
     * 创建索引
     * @throws IOException
     */
    @Test
    public void createIndex() throws IOException {
        //1.指定索引库存放的路径   索引库还可以存放在内存中
        FSDirectory fsDirectory = FSDirectory.open(new File("E:\\IDEA\\index").toPath());

        //2.创建indexwriterConfig对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig();

        //创建indexWriter对象
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);

        //3.原始文档的路径
        File dir = new File("E:\\java学习内容\\Lucene\\02-资料\\searchsource");

        //4.遍历读取文件
        for (File f : dir.listFiles() ) {
            //文件名
            String fileName = f.getName();
            //文件内存
            String fileContent = FileUtils.readFileToString(f,"utf-8");
            //文件路径
            String filePath = f.getPath();
            //文件大小
            long fileSize = FileUtils.sizeOf(f);

            //5.创建文件名域
            //第一个参数：域的名称
            //第二个参数：域的内容
            //第三个参数：是否存储
            Field fileNameField = new TextField("fileName", fileName, Field.Store.YES);
            //文件内容域
            Field fileContentField = new TextField("fileContent",fileContent,Field.Store.YES);
            //文件路径域(不分析,不索引,只存储)
            Field filePathField = new TextField("filePath",filePath,Field.Store.YES);
            //文件大小域
            Field fileSizeField = new TextField("fileSize",fileSize+"",Field.Store.YES);

            //6.创建document对象
            Document document = new Document();
            document.add(fileNameField);
            document.add(fileContentField);
            document.add(filePathField);
            document.add(fileSizeField);
            //7.创建索引并写入索引库
            indexWriter.addDocument(document);
        }
        //8.关闭indexWriter
        indexWriter.close();
    }

    /**
     * 第一步：创建一个Directory对象，也就是索引库存放的位置。
     *
     * 第二步：创建一个indexReader对象，需要指定Directory对象。
     *
     * 第三步：创建一个indexsearcher对象，需要指定IndexReader对象
     *
     * 第四步：创建一个TermQuery对象，指定查询的域和查询的关键词。
     *
     * 第五步：执行查询。
     *
     * 第六步：返回查询结果。遍历查询结果并输出。
     *
     * 第七步：关闭IndexReader对象
     */
    @Test
    public void searchIndex() throws IOException {
        //第一步：创建一个Directory对象，也就是索引库存放的位置。
        FSDirectory directory = FSDirectory.open(new File("E:\\IDEA\\index").toPath());

        //第二步：创建一个indexReader对象，需要指定Directory对象。
        IndexReader indexReader = DirectoryReader.open(directory);

        //第三步：创建一个indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        ///第四步：创建一个TermQuery对象，指定查询的域和查询的关键词。
        Query query = new TermQuery(new Term("fileName", "要更新的文档"));

        //执行查询
        //第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);
        //查询结果的总条数
        System.out.println("查询结果的总条数是:"+topDocs.totalHits);
        //遍历查询结果
        //topDocs.scoreDocs存储了document对象的id
        for (ScoreDoc scoreDoc : topDocs.scoreDocs){
            //scoreDoc.doc属性就是document对象的id
            //根据document的id找到document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("fileName"));
            //System.out.println(document.get("fileContent"));
            System.out.println(document.get("filePath"));
            System.out.println(document.get("fileSize"));
            System.out.println("--------------------------------------");
        }
        indexReader.close();
    }

    @Test
    public void testTokenStream() throws IOException {
        //创建一个标准分词器对象
        //Analyzer analyzer = new StandardAnalyzer();
        IKAnalyzer analyzer = new IKAnalyzer();
        //获得tokenStream对象
        //第一个参数：域名，可以随便给一个
        //第二个参数：要分析的文本内容
        TokenStream tokenStream = analyzer.tokenStream("test", "你最好赶紧滚");
        //添加一个引用，可以获得每个关键词
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //添加一个偏移量的引用，记录了关键词的开始位置以及结束位置
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        //将指针调整到列表的头部
        tokenStream.reset();
        //遍历关键词列表，通过incrementToken方法判断列表是否结束
        while(tokenStream.incrementToken()){
            //关键词的起始位置
            System.out.println("start->"+offsetAttribute.startOffset());
            //取关键词
            System.out.println(charTermAttribute);
            //结束位置
            System.out.println("end->" + offsetAttribute.endOffset());
        }
        tokenStream.close();
    }
}
