import java.io.*;
import java.util.ArrayList;

/**
 * Created by qiujiarong on 30/11/2017.
 */
public class MessageStat {

    public static ArrayList<Message> readTxtFile(String filename){
        ArrayList<Message> res= new ArrayList<Message>();

        try {
            File file=new File(filename);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int cnt=0;
                while((lineTxt = bufferedReader.readLine()) != null ){
                    String[] array = lineTxt.split("\t");
                    if(array.length!=3)continue;
                    String sender=array[0];
                    String receiver=array[1];
                    String message=array[2];
                    Message mes = new Message(sender,receiver,message);
                    res.add(mes);
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

    public static void relationOutput(String filename,ArrayList<Relation> relations){
        File file=new File(filename);
        try {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            for (Relation relation:relations) {
                out.write((relation.getSubject()+"\t"+relation.getObject()+"\t"+relation.getRelation()+"\n").getBytes());
            }
            out.close();
        }catch (Exception e) {
            System.out.println("写文件内容出错");
            e.printStackTrace();
        }
    }


    public static ArrayList<Relation> GetRelation(Message message){
        ArrayList<Relation> relations = new ArrayList<Relation>();
        ArrayList<String> candidates=StanfordAPI.NER(message.getMessage());
        for(Relation relation: StanfordAPI.OpenIE(message.getMessage())){
            String sub=relation.getSubject(),obj=relation.getObject();
            if(!(candidates.contains(sub)&&candidates.contains(obj)))continue;
            if(sub.equals("I") || sub.equals("i")){
                sub=message.getSender();
            }else if(sub.equals("You") ||sub.equals("u") || sub.equals("you")){
                sub=message.getReceiver();
            }else if(sub.equals("We")||sub.equals("we")){
                sub=message.getSender()+" and "+ message.getReceiver();
            }
            if(obj.equals("I") || obj.equals("i")){
                obj=message.getSender();
            }else if(obj.equals("You") ||obj.equals("u")||obj.equals("you")){
                obj=message.getReceiver();
            }
            if(sub==relation.getSubject() && obj==relation.getObject())continue;
            relations.add(new Relation(sub,relation.getRelation(),obj));
        }
        return relations;
    }
//    public static ArrayList<MessageRelation> GetRelation(Message message){
//        ArrayList<MessageRelation> messageRelations = new ArrayList<MessageRelation>();
//        ArrayList<Relation> relations = new ArrayList<Relation>();
//        relations.addAll( StanfordAPI.OpenIE(message.getMessage()));
//        for(Relation relation:relations){
//
//
//        }
//
//    }
    public static void main(String[] args) {
        String filename="/Users/qiujiarong/Desktop/DeepScrubb/all_message_0";
        ArrayList<Message> messages= new ArrayList<Message>();
        messages=MessageStat.readTxtFile(filename);
        ArrayList<Relation> relations=new ArrayList<Relation>();
        for(int i=0;i<messages.size();i++){
            if(i%50==0)System.out.println(i);
            Message mes = messages.get(i);
            relations.addAll(GetRelation(mes));
        }
        System.out.println(relations.size());
        MessageStat.relationOutput("/Users/qiujiarong/Desktop/DeepScrubb/all_message_01",relations);

    }
}
