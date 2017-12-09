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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
/**
 * Created by qiujiarong on 30/11/2017.
 */
public class StanfordAPI {
    public static ArrayList<Relation> OpenIE(String line){
        ArrayList<Relation> relations= new ArrayList<Relation>();
        Annotation doc = new Annotation(line);
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,ner,natlog,openie");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(doc);
//        System.out.println(line);
        // Loop over sentences in the document
        for (CoreMap sentence : doc.get(SentencesAnnotation.class)) {

            // Get the OpenIE triples for the sentence
            Collection<RelationTriple> triples =
                    sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
            for (RelationTriple triple : triples){
                Relation relation = new Relation(triple.subjectLemmaGloss(),triple.relationLemmaGloss(),triple.objectLemmaGloss());
                relations.add(relation);
            }
//            // Print the triples
//            for (RelationTriple triple : triples) {
//                System.out.println(
//                        "subject " + triple.subjectLemmaGloss() + "\t" +
//                                "relation " + triple.relationLemmaGloss() + "\t" +
//                                "object " + triple.objectLemmaGloss());
//            }
        }
        return  relations;
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
}
