package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nelson
 */
public class UserManager {
    private List<String> usersList;
    
    public UserManager(){
        this.usersList = new ArrayList<>();
    }
    
    public void addUser(String userNickName){
        usersList.add(userNickName);
    }
    
    public List<String> getUserList(){
        return usersList;
    }
    
    public boolean validateUserExists(String userNickName){
        return usersList.contains(userNickName);
    }
}
