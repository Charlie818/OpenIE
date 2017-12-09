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
//                    if(cnt>50)break;
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

    public static void relationOutput(String filename,ArrayList<MessageRelation> relations){
        File file=new File(filename);
        try {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            for (MessageRelation relation:relations) {
                Relation relation1=relation.getRelation();
                out.write((relation.getUser()+'\t'+
                        relation.getContact()+'\t'+
                        relation1.getSubject()+"\t"+relation1.getObject()+"\t"+relation1.getRelation()+"\n").getBytes());
            }
            out.close();
        }catch (Exception e) {
            System.out.println("写文件内容出错");
            e.printStackTrace();
        }
    }
    public static ArrayList<Relation> GetRelation(String message){
        ArrayList<Relation> relations = new ArrayList<Relation>();
        relations.addAll( StanfordAPI.OpenIE(message));
        return relations;
    }
    public static void main(String[] args) {
        String filename="/Users/qiujiarong/Desktop/CMU/all_message";
        ArrayList<Message> messages= new ArrayList<Message>();
        messages=MessageStat.readTxtFile(filename);
        ArrayList<MessageRelation> relations=new ArrayList<MessageRelation>();
        for(int i=0;i<messages.size();i++){
            if(i%50==0)System.out.println(i);
            Message mes = messages.get(i);
            for(Relation relation:GetRelation(mes.getMessage())) {

                relations.add(new MessageRelation(mes.getSender(), mes.getReceiver(), relation));
            }
        }
        System.out.println(relations.size());
        MessageStat.relationOutput("/Users/qiujiarong/Desktop/CMU/all_message2",relations);

    }
}
