package FXX;

public class Chronometre {
    long tempsDepart=0;
    long tempsFin=0;
    long duree=0;

    public void start()
    {
        tempsDepart=System.currentTimeMillis();
        tempsFin=0;
        duree=0;
    }

    public void stop()
    {
        if(tempsDepart==0) {return;}
        tempsFin=System.currentTimeMillis();
        duree=(tempsFin-tempsDepart);
        tempsDepart=0;
        tempsFin=0;
    }

    public long getDureeMs()
    {
        return duree;
    }

}