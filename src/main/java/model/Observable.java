package model;

import model.Observer;

public interface Observable {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(String bericht);
}
