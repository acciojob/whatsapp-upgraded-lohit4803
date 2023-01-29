package com.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhatsappRepository {


//    hashmap of user name and user object
    HashMap<String,User> userMap = new HashMap<>();
//    hashmap of group name and group object
    HashMap<String,Group> groupMap = new HashMap<>();
//    hashmap of message id and message object
    HashMap<Integer,Message> messageMap = new HashMap<>();
//    hashmap of group name and user mobile number who will be the admin
    HashMap<String,String> adminMap = new HashMap<>();
//    hashmap of group name and users list
    HashMap<String,List<User>> groupUserMap = new HashMap<>();
//    hashmap of user mobileno and list of Messages
    HashMap<String,List<Message>> userMessageMap = new HashMap<>();
    //    hashmap of group name and list of Messages
    HashMap<String,List<Message>> groupMessageMap= new HashMap<>();

    public String createUser(String name, String mobile) throws Exception{
        if(userMap.containsKey(mobile)){
            throw  new Exception("User already exists");
        }
        else userMap.put(mobile,new User(name,mobile) );
        return "SUCCESS";
    }


    public Group createGroup(List<User> users) {
        String name;
        int numberOfParticipants;
        int count =1;

        if(users.size() ==2){
            name = users.get(1).getName();
            numberOfParticipants= users.size();
        }else{
            numberOfParticipants = users.size();
            for(Map.Entry<String,Group> map  : groupMap.entrySet()){
                String key = map.getKey();
               if( key.contains("Group")){
                   count++;
               }
            }
            name = "Group "+ count;
        }

        Group group = new Group(name,numberOfParticipants);
        groupMap.put(name, group);
        groupUserMap.put(name,users);
        adminMap.put(name,users.get(0).getMobile());
        return group;
    }

    public int createMessage(String content) {
        int id = messageMap.size() + 1;

        Message message = new Message(id,content);
        messageMap.put(id,message);
        return id;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!groupMap.containsKey(group.getName())){
            throw new Exception("Group does not exist");
        }
       List<User> userList = groupUserMap.get(group.getName());
       if (!userList.contains(sender)){
           throw new Exception("You are not allowed to send message");
       }

        List<Message> groupMessages = new ArrayList<>();
       if(groupMessageMap.containsKey(group.getName())){
           groupMessages = groupMessageMap.get(group.getName());
       }

        groupMessages.add(message);

       groupMessageMap.put(group.getName(),groupMessages);
        List<Message> userMessages = new ArrayList<>();
        if(userMessageMap.containsKey(sender.getMobile())){
            userMessages = userMessageMap.get(sender.getMobile());
        }
        userMessages.add(message);
        userMessageMap.put(sender.getMobile(),userMessages);
        messageMap.put(message.getId(),message);
        return groupMessages.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!groupMap.containsKey(group.getName())){
            throw new Exception("Group does not exist");
        }
        if(!adminMap.containsValue(approver.getMobile())){
            throw new Exception("Approver does not have rights");
        }
        List<User> userList = groupUserMap.get(group.getName());
        if (!userList.contains(user)){
            throw new Exception("User is not a participant");
        }
        adminMap.put(group.getName(),user.getMobile());
        return "SUCCESS";
    }
    public int removeUser(User user) throws Exception{
        boolean foundUser = false;
        String groupName = null;
       for(Map.Entry<String ,List<User>> map : groupUserMap.entrySet()){
          if( map.getValue().contains(user)){
                foundUser = true;
              groupName = map.getKey();
           }
       }
       if(!foundUser){
           throw new Exception("User not found");
       }
       if(adminMap.containsValue(user.getMobile())){
           throw new Exception("Cannot remove admin");
       }
        userMap.remove(user.getMobile());
       groupUserMap.get(groupName).remove(user);
      Group group = groupMap.get(groupName);
        group.setNumberOfParticipants(group.getNumberOfParticipants()-1);
       List<Message> UserMessageList= userMessageMap.get(user.getMobile());
        List<Message> groupMessageList=groupMessageMap.get(group.getName());
        groupMessageList.removeAll(UserMessageList);
        for(Message message: UserMessageList){
            messageMap.remove(message.getId());
        }
        userMessageMap.remove(user.getMobile());

        return groupUserMap.get(groupName).size()+groupMessageMap.get(groupName).size()+messageMap.size();
    }

}
