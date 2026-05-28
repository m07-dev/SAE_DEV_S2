package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.scene.paint.Color;

public abstract class Personnage {
    private double x, y;
    private Terrain e;
    private int pv;
    private int vitesse;

    // -- Effets de statut --
    private int ticksBrulure = 0;        // nombre de ticks de brûlure restants
    private boolean ralenti = false;     // est-ce que l'ennemi est actuellement ralenti ?
    private int vitesseOriginale = 0;    // vitesse avant le ralentissement (pour la restaurer)
    private int ticksRalentissement = 0; // nombre de ticks de ralentissement restants

    public Personnage(double x, double y, Terrain e, int pv, int v) {
        this.x = x;
        this.y = y;
        this.e = e;
        this.pv = pv;
        this.vitesse = v;
    }

    // Applique un ralentissement pendant X ticks
    public void setRalenti(int duree) {
        if (!this.ralenti) {
            // Première fois → on sauvegarde la vitesse et on la divise par 2
            this.vitesseOriginale = this.vitesse;
            this.vitesse = Math.max(1, this.vitesse / 2);
            this.ralenti = true;
        }
        // Recharge le timer (même si déjà ralenti)
        this.ticksRalentissement = duree;
    }

    // Applique une brûlure pendant X ticks
    public void setTicksBrulure(int ticks) {
        this.ticksBrulure = ticks;
    }

    // Appelée à chaque tick → met à jour tous les effets actifs
    public void mettreAJourEffets() {

        // Effet brûlure
        if (ticksBrulure > 0) {
            this.subirDegat(2);   // perd 2 PV par tick
            ticksBrulure--;
        }

        // Effet ralentissement
        if (ralenti) {
            ticksRalentissement--;
            if (ticksRalentissement <= 0) {
                // Le ralentissement est terminé → on restaure la vitesse
                this.vitesse = this.vitesseOriginale;
            }
        }
    }

    // --- Getters / Setters existants ---
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public int getPv() { return pv; }
    public void setPv(int nvPv) { this.pv = Math.max(0, nvPv); }
    public void setVitesse(int v) { this.vitesse = v; }
    public int getVitesse() { return this.vitesse; }
    public int getRecompense() { return 10; }

    public void subirDegat(int degat) { this.setPv(this.pv - degat); }
    public boolean estMort() { return this.pv == 0; }
    public boolean horsDesLimites() {
        return this.x < 0 || this.x >= 32*30 || this.y < 0 || this.y >= 30*32;
    }

    public javafx.scene.paint.Color getCouleur() { return javafx.scene.paint.Color.BLACK; }
}