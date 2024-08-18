public class Helmet { // CLASSE DO RECURSO DO CAPACETE
    private boolean isAvailable;

    public Helmet() {
        this.isAvailable = true;
    }

    public synchronized boolean acquireHelmet() { // adquirir recurso do capacete
        if (isAvailable) {
            isAvailable = false;
            return true;
        }
        return false;
    }

    public synchronized void releaseHelmet() { // liberar recurso do capacete
        isAvailable = true;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

}
