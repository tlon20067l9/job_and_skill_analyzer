package service;

import Data.UserDATA;
import Nguoi_Dung.User;

public class AuthService {
    private UserDATA userDATA;

    public AuthService() {
        this.userDATA = new UserDATA();
    }

    public User login(String email, String password) {
        return userDATA.getUserByEmailAndPassword(email, password);
    }
}
