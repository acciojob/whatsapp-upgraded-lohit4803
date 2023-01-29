package com.driver;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WhatsappService {
    WhatsappRepository whatsappRepository= new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception{
      return  whatsappRepository.createUser(name, mobile);
    }

    public Group createGroup(List<User> users) {
        return whatsappRepository.createGroup( users);
    }

    public int createMessage(String cont) {
        return whatsappRepository.createMessage(cont);
    }

    public int sendMessage(Message msg, User sdr, Group grp) throws Exception {
        return whatsappRepository.sendMessage(msg,sdr,grp);
    }

    public String changeAdmin(User ap, User u, Group grp) throws Exception {
        return whatsappRepository.changeAdmin(ap,u,grp);
    }

    public int removeUser(User user) throws Exception {
        return whatsappRepository.removeUser(user);
    }

    public String findMessage(Date s, Date e, int K) {
        return null;
    }
}
