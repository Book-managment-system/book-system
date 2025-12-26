package org.example.backend.model.dto;

import org.example.backend.model.entity.User;


public class customerHistory {
    User user;
    int numberoforders;
    public customerHistory(User user, int numberoforders) {
        this.user = user;
        this.numberoforders = numberoforders;
    }

    public void setNumberoforders(int numberoforders) {
        this.numberoforders = numberoforders;
    }
    public int getNumberoforders() {
        return numberoforders;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}
