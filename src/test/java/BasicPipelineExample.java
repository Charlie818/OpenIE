import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.SentenceFragment;
import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.Mention;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Properties;
/**
 * Created by qiujiarong on 11/11/2017.
 */


public class BasicPipelineExample {
    public static String readTxtFile(){
        String res="";
        String filename="/Users/qiujiarong/Desktop/CMU/message.txt";
        try {
            File file=new File(filename);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int cnt=0;
                while((lineTxt = bufferedReader.readLine()) != null && cnt<10){
                    res+=lineTxt+".";
//                    System.out.println(lineTxt);
                    cnt++;
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return res;
    }
    public static void OpenIE(String line){
        Annotation doc = new Annotation(line);
        Properties props = new Properties();
//        props.setProperty("openie.resolve_coref", "true");
//        props.setProperty("openie.threads", "1");
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,parse,natlog,ner,openie");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(doc);
        // Loop over sentences in the document
        for (CoreMap sentence : doc.get(SentencesAnnotation.class)) {

            // Get the OpenIE triples for the sentence
            Collection<RelationTriple> triples =
                    sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
            // Print the triples
            for (RelationTriple triple : triples) {
                System.out.println(
                        "subject "+triple.subjectLemmaGloss() + "\t" +
                        "relation "+triple.relationLemmaGloss() + "\t" +
                        "object "+triple.objectLemmaGloss());
            }
        }

    }

    public static void NER(String line){
        Annotation doc = new Annotation(line);
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,ner,natlog,openie");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(doc);
        for (CoreMap sentence : doc.get(SentencesAnnotation.class)) {

            for (CoreLabel token : sentence.get(TokensAnnotation.class)){
                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(NamedEntityTagAnnotation.class);
                System.out.println(word+" "+pos+" "+ne);
            }
        }
    }


    public static void main(String[] args) {
//        String line=readTxtFile();
        String line="Can we watch Tokyo Ghoul in 1305 on Friday night or Saturday night please";
        System.out.println(line);
        OpenIE(line);
        NER(line);
////        Annotation doc = new Annotation("Barack Obama was born in Hawaii.  He is the president. Obama was elected in 2008.");
////        Properties props = new Properties();
////        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,mention,coref");
////        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
////        pipeline.annotate(doc);
////        System.out.println("---");
////        System.out.println("coref chains");
////        for (CorefChain cc : doc.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
////            System.out.println("\t" + cc);
////        }
////        for (CoreMap sentence : doc.get(SentencesAnnotation.class)) {
////            System.out.println("---");
////            System.out.println("mentions");
////            for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
////                System.out.println("\t" + m);
////            }
////        }
//
//

//
//
//

    }

}