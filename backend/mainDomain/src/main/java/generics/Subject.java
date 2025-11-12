package generics;

import java.util.Observable;
import java.util.Observer;

public interface Subject {
    void attach(Observable observer);
    void detach(Observer observer);
    void notifyObservers(String message);
}