package user;

/**
 * Created by Juan Carlos on 12/05/2017.
 */

public class Logros {

    private String id_user;
    private int id_project;

    // Logros para obtener las medallas de las diferentes categorias
    private int fauna = 0;
    private int nutricion = 0;
    private int medicina = 0;
    private int agua = 0;
    private int medioambiente = 0;
    private int flora = 0;
    private int investigacion = 0;
    private int educacion = 0;

    // Logros especiales
    private int semanaJuego = 0;
    private int mesJuego = 0;
    private int cincomil = 0;
    private int publi = 0;

    // Puntos totales obtenidos en cada una de las categorias
    private int p_fauna = 0;
    private int p_nutricion = 0;
    private int p_medicina = 0;
    private int p_agua = 0;
    private int p_medioambiente = 0;
    private int p_flora = 0;
    private int p_investigacion = 0;
    private int p_educacion = 0;


    // Puntos partida actual para el proyecto actual
    private int puntos_actuales = 0;
    // Puntos totales acumulados en todas las categorias
    private int total_puntos = 0;

    private int best = 0; // record personal

    public Logros(){

    }


    // Getters and Setters


    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public int getId_project() {
        return id_project;
    }

    public void setId_project(int id_project) {
        this.id_project = id_project;
    }

    public int getFauna() {
        return fauna;
    }

    public void setFauna(int fauna) {
        this.fauna = fauna;
    }

    public int getNutricion() {
        return nutricion;
    }

    public void setNutricion(int nutricion) {
        this.nutricion = nutricion;
    }

    public int getMedicina() {
        return medicina;
    }

    public void setMedicina(int medicina) {
        this.medicina = medicina;
    }

    public int getAgua() {
        return agua;
    }

    public void setAgua(int agua) {
        this.agua = agua;
    }

    public int getMedioambiente() {
        return medioambiente;
    }

    public void setMedioambiente(int medioambiente) {
        this.medioambiente = medioambiente;
    }

    public int getFlora() {
        return flora;
    }

    public void setFlora(int flora) {
        this.flora = flora;
    }

    public int getInvestigacion() {
        return investigacion;
    }

    public void setInvestigacion(int investigacion) {
        this.investigacion = investigacion;
    }

    public int getEducacion() {
        return educacion;
    }

    public void setEducacion(int educacion) {
        this.educacion = educacion;
    }

    public int getSemanaJuego() {
        return semanaJuego;
    }

    public void setSemanaJuego(int semanaJuego) {
        this.semanaJuego = semanaJuego;
    }

    public int getMesJuego() {
        return mesJuego;
    }

    public void setMesJuego(int mesJuego) {
        this.mesJuego = mesJuego;
    }

    public int getCincomil() {
        return cincomil;
    }

    public void setCincomil(int cincomil) {
        this.cincomil = cincomil;
    }

    public int getPubli() {
        return publi;
    }

    public void setPubli(int publi) {
        this.publi = publi;
    }

    public int getP_fauna() {
        return p_fauna;
    }

    public void setP_fauna(int p_fauna) {
        this.p_fauna = p_fauna;
    }

    public int getP_nutricion() {
        return p_nutricion;
    }

    public void setP_nutricion(int p_nutricion) {
        this.p_nutricion = p_nutricion;
    }

    public int getP_medicina() {
        return p_medicina;
    }

    public void setP_medicina(int p_medicina) {
        this.p_medicina = p_medicina;
    }

    public int getP_agua() {
        return p_agua;
    }

    public void setP_agua(int p_agua) {
        this.p_agua = p_agua;
    }

    public int getP_medioambiente() {
        return p_medioambiente;
    }

    public void setP_medioambiente(int p_medioambiente) {
        this.p_medioambiente = p_medioambiente;
    }

    public int getP_flora() {
        return p_flora;
    }

    public void setP_flora(int p_flora) {
        this.p_flora = p_flora;
    }

    public int getP_investigacion() {
        return p_investigacion;
    }

    public void setP_investigacion(int p_investigacion) {
        this.p_investigacion = p_investigacion;
    }

    public int getP_educacion() {
        return p_educacion;
    }

    public void setP_educacion(int p_educacion) {
        this.p_educacion = p_educacion;
    }

    public int getPuntos_actuales() {
        return puntos_actuales;
    }

    public void setPuntos_actuales(int puntos_actuales) {
        this.puntos_actuales = puntos_actuales;
    }

    public int getTotal_puntos() {
        return total_puntos;
    }

    public void setTotal_puntos(int total_puntos) {
        this.total_puntos = total_puntos;
    }

    public int getBest() {
        return best;
    }

    public void setBest(int best) {
        this.best = best;
    }

    @Override
    public String toString() {
        return "Logros{" +
                "id_user='" + id_user + '\'' +
                ", id_project=" + id_project +
                ", fauna=" + fauna +
                ", nutricion=" + nutricion +
                ", medicina=" + medicina +
                ", agua=" + agua +
                ", medioambiente=" + medioambiente +
                ", flora=" + flora +
                ", investigacion=" + investigacion +
                ", educacion=" + educacion +
                ", semanaJuego=" + semanaJuego +
                ", mesJuego=" + mesJuego +
                ", cincomil=" + cincomil +
                ", publi=" + publi +
                ", p_fauna=" + p_fauna +
                ", p_nutricion=" + p_nutricion +
                ", p_medicina=" + p_medicina +
                ", p_agua=" + p_agua +
                ", p_medioambiente=" + p_medioambiente +
                ", p_flora=" + p_flora +
                ", p_investigacion=" + p_investigacion +
                ", p_educacion=" + p_educacion +
                ", puntos_actuales=" + puntos_actuales +
                ", total_puntos=" + total_puntos +
                ", best=" + best +
                '}';
    }
}
