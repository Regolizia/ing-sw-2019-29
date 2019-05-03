package adrenaline;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote {
    public String getMessage(String text) throws RemoteException;
}