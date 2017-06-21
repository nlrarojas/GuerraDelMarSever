package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nelson
 */
public class UserManager {
    private List<String> usersList;
    private List<Player> playerList;
    
    public UserManager(){
        this.usersList = new ArrayList<>();
        this.playerList = new ArrayList<>();
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
    
    public boolean addPlayer(Player pPlayer){
        if(!playerList.contains(pPlayer)){
            playerList.add(pPlayer);
            return true;
        }else{
            return false;
        }
    }
    
    public boolean removePlayer(Player pPlayer){
        if(!playerList.contains(pPlayer)){
            playerList.remove(pPlayer);
            return true;
        }else{
            return false;
        }
    }
}
