public class Kart { // CLASSE DO RECURSO DO KART
    private boolean isAvailable;

    public Kart() {
        this.isAvailable = true;
    }

    public synchronized boolean acquireKart() { // adquirir recurso do kart
        if (isAvailable) {
            isAvailable = false;
            return true;
        }
        return false;
    }

    public synchronized void releaseKart() { // adquirir recurso do kart
        isAvailable = true;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

}
